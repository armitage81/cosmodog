package antonafanasjew.cosmodog;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.DefaultWaterValidator;
import antonafanasjew.cosmodog.collision.DynamicPieceCollisionValidator;
import antonafanasjew.cosmodog.collision.EnergyWallCollisionValidator;
import antonafanasjew.cosmodog.collision.FeatureBoundCollisionValidator;
import antonafanasjew.cosmodog.collision.GeneralCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.collision.InterCharacterCollisionValidator;
import antonafanasjew.cosmodog.collision.OneBlocksAllCollisionValidator;
import antonafanasjew.cosmodog.controller.DebugConsoleInputHandler;
import antonafanasjew.cosmodog.controller.InGameControlInputHandler;
import antonafanasjew.cosmodog.controller.InGameDialogInputHandler;
import antonafanasjew.cosmodog.controller.InGameGameLogInputHandler;
import antonafanasjew.cosmodog.controller.InGameInputHandler;
import antonafanasjew.cosmodog.controller.InGameMenuInputHandler;
import antonafanasjew.cosmodog.controller.InGameTextFrameInputHandler;
import antonafanasjew.cosmodog.controller.InputHandler;
import antonafanasjew.cosmodog.controller.NoInputInputHandler;
import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogScorePersistor;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.AmmoInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.AntidoteInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.ArmorInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.AxeInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.BinocularsInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.BoatInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.BottleInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.ChartInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.CognitionInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.ComposedInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.DynamiteInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.FoodCompartmentInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.GeigerZaehlerInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.InfobankInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.InfobitInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.InfobyteInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.InsightInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.JacketInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.KeyInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.LogInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.MacheteInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.MedipackInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.MineDetectorInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PickInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PieceInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.PlatformInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SkiInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SoftwareInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SoulEssenceInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SuppliesInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.SupplyTrackerInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.VehicleInteraction;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.WeaponInteraction;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.User;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuAction;
import antonafanasjew.cosmodog.model.menu.MenuActionFactory;
import antonafanasjew.cosmodog.model.menu.MenuLabel;
import antonafanasjew.cosmodog.model.menu.MenuLabelFactory;
import antonafanasjew.cosmodog.pathfinding.DefaultTravelTimeCalculator;
import antonafanasjew.cosmodog.pathfinding.EnemyAlertBasedDecisionPathFinder;
import antonafanasjew.cosmodog.pathfinding.EnemyTypeSpecificAlertedPathFinder;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.pathfinding.PatrolingPathFinder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.resourcehandling.GenericResourceWrapper;
import antonafanasjew.cosmodog.resourcehandling.builder.animations.AnimationBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.menu.MenuBuilder;
import antonafanasjew.cosmodog.resourcehandling.gamelogs.GameLogBuilder;
import antonafanasjew.cosmodog.resourcehandling.gamelogs.GameLogBuilderImpl;
import antonafanasjew.cosmodog.sight.GeneralSightModifier;
import antonafanasjew.cosmodog.sight.SightModifier;
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
	private GameLogs gameLogs = new GameLogs();
	private SoundResources soundResources = new SoundResources();
	private Animations animations = new Animations();
	private SpriteSheets spriteSheets = new SpriteSheets();
	private TiledMapReader tiledMapReader = new XmlTiledMapReader(Constants.PATH_TO_TILED_MAP);
	
	private Map<Character, Letter> characterLetters = Maps.newHashMap();
	
	private Map<String, MenuAction> menuActions = Maps.newHashMap();
	private Map<String, MenuLabel> menuLabels = Maps.newHashMap();
	
	private Map<String, Menu> menus = Maps.newHashMap();
	
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
		InputHandler inGameGameLogInputHandler = new InGameGameLogInputHandler();
		InputHandler inGameMenuInputHandler = new InGameMenuInputHandler();
		InputHandler noInputInputHandler = new NoInputInputHandler();
		
		Cosmodog cosmodog = new Cosmodog();
		User user = new User();
		user.setUserName("Armitage");
		cosmodog.setUser(user);
		
		CollisionValidator collisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(new GeneralCollisionValidatorForPlayer(), new InterCharacterCollisionValidator(null, Maps.newHashMap()), new EnergyWallCollisionValidator(), new DynamicPieceCollisionValidator())); 
		
		PathFinder alertedPathFinder = new EnemyTypeSpecificAlertedPathFinder();
		PathFinder patrolingPathFinder = new PatrolingPathFinder();
		
		PathFinder enemyAlertBasedDecisionPathFinder = new EnemyAlertBasedDecisionPathFinder(patrolingPathFinder, alertedPathFinder);
		
		cosmodog.setPathFinder(enemyAlertBasedDecisionPathFinder);
		cosmodog.setCollisionValidator(new FeatureBoundCollisionValidator(collisionValidator));
		cosmodog.setWaterValidator(new DefaultWaterValidator());
		
		cosmodog.setTravelTimeCalculator(new DefaultTravelTimeCalculator());
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME, inGameInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_CONTROL, inGameControlInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_DEBUGCONSOLE, debugConsoleInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_DIALOG, inGameDialogInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_TEXTFRAME, inGameTextFrameInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG, inGameGameLogInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_MENU, inGameMenuInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_NO_INPUT, noInputInputHandler);
		
		cosmodog.setGamePersistor(CosmodogGamePersistor.instance());
		cosmodog.setScorePersistor(CosmodogScorePersistor.instance());
		
		Map<String, PieceInteraction> pieceInteractionMap = cosmodog.getPieceInteractionMap();
		
		pieceInteractionMap.put(CollectibleComposed.class.getSimpleName(), new ComposedInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.medipack.name(), new MedipackInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.armor.name(), new ArmorInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.soulessence.name(), new SoulEssenceInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.supplies.name(), new SuppliesInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.insight.name(), new InsightInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.cognition.name(), new CognitionInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.software.name(), new SoftwareInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.chart.name(), new ChartInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.infobit.name(), new InfobitInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.infobyte.name(), new InfobyteInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.infobank.name(), new InfobankInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.bottle.name(), new BottleInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.foodcompartment.name(), new FoodCompartmentInteraction());
		pieceInteractionMap.put(Vehicle.class.getSimpleName(), new VehicleInteraction());
		pieceInteractionMap.put(Platform.class.getSimpleName(), new PlatformInteraction());
		pieceInteractionMap.put(CollectibleWeapon.class.getSimpleName(), new WeaponInteraction());
		pieceInteractionMap.put(CollectibleAmmo.class.getSimpleName(), new AmmoInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName(), new KeyInteraction());
		pieceInteractionMap.put(CollectibleLog.class.getSimpleName(), new LogInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.boat.name(), new BoatInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.dynamite.name(), new DynamiteInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.geigerzaehler.name(), new GeigerZaehlerInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.supplytracker.name(), new SupplyTrackerInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.binoculars.name(), new BinocularsInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.jacket.name(), new JacketInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.antidote.name(), new AntidoteInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.minedetector.name(), new MineDetectorInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.ski.name(), new SkiInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.pick.name(), new PickInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.machete.name(), new MacheteInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.axe.name(), new AxeInteraction());
		
		
		SightModifier sightModifier = new GeneralSightModifier();
		cosmodog.setSightModifier(sightModifier);
		
		
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
		Sound teleport_start = new Sound("data/sound/teleport_start.wav");
		Sound teleport_transfer = new Sound("data/sound/teleport_transferring.wav");
		Sound teleport_end = new Sound("data/sound/teleport_end.wav");
		Sound reload = new Sound("data/sound/reload.wav");
		Sound droppedItem = new Sound("data/sound/droppeditem.wav");
		Sound pressurePlate = new Sound("data/sound/pressureplate.wav");
		Sound drainPoison = new Sound("data/sound/drainpoison.wav");
		Sound artilleryShots = new Sound("data/sound/artilleryshots.wav");
		Sound secretFound = new Sound("data/sound/secretfound.wav");
		Sound carMoves = new Sound("data/sound/carmoves.wav");
		Sound carDoor = new Sound("data/sound/cardoor.wav");
		Sound console = new Sound("data/sound/console.wav");
		Sound wormGrowl = new Sound("data/sound/wormgrowl.wav");
		Sound earthquake = new Sound("data/sound/earthquake.wav");
		
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
		this.getSoundResources().put(SoundResources.SOUND_TELEPORT_START, teleport_start);
		this.getSoundResources().put(SoundResources.SOUND_TELEPORT_TRANSFER, teleport_transfer);
		this.getSoundResources().put(SoundResources.SOUND_TELEPORT_END, teleport_end);
		this.getSoundResources().put(SoundResources.SOUND_RELOAD, reload);
		this.getSoundResources().put(SoundResources.SOUND_DROPPED_ITEM, droppedItem);
		this.getSoundResources().put(SoundResources.SOUND_PRESSURE_PLATE, pressurePlate);
		this.getSoundResources().put(SoundResources.SOUND_DRAIN_POISON, drainPoison);
		this.getSoundResources().put(SoundResources.SOUND_ARTILLERY_SHOTS, artilleryShots);
		this.getSoundResources().put(SoundResources.SOUND_SECRET_FOUND, secretFound);
		this.getSoundResources().put(SoundResources.SOUND_CAR_MOVES, carMoves);
		this.getSoundResources().put(SoundResources.SOUND_CAR_DOOR, carDoor);
		this.getSoundResources().put(SoundResources.SOUND_CONSOLE, console);
		this.getSoundResources().put(SoundResources.SOUND_WORM_GROWL, wormGrowl);
		this.getSoundResources().put(SoundResources.SOUND_EARTHQUAKE, earthquake);

		
		SpriteSheet playerSheet = new SpriteSheet("data/sprites.png", 16, 16);
		SpriteSheet collectibleItemToolSheet = new SpriteSheet("data/collectible_tool.png", 16, 16);
		SpriteSheet infobitsSheet = new SpriteSheet("data/infobits.png", 16, 16);
		SpriteSheet suppliesSheet = new SpriteSheet("data/supplies.png", 16, 16);
		SpriteSheet insightSheet = new SpriteSheet("data/insight.png", 16, 16);
		SpriteSheet softwareSheet = new SpriteSheet("data/software.png", 16, 16);
		SpriteSheet cloudSheet = new SpriteSheet("data/cloud.png", 240, 240);
		SpriteSheet alphabethSheet = new SpriteSheet("data/alphabeth.png", 16, 24);
		SpriteSheet alphabeth2Sheet = new SpriteSheet("data/alphabeth2.png", 16, 16);
		SpriteSheet interfaceSheet = new SpriteSheet("data/interface.png", 16, 16);
		SpriteSheet tilesetSheet = new SpriteSheet("data/tiles.png", 16, 16);
		
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_PLAYER, playerSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_COLLECTIBLE_ITEM_TOOL, collectibleItemToolSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_INFOBITS, infobitsSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_INSIGHT, insightSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_SOFTWARE, softwareSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_SUPPLIES, suppliesSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_CLOUDS, cloudSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_ALPHABETH, alphabethSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_ALPHABETH2, alphabeth2Sheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_INTERFACE, interfaceSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_TILES, tilesetSheet);
		
		
		AnimationBuilder builder = new AnimationBuilder();
		Map<String, GenericResourceWrapper<Animation>> animationResourceWrappers = builder.build();
		for (String key : animationResourceWrappers.keySet()) {
			this.getAnimations().put(key, animationResourceWrappers.get(key).getEntity());
		}
		
		//This big image is not loaded eagerly when initializing animations in the application context.
		//That causes a delay when opening map for the first time. Initializing the map image explicitly to
		//avoid this.
		Image mapImage = getAnimations().get("completechart").getImage(0);
		mapImage.draw();
		
		LetterBuilder letterBuilder = new DefaultLetterBuilder(alphabeth2Sheet);
		this.characterLetters = letterBuilder.buildLetters();
		
		menuActions.put("newGameMenuAction1", MenuActionFactory.getStartNewGameMenuAction(1));
		menuActions.put("newGameMenuAction2", MenuActionFactory.getStartNewGameMenuAction(2));
		menuActions.put("newGameMenuAction3", MenuActionFactory.getStartNewGameMenuAction(3));
		menuActions.put("newGameMenuAction4", MenuActionFactory.getStartNewGameMenuAction(4));
		menuActions.put("newGameMenuAction5", MenuActionFactory.getStartNewGameMenuAction(5));
		
		menuActions.put("loadGameMenuAction1", MenuActionFactory.getLoadSavedGameMenuAction(1));
		menuActions.put("loadGameMenuAction2", MenuActionFactory.getLoadSavedGameMenuAction(2));
		menuActions.put("loadGameMenuAction3", MenuActionFactory.getLoadSavedGameMenuAction(3));
		menuActions.put("loadGameMenuAction4", MenuActionFactory.getLoadSavedGameMenuAction(4));
		menuActions.put("loadGameMenuAction5", MenuActionFactory.getLoadSavedGameMenuAction(5));
		menuActions.put("showRecordsGameMenuAction", MenuActionFactory.getShowRecordsMenuAction());
		menuActions.put("quitGameMenuAction", MenuActionFactory.getQuitGameMenuAction());
		
		menuLabels.put("startNewGame", MenuLabelFactory.fromText("Start a new game"));
		
		menuLabels.put("newGame1", MenuLabelFactory.newGameLabel(1));
		menuLabels.put("newGame2", MenuLabelFactory.newGameLabel(2));
		menuLabels.put("newGame3", MenuLabelFactory.newGameLabel(3));
		menuLabels.put("newGame4", MenuLabelFactory.newGameLabel(4));
		menuLabels.put("newGame5", MenuLabelFactory.newGameLabel(5));
		
		menuLabels.put("loadSavedGame", MenuLabelFactory.fromText("Load a saved game"));
		menuLabels.put("loadGame1", MenuLabelFactory.loadGameLabel(1));
		menuLabels.put("loadGame2", MenuLabelFactory.loadGameLabel(2));
		menuLabels.put("loadGame3", MenuLabelFactory.loadGameLabel(3));
		menuLabels.put("loadGame4", MenuLabelFactory.loadGameLabel(4));
		menuLabels.put("loadGame5", MenuLabelFactory.loadGameLabel(5));
		menuLabels.put("showRecords", MenuLabelFactory.fromText("Show Records"));
		menuLabels.put("quit", MenuLabelFactory.fromText("Quit"));
		
		MenuBuilder b = new MenuBuilder(menuActions, menuLabels);
		b.build();
		List<Menu> menuList = b.getTopMenus();
		for (Menu menu : menuList) {
			menus.put(menu.getId(), menu);
		}
		
		GameLogBuilder gameLogBuilder = new GameLogBuilderImpl();
		try {
			this.gameLogs = gameLogBuilder.buildGameLogs("data/writing/gamelogs");
		} catch (IOException e) {
			throw new RuntimeException("Could not load game logs.", e);
		}
		
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

	public Map<String, Menu> getMenus() {
		return menus;
	}

	public GameLogs getGameLogs() {
		return gameLogs;
	}
	
}
