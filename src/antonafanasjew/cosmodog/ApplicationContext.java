package antonafanasjew.cosmodog;

import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.EnergyWallCollisionValidator;
import antonafanasjew.cosmodog.collision.FeatureBoundCollisionValidator;
import antonafanasjew.cosmodog.collision.GeneralCollisionValidator;
import antonafanasjew.cosmodog.collision.InterCharacterCollisionValidator;
import antonafanasjew.cosmodog.collision.OneBlocksAllCollisionValidator;
import antonafanasjew.cosmodog.controller.DebugConsoleInputHandler;
import antonafanasjew.cosmodog.controller.InGameControlInputHandler;
import antonafanasjew.cosmodog.controller.InGameDialogInputHandler;
import antonafanasjew.cosmodog.controller.InGameInputHandler;
import antonafanasjew.cosmodog.controller.InGameTextFrameInputHandler;
import antonafanasjew.cosmodog.controller.InputHandler;
import antonafanasjew.cosmodog.controller.NoInputInputHandler;
import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogScorePersistor;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.User;
import antonafanasjew.cosmodog.pathfinding.DefaultTravelTimeCalculator;
import antonafanasjew.cosmodog.pathfinding.EnemySightBasedDecitionPathFinder;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.pathfinding.RandomPathFinder;
import antonafanasjew.cosmodog.pathfinding.TowardsPlayerPathFinder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.resourcehandling.GenericResourceWrapper;
import antonafanasjew.cosmodog.resourcehandling.builder.animations.AnimationBuilder;
import antonafanasjew.cosmodog.sight.DayTimeAffectedSightRadiusCalculator;
import antonafanasjew.cosmodog.sight.DefaultSightRadiusCalculator;
import antonafanasjew.cosmodog.sight.SightRadiusCalculator;
import antonafanasjew.cosmodog.text.DefaultLetterBuilder;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterBuilder;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapReader;
import antonafanasjew.cosmodog.tiledmap.io.XmlTiledMapReader;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Note: The application context needs to be instantiated only from a OpenGL bound thread. Do not 
 * instantiate it from the main thread (see also comment in the init method).
 * 
 * This singleton will contain the cosmodog model as well as all external resourses, like animations, sprite sheets, sounds and the tiled map.
 * 
 * Think of it as a spring context.
 * 
 */
public class ApplicationContext {

	private static ApplicationContext instance = null;
	
	/**
	 * Single instance of the application context. Will be initialized with the first call.
	 * @return The single instance of the application context.
	 */
	public static ApplicationContext instance() {
		if (instance == null) {
			synchronized(ApplicationContext.class) {
				if (instance == null) {
					instance = new ApplicationContext();
				}
			}
		}
		
		return instance;
	}
	
	
	private Cosmodog cosmodog;
	private CustomTiledMap customTiledMap;
	private SoundResources soundResources = new SoundResources();
	private Animations animations = new Animations();
	private SpriteSheets spriteSheets = new SpriteSheets();
	private TiledMapReader tiledMapReader = new XmlTiledMapReader(Constants.PATH_TO_TILED_MAP);
	
	private Map<Character, Letter> characterLetters = Maps.newHashMap();
	
	//We need this global variable to link the writing text box state to the place where it will be drawn.
	private DrawingContext dialogBoxDrawingContext = null;

	private ApplicationContext() {
		try {
			init();
		} catch (SlickException e) {
			Log.error("Could not initialize application context", e);
		}
	}
	
	/**
	 * Returns the cosmodog object.
	 * @return Cosmodog object.
	 */
	public Cosmodog getCosmodog() {
		return cosmodog;
	}
	
	/**
	 * Sets the cosmodog object.
	 * @param cosmodog Cosmodog object.
	 */
	public void setCosmodog(Cosmodog cosmodog) {
		this.cosmodog = cosmodog;
	}
	
	/**
	 * Returns the custom tiled map object. Take note: The custom tiled map is the initial information about the map.
	 * It cannot be modified throughout the game. All modifyable parts of the map should be stored in the CosmodogMap object.
	 * The initialization of the custom tiled map happens within the getter as a type of lazy loading.
	 * Take note: This object replicates the data in the TiledMap property (see tiledMap). The mid term goal is to get rid of the TiledMap dependency at all,
	 * and use only the CosmodogTiledMap class.
	 * @return Custom Tiled Map as the initial state of the map.
	 */
	public CustomTiledMap getCustomTiledMap() {
		if (this.customTiledMap == null) {
			try {
				this.customTiledMap = getTiledMapReader().readTiledMap();
			} catch (TiledMapIoException e) {
				Log.error("Lazy loading of the custom tiled map has failed: " + e.getLocalizedMessage(), e);
			}
		}
		return this.customTiledMap;
	}
	
	/**
	 * Returns the map of the sound resources.
	 * @return Sound resources by their IDs.
	 */
	public SoundResources getSoundResources() {
		return soundResources;
	}
	
	/**
	 * Returns the map of the animations.
	 * @return Animation resources by their IDs.
	 */
	public Animations getAnimations() {
		return animations;
	}
	
	/**
	 * Returns the map of sprite sheets.
	 * @return Sprite sheet resources by their IDs.
	 */
	public SpriteSheets getSpriteSheets() {
		return spriteSheets;
	}
	
	/*
	 * We have to initialize the context within it's own constructor.
	 * The reason is: the tiled map needs to be initialized in a open GL bound thread (f.i. in one of the states).
	 * It cannot be initialized within the main thread. On the other hand, the initialization of the context
	 * should be state independent and as early as possible.
	 */
	private void init() throws SlickException {
		
		InputHandler inGameInputHandler = new InGameInputHandler();
		InputHandler inGameControlInputHandler = new InGameControlInputHandler();
		InputHandler debugConsoleInputHandler = new DebugConsoleInputHandler();
		InputHandler inGameDialogInputHandler = new InGameDialogInputHandler();
		InputHandler inGameTextFrameInputHandler = new InGameTextFrameInputHandler();
		InputHandler noInputInputHandler = new NoInputInputHandler();
		
		Cosmodog cosmodog = new Cosmodog();
		User user = new User();
		user.setUserName("Armitage");
		cosmodog.setUser(user);
		
		CollisionValidator collisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(new GeneralCollisionValidator(), new InterCharacterCollisionValidator(null, Maps.newHashMap()), new EnergyWallCollisionValidator())); 
		
		PathFinder towardsPlayerPathFinder = new TowardsPlayerPathFinder();
		PathFinder randomPathFinder = new RandomPathFinder();
		
		PathFinder enemySightBasedDecisionPathFinder = new EnemySightBasedDecitionPathFinder(randomPathFinder, towardsPlayerPathFinder);
		
		cosmodog.setPathFinder(enemySightBasedDecisionPathFinder);
		cosmodog.setCollisionValidator(new FeatureBoundCollisionValidator(collisionValidator));
		
		cosmodog.setTravelTimeCalculator(new DefaultTravelTimeCalculator());
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME, inGameInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_CONTROL, inGameControlInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_DEBUGCONSOLE, debugConsoleInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_DIALOG, inGameDialogInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_TEXTFRAME, inGameTextFrameInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_NO_INPUT, noInputInputHandler);
		
		cosmodog.setGamePersistor(CosmodogGamePersistor.instance());
		cosmodog.setScorePersistor(CosmodogScorePersistor.instance());
		
		SightRadiusCalculator sightRadiusCalculator = new DayTimeAffectedSightRadiusCalculator(new DefaultSightRadiusCalculator());
		cosmodog.setSightRadiusCalculator(sightRadiusCalculator);
		
		
		this.setCosmodog(cosmodog);
		
		Sound collected = new Sound("data/sound/collected.wav");
		Sound eaten = new Sound("data/sound/eaten.wav");
		Sound drunk = new Sound("data/sound/drunk.wav");
		Sound carstart = new Sound("data/sound/carstart.wav");
		Sound noway = new Sound("data/sound/noway.wav");
		Sound footsteps = new Sound("data/sound/footsteps.wav");
		Sound carmotor = new Sound("data/sound/carmotor.wav");
		Sound motordies = new Sound("data/sound/motordies.wav");
		Sound hit = new Sound("data/sound/hit.wav");
		Sound explosion = new Sound("data/sound/explosion.wav");
		Sound powerup = new Sound("data/sound/powerup.wav");
		
		this.getSoundResources().put(SoundResources.SOUND_COLLECTED, collected);
		this.getSoundResources().put(SoundResources.SOUND_EATEN, eaten);
		this.getSoundResources().put(SoundResources.SOUND_DRUNK, drunk);
		this.getSoundResources().put(SoundResources.SOUND_CARSTART, carstart);
		this.getSoundResources().put(SoundResources.SOUND_NOWAY, noway);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS, footsteps);
		this.getSoundResources().put(SoundResources.SOUND_CARMOTOR, carmotor);
		this.getSoundResources().put(SoundResources.SOUND_MOTOR_DIES, motordies);
		this.getSoundResources().put(SoundResources.SOUND_HIT, hit);
		this.getSoundResources().put(SoundResources.SOUND_EXPLOSION, explosion);
		this.getSoundResources().put(SoundResources.SOUND_POWERUP, powerup);

		
		SpriteSheet playerSheet = new SpriteSheet("data/sprites.png", 16, 16);
		SpriteSheet collectibleItemToolSheet = new SpriteSheet("data/collectible_tool.png", 16, 16);
		SpriteSheet infobitsSheet = new SpriteSheet("data/infobits.png", 16, 16);
		SpriteSheet suppliesSheet = new SpriteSheet("data/supplies.png", 16, 16);
		SpriteSheet insightSheet = new SpriteSheet("data/insight.png", 16, 16);
		SpriteSheet cloudSheet = new SpriteSheet("data/cloud.png", 240, 240);
		SpriteSheet alphabethSheet = new SpriteSheet("data/alphabeth.png", 16, 24);
		SpriteSheet alphabeth2Sheet = new SpriteSheet("data/alphabeth2.png", 16, 16);
		SpriteSheet interfaceSheet = new SpriteSheet("data/interface.png", 16, 16);
		
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_PLAYER, playerSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_COLLECTIBLE_ITEM_TOOL, collectibleItemToolSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_INFOBITS, infobitsSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_INSIGHT, insightSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_SUPPLIES, suppliesSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_CLOUDS, cloudSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_ALPHABETH, alphabethSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_ALPHABETH2, alphabeth2Sheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_INTERFACE, interfaceSheet);
		
		
		AnimationBuilder builder = new AnimationBuilder();
		Map<String, GenericResourceWrapper<Animation>> animationResourceWrappers = builder.build();
		for (String key : animationResourceWrappers.keySet()) {
			this.getAnimations().put(key, animationResourceWrappers.get(key).getEntity());
		}
		
		LetterBuilder letterBuilder = new DefaultLetterBuilder(alphabeth2Sheet);
		this.characterLetters = letterBuilder.buildLetters();
		
		
		
	}

	/**
	 * Returns the dialog box drawing context. 
	 * @return Dialog box drawing context.
	 */
	public DrawingContext getDialogBoxDrawingContext() {
		return dialogBoxDrawingContext;
	}

	/**
	 * Sets the dialog box drawing context.
	 * @param dialogBoxDrawingContext Dialog box drawing context.
	 */
	public void setDialogBoxDrawingContext(DrawingContext dialogBoxDrawingContext) {
		this.dialogBoxDrawingContext = dialogBoxDrawingContext;
	}
	
	/**
	 * Returns the tiled map reader which is responsible for loading the custom tiled map.
	 * @return Custom tiled map reader.
	 */
	public TiledMapReader getTiledMapReader() {
		return tiledMapReader;
	}

	/**
	 * Returns the map of letters by characters defining a kind of system font.
	 * @return Letters by characters.
	 */
	public Map<Character, Letter> getCharacterLetters() {
		return characterLetters;
	}

}
