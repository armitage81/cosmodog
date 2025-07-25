package antonafanasjew.cosmodog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.listener.movement.consumer.*;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.*;
import antonafanasjew.cosmodog.player.DefaultPlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.validators.OneBlocksAllCollisionValidator;
import antonafanasjew.cosmodog.collision.validators.moveable.GeneralCollisionValidatorForMoveable;
import antonafanasjew.cosmodog.collision.validators.player.DynamicPieceCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.collision.validators.player.EnergyWallCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.collision.validators.player.FeatureBoundCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.collision.validators.player.GeneralCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.collision.validators.player.InterCharacterCollisionValidatorForPlayer;
import antonafanasjew.cosmodog.controller.DebugConsoleInputHandler;
import antonafanasjew.cosmodog.controller.InGameGameLogInputHandler;
import antonafanasjew.cosmodog.controller.InGameInputHandler;
import antonafanasjew.cosmodog.controller.InGameMenuInputHandler;
import antonafanasjew.cosmodog.controller.InGameTextFrameInputHandler;
import antonafanasjew.cosmodog.controller.InputHandler;
import antonafanasjew.cosmodog.controller.NoInputInputHandler;
import antonafanasjew.cosmodog.filesystem.CosmodogGamePersistor;
import antonafanasjew.cosmodog.filesystem.CosmodogScorePersistor;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.User;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.menu.Menu;
import antonafanasjew.cosmodog.model.menu.MenuAction;
import antonafanasjew.cosmodog.model.menu.MenuActionFactory;
import antonafanasjew.cosmodog.model.menu.MenuLabel;
import antonafanasjew.cosmodog.model.menu.MenuLabelFactory;
import antonafanasjew.cosmodog.pathfinding.EnemyAlertBasedDecisionPathFinder;
import antonafanasjew.cosmodog.pathfinding.EnemyTypeSpecificAlertedPathFinder;
import antonafanasjew.cosmodog.pathfinding.PathFinder;
import antonafanasjew.cosmodog.pathfinding.PatrolingPathFinder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.resourcehandling.GenericResourceWrapper;
import antonafanasjew.cosmodog.resourcehandling.builder.animations.AnimationBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.menu.MenuBuilder;
import antonafanasjew.cosmodog.resourcehandling.dyinghints.DyingHintsBuilder;
import antonafanasjew.cosmodog.resourcehandling.dyinghints.DyingHintsBuilderImpl;
import antonafanasjew.cosmodog.resourcehandling.gamelogs.GameLogBuilder;
import antonafanasjew.cosmodog.resourcehandling.gamelogs.GameLogBuilderImpl;
import antonafanasjew.cosmodog.text.DefaultLetterBuilder;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterBuilder;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapReader;
import antonafanasjew.cosmodog.tiledmap.io.XmlTiledMapReader;
import antonafanasjew.cosmodog.waterplaces.DefaultWaterValidator;

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
	
	private PlayerBuilder playerBuilder;
	
	/*
	 * Contains the game model beyond the game session. User, score list etc are also included.
	 */
	private Cosmodog cosmodog;
	
	/*
	 * Data of maps as loaded from the Tiled format.
	 * The prefix "Custom" indicates that it is custom class and not the one from the framework.
	 */
	private Map<MapType, CustomTiledMap> customTiledMaps;
	
	/*
	 * All log data (cutscenes, monolith interactions, found logs) as texts. 
	 */
	private GameLogs gameLogs = new GameLogs();
	
	/*
	 * A series of hint texts that are presented on the game over screen.
	 */
	private List<String> dyingHints = new ArrayList<>();
	
	/*
	 * Texts for Intro, Outro, Credits and References in the main menu are stored here.
	 */
	private Map<String, GameLog> gameTexts = Maps.newHashMap();
	
	/*
	 * All music data for the game.
	 */
	private MusicResources musicResources = new MusicResources();
	
	/*
	 * All sounds for the game.
	 */
	private SoundResources soundResources = new SoundResources();
	
	/*
	 * All animations and sprites for the game.
	 */
	private Animations animations = new Animations();
	
	/*
	 * Big images like the whole screen interface frame are stored here.
	 */
	private Images images = new Images();
	private SpriteSheets spriteSheets = new SpriteSheets();
	private TiledMapReader tiledMapReader = new XmlTiledMapReader();
	
	private Map<Character, Letter> characterLetters = Maps.newHashMap();
	
	private Map<String, MenuAction> menuActions = Maps.newHashMap();
	private Map<String, MenuLabel> menuLabels = Maps.newHashMap();
	
	private Map<String, Menu> menus = Maps.newHashMap();

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
	 * Returns custom tiled map objects for all map types. Take note: A custom tiled map is the initial information about the map.
	 * It cannot be modified throughout the game. All modifyable parts of the map should be stored in the CosmodogMap object.
	 * The initialization of the custom tiled map happens within the getter as a type of lazy loading.
	 * Take note: This object replicates the data in the TiledMap property (see tiledMap).
	 * The mid term goal is to get rid of the TiledMap dependency at all,
	 * and use only the CosmodogTiledMap class.
	 * @return Custom Tiled Map as the initial state of the map.
	 */
	public Map<MapType, CustomTiledMap> getCustomTiledMaps() {
		if (this.customTiledMaps == null) {
			try {
				this.customTiledMaps = Maps.newHashMap();
				for (MapType mapType : MapType.values()) {
					CustomTiledMap map = getTiledMapReader().readTiledMap(Constants.mapPathsSupplier.get().get(mapType));
					customTiledMaps.put(mapType, map);
				}
			} catch (TiledMapIoException e) {
				Log.error("Lazy loading of the custom tiled map has failed: " + e.getLocalizedMessage(), e);
			}
		}
		return this.customTiledMaps;
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
	
	public Images getImages() {
		return images;
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
		InputHandler debugConsoleInputHandler = new DebugConsoleInputHandler();
		InputHandler inGameTextFrameInputHandler = new InGameTextFrameInputHandler();
		InputHandler inGameGameLogInputHandler = new InGameGameLogInputHandler();
		InputHandler inGameMenuInputHandler = new InGameMenuInputHandler();
		InputHandler noInputInputHandler = new NoInputInputHandler();
		
		Cosmodog cosmodog = new Cosmodog();
		User user = new User();
		user.setUserName("Armitage");
		cosmodog.setUser(user);

		this.playerBuilder = CosmodogModelHolder.retrievePlayerBuilder();
		cosmodog.setPlayerBuilder(playerBuilder);
		
		List<CollisionValidator> delegateValidators = Lists.newArrayList();
		delegateValidators.add(new GeneralCollisionValidatorForPlayer());
		delegateValidators.add(new InterCharacterCollisionValidatorForPlayer());
		delegateValidators.add(new EnergyWallCollisionValidatorForPlayer());
		delegateValidators.add(new DynamicPieceCollisionValidatorForPlayer());
		CollisionValidator collisionValidatorForPlayer = new OneBlocksAllCollisionValidator(delegateValidators); 
		cosmodog.setCollisionValidatorForPlayer(new FeatureBoundCollisionValidatorForPlayer(collisionValidatorForPlayer));
		CollisionValidator collisionValidatorForMoveable = new GeneralCollisionValidatorForMoveable();
		cosmodog.setCollisionValidatorForMoveable(new FeatureBoundCollisionValidatorForPlayer(collisionValidatorForMoveable));

		
		PathFinder alertedPathFinder = new EnemyTypeSpecificAlertedPathFinder();
		PathFinder patrolingPathFinder = new PatrolingPathFinder();
		PathFinder enemyAlertBasedDecisionPathFinder = new EnemyAlertBasedDecisionPathFinder(patrolingPathFinder, alertedPathFinder);
		cosmodog.setPathFinder(enemyAlertBasedDecisionPathFinder);
		
		cosmodog.setWaterValidator(new DefaultWaterValidator());
		
		ResourceConsumer fuelConsumer = Features.getInstance().featureOn(Features.FEATURE_FUEL) ? new FuelConsumer() : new ResourceConsumer() {
			@Override
			public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
				return 0;
			}
		};
		
		cosmodog.setFuelConsumer(fuelConsumer);
		
		ResourceConsumer waterConsumer = Features.getInstance().featureOn(Features.FEATURE_THIRST) ? new WaterConsumer() : new ResourceConsumer() {
			@Override
			public int turnCosts(Position position1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
				return 0;
			}
		};

		waterConsumer = ConditionalResourceConsumer.instanceWithResourceConsumptionActiveCondition(waterConsumer);

		cosmodog.setWaterConsumer(waterConsumer);
		
		ResourceConsumer foodConsumer = Features.getInstance().featureOn(Features.FEATURE_HUNGER) ? new FoodConsumer() : new ResourceConsumer() {
			@Override
			public int turnCosts(Position positioin1, Position position2, Player player, CosmodogMap map, ApplicationContext cx) {
				return 0;
			}
		};

		foodConsumer = ConditionalResourceConsumer.instanceWithResourceConsumptionActiveCondition(foodConsumer);
		
		cosmodog.setFoodConsumer(foodConsumer);
		
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME, inGameInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_DEBUGCONSOLE, debugConsoleInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_TEXTFRAME, inGameTextFrameInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_GAMELOG, inGameGameLogInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_INGAME_MENU, inGameMenuInputHandler);
		cosmodog.getInputHandlers().put(InputHandlerType.INPUT_HANDLER_NO_INPUT, noInputInputHandler);
		
		cosmodog.setGamePersistor(CosmodogGamePersistor.instance());
		cosmodog.setScorePersistor(CosmodogScorePersistor.instance());
		
		Map<String, PieceInteraction> pieceInteractionMap = cosmodog.getPieceInteractionMap();
		
		pieceInteractionMap.put(CollectibleComposed.class.getSimpleName(), new ComposedInteraction());
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.firstaidkit.name(), new FirstAidKitInteraction());
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
		pieceInteractionMap.put(CollectibleGoodie.GoodieType.fueltank.name(), new FuelTankInteraction());
		pieceInteractionMap.put(Vehicle.class.getSimpleName(), new VehicleInteraction());
		pieceInteractionMap.put(Platform.class.getSimpleName(), new PlatformInteraction());
		pieceInteractionMap.put("pistol", new WeaponInteraction());
		pieceInteractionMap.put("shotgun", new WeaponInteraction());
		pieceInteractionMap.put("rifle", new WeaponInteraction());
		pieceInteractionMap.put("machinegun", new WeaponInteraction());
		pieceInteractionMap.put("rpg", new WeaponInteraction());
		pieceInteractionMap.put("CollectibleAmmo_pistol", new AmmoInteraction());
		pieceInteractionMap.put("CollectibleAmmo_shotgun", new AmmoInteraction());
		pieceInteractionMap.put("CollectibleAmmo_rifle", new AmmoInteraction());
		pieceInteractionMap.put("CollectibleAmmo_machinegun", new AmmoInteraction());
		pieceInteractionMap.put("CollectibleAmmo_rpg", new AmmoInteraction());
		
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blackKeyDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blueKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blueKeyDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.brownKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.cyanKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.darkblueKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.greenKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.greenKeyDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.lilaKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.purpleKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.redKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.redKeyDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.whiteKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.yellowKeycardDoor, new KeyInteraction());
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.yellowKeyDoor, new KeyInteraction());
		
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
		pieceInteractionMap.put(CollectibleTool.ToolType.archeologistsJournal.name(), new ArcheologistsJournalInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.weaponFirmwareUpgrade.name(), new WeaponFirmwareUpgradeInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.nutrients.name(), new NutrientsInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.axe.name(), new AxeInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.portalgun.name(), new PortalGunInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.nightvision.name(), new NightVisionInteraction());
		pieceInteractionMap.put(CollectibleTool.ToolType.motiontracker.name(), new MotionTrackerInteraction());
		
		this.setCosmodog(cosmodog);
		
		Features.getInstance().featureBoundProcedure(Features.FEATURE_MUSIC, new Runnable() {

			@Override
			public void run() {
				try {
					Music musicMainMenu = new Music("data/music/EG_Map_Select_01_Loop.ogg");
					Music musicGameOver = new Music("data/music/EG_Negative_Stinger.ogg");
					Music musicLogo = new Music("data/music/EG_Neutral_Stinger_01.ogg");
					Music musicCutscene = new Music("data/music/EG_DangerZone_Loop.ogg");
					Music foundTool = new Music("data/music/EG_Positive_Stinger_02.ogg");
					Music musicSoundtrack = new Music("data/music/Soundtrack.ogg");
					Music musicSoundtrackSpace = new Music("data/music/Soundtrack_space.ogg");
					
					
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_MAIN_MENU, musicMainMenu);
					
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK, musicSoundtrack);
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK_SPACE, musicSoundtrackSpace);
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_GAME_OVER, musicGameOver);
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_LOGO, musicLogo);
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_CUTSCENE, musicCutscene);
					ApplicationContext.this.getMusicResources().put(MusicResources.MUSIC_FOUND_TOOL, foundTool);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			
		});

		Sound spaceliftLatch = new Sound("data/sound/spacelift_latch.wav");
		Sound spacelift = new Sound("data/sound/spacelift.wav");
		Sound collected = new Sound("data/sound/collected.wav");
		Sound eaten = new Sound("data/sound/eaten.wav");
		Sound drunk = new Sound("data/sound/drunk.wav");
		Sound carstart = new Sound("data/sound/carstart.wav");
		Sound noway = new Sound("data/sound/noway.wav");
		Sound carmotor = new Sound("data/sound/carmotor.wav");
		Sound motordies = new Sound("data/sound/motordies.wav");
		Sound hit = new Sound("data/sound/hit.wav");
		Sound explosion = new Sound("data/sound/explosion.wav");
		Sound powerup = new Sound("data/sound/powerup.wav");
		Sound popup = new Sound("data/sound/popup.wav");
		Sound teleport_start = new Sound("data/sound/teleport_start.wav");
		Sound teleport_transfer = new Sound("data/sound/teleport_transferring.wav");
		Sound teleport_end = new Sound("data/sound/teleport_end.wav");
		Sound reload = new Sound("data/sound/reload.wav");
		Sound droppedItem = new Sound("data/sound/droppeditem.wav");
		Sound pressurePlate = new Sound("data/sound/pressureplate.wav");
		Sound letterPlate = new Sound("data/sound/letterplate.wav");
		Sound drainPoison = new Sound("data/sound/drainpoison.wav");
		Sound artilleryShots = new Sound("data/sound/artilleryshots.wav");
		Sound secretFound = new Sound("data/sound/secretfound.wav");
		Sound carMoves = new Sound("data/sound/carmoves.wav");
		Sound carDoor = new Sound("data/sound/cardoor.wav");
		Sound console = new Sound("data/sound/console.wav");
		Sound wormGrowl = new Sound("data/sound/wormgrowl.wav");
		Sound shortWormGrowl = new Sound("data/sound/shortwormgrowl.wav");
		Sound earthquake = new Sound("data/sound/earthquake.wav");
		Sound breakcrate1 = new Sound("data/sound/breakcrate1.wav");
		Sound breakcrate2 = new Sound("data/sound/breakcrate2.wav");
		Sound breakcrate3 = new Sound("data/sound/breakcrate3.wav");
		
		Sound breakstone1 = new Sound("data/sound/breakstone1.wav");
		Sound breakstone2 = new Sound("data/sound/breakstone2.wav");
		Sound breakstone3 = new Sound("data/sound/breakstone3.wav");
		Sound breakhardstone1 = new Sound("data/sound/breakhardstone1.wav");
		Sound breakhardstone2 = new Sound("data/sound/breakhardstone2.wav");
		Sound breakhardstone3 = new Sound("data/sound/breakhardstone3.wav");
		Sound cuttingtree1 = new Sound("data/sound/cuttingtree1.wav");
		Sound cuttingtree2 = new Sound("data/sound/cuttingtree2.wav");
		Sound cuttingtree3 = new Sound("data/sound/cuttingtree3.wav");
		
		Sound cuttingbamboo1 = new Sound("data/sound/cutbamboo1.wav");
		Sound cuttingbamboo2 = new Sound("data/sound/cutbamboo2.wav");
		Sound cuttingbamboo3 = new Sound("data/sound/cutbamboo3.wav");
		
		Sound lockedaliendoor = new Sound("data/sound/lockedaliendoor.wav");
		Sound openingaliendoor = new Sound("data/sound/openingaliendoor.wav");
		
		Sound poisoned = new Sound("data/sound/poisoned.wav");
		
		Sound guardianDestroyed = new Sound("data/sound/guardiandestroyed.wav");
		
		Sound menu_sub = new Sound("data/sound/menu_sub.wav");
		Sound menu_back = new Sound("data/sound/menu_back.wav");
		Sound menu_select = new Sound("data/sound/menu_select.wav");
		Sound menu_move = new Sound("data/sound/menu_move.wav");
		
		
		
		Sound footsteps = new Sound("data/sound/footsteps.wav");
		Sound footstepsHighGrass = new Sound("data/sound/footsteps_highgrass.wav");
		Sound footstepsGrass = new Sound("data/sound/footsteps_grass.wav");
		Sound footstepsSnow = new Sound("data/sound/footsteps_snow.wav");
		Sound footstepsSand = new Sound("data/sound/footsteps_sand.wav");
		Sound footstepsRoad = new Sound("data/sound/footsteps_road.wav");
		Sound footstepsWater = new Sound("data/sound/footsteps_water.wav");
		
		Sound ambientElectricity = new Sound("data/sound/ambient_electricity.wav");
		Sound ambientEnergyWall = new Sound("data/sound/ambient_energywall.wav");
		Sound ambientFire = new Sound("data/sound/ambient_fire.wav");
		
		Sound introMissileAlert = new Sound("data/intro/missilealert.wav");
		Sound introSiren = new Sound("data/intro/siren.wav");
		
		Sound alisasMessage = new Sound("data/alisasMessage.wav");
		
		Sound textTyping = new Sound("data/sound/text.wav");
		
		Sound sliding = new Sound("data/sound/slide.wav");
		
		Sound secretDoorSpikes = new Sound("data/sound/secretdoor_spikes.wav");
		Sound secretDoorHydraulics = new Sound("data/sound/secretdoor_hydraulics.wav");
		Sound secretDoorEnergy = new Sound("data/sound/secretdoor_energy.wav");
		Sound secretDoorWall = new Sound("data/sound/secretdoor_wall.wav");

		Sound portalsGunshot = new Sound("data/sound/portals_gunshot.wav");
		Sound portalsJammed = new Sound("data/sound/portals_jammed.wav");
		Sound portalsCreated = new Sound("data/sound/portals_created.wav");
		Sound portalsFailed = new Sound("data/sound/portals_failed.wav");
		Sound portalsCanceled = new Sound("data/sound/portals_canceled.wav");
		Sound portalsTeleported = new Sound("data/sound/portals_teleported.wav");
		Sound buttonPushed = new Sound("data/sound/button_pushed.wav");

		Sound sensorPresenceDetected = new Sound("data/sound/sensor_presence_detected.wav");
		Sound sensorPresenceLost = new Sound("data/sound/sensor_presence_lost.wav");
		Sound alert = new Sound("data/sound/alert.wav");


		this.getSoundResources().put(SoundResources.SOUND_SPACE_LIFT_LATCH, spaceliftLatch);
		this.getSoundResources().put(SoundResources.SOUND_SPACE_LIFT, spacelift);
		this.getSoundResources().put(SoundResources.SOUND_COLLECTED, collected);
		this.getSoundResources().put(SoundResources.SOUND_EATEN, eaten);
		this.getSoundResources().put(SoundResources.SOUND_DRUNK, drunk);
		this.getSoundResources().put(SoundResources.SOUND_CARSTART, carstart);
		this.getSoundResources().put(SoundResources.SOUND_NOWAY, noway);
		this.getSoundResources().put(SoundResources.SOUND_CARMOTOR, carmotor);
		this.getSoundResources().put(SoundResources.SOUND_MOTOR_DIES, motordies);
		this.getSoundResources().put(SoundResources.SOUND_HIT, hit);
		this.getSoundResources().put(SoundResources.SOUND_EXPLOSION, explosion);
		this.getSoundResources().put(SoundResources.SOUND_POWERUP, powerup);
		this.getSoundResources().put(SoundResources.SOUND_POPUP, popup);
		this.getSoundResources().put(SoundResources.SOUND_TELEPORT_START, teleport_start);
		this.getSoundResources().put(SoundResources.SOUND_TELEPORT_TRANSFER, teleport_transfer);
		this.getSoundResources().put(SoundResources.SOUND_TELEPORT_END, teleport_end);
		this.getSoundResources().put(SoundResources.SOUND_RELOAD, reload);
		this.getSoundResources().put(SoundResources.SOUND_DROPPED_ITEM, droppedItem);
		this.getSoundResources().put(SoundResources.SOUND_PRESSURE_PLATE, pressurePlate);
		this.getSoundResources().put(SoundResources.SOUND_LETTER_PLATE, letterPlate);
		this.getSoundResources().put(SoundResources.SOUND_DRAIN_POISON, drainPoison);
		this.getSoundResources().put(SoundResources.SOUND_ARTILLERY_SHOTS, artilleryShots);
		this.getSoundResources().put(SoundResources.SOUND_SECRET_FOUND, secretFound);
		this.getSoundResources().put(SoundResources.SOUND_CAR_MOVES, carMoves);
		this.getSoundResources().put(SoundResources.SOUND_CAR_DOOR, carDoor);
		this.getSoundResources().put(SoundResources.SOUND_CONSOLE, console);
		this.getSoundResources().put(SoundResources.SOUND_WORM_GROWL, wormGrowl);
		this.getSoundResources().put(SoundResources.SOUND_SHORT_WORM_GROWL, shortWormGrowl);
		this.getSoundResources().put(SoundResources.SOUND_EARTHQUAKE, earthquake);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_CRATE1, breakcrate1);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_CRATE2, breakcrate2);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_CRATE3, breakcrate3);
		
		this.getSoundResources().put(SoundResources.SOUND_BREAK_STONE1, breakstone1);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_STONE2, breakstone2);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_STONE3, breakstone3);
		
		this.getSoundResources().put(SoundResources.SOUND_BREAK_HARDSTONE1, breakhardstone1);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_HARDSTONE2, breakhardstone2);
		this.getSoundResources().put(SoundResources.SOUND_BREAK_HARDSTONE3, breakhardstone3);
		
		this.getSoundResources().put(SoundResources.SOUND_CUT_TREE1, cuttingtree1);
		this.getSoundResources().put(SoundResources.SOUND_CUT_TREE2, cuttingtree2);
		this.getSoundResources().put(SoundResources.SOUND_CUT_TREE3, cuttingtree3);

		this.getSoundResources().put(SoundResources.SOUND_CUT_BAMBOO1, cuttingbamboo1);
		this.getSoundResources().put(SoundResources.SOUND_CUT_BAMBOO2, cuttingbamboo2);
		this.getSoundResources().put(SoundResources.SOUND_CUT_BAMBOO3, cuttingbamboo3);
		
		this.getSoundResources().put(SoundResources.SOUND_LOCKED_ALIEN_DOOR, lockedaliendoor);
		this.getSoundResources().put(SoundResources.SOUND_OPENING_ALIEN_DOOR, openingaliendoor);
		
		this.getSoundResources().put(SoundResources.SOUND_POISONED, poisoned);
		
		this.getSoundResources().put(SoundResources.SOUND_GUARDIAN_DESTROYED, guardianDestroyed);
		
		this.getSoundResources().put(SoundResources.SOUND_MENU_SUB, menu_sub);
		this.getSoundResources().put(SoundResources.SOUND_MENU_BACK, menu_back);
		this.getSoundResources().put(SoundResources.SOUND_MENU_SELECT, menu_select);
		this.getSoundResources().put(SoundResources.SOUND_MENU_MOVE, menu_move);
		
		
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS, footsteps);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS_HIGH_GRASS, footstepsHighGrass);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS_GRASS, footstepsGrass);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS_ROAD, footstepsRoad);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS_SAND, footstepsSand);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS_SNOW, footstepsSnow);
		this.getSoundResources().put(SoundResources.SOUND_FOOTSTEPS_WATER, footstepsWater);
		
		this.getSoundResources().put(SoundResources.SOUND_AMBIENT_ELECTRICITY, ambientElectricity);
		this.getSoundResources().put(SoundResources.SOUND_AMBIENT_ENERGYWALL, ambientEnergyWall);
		this.getSoundResources().put(SoundResources.SOUND_AMBIENT_FIRE, ambientFire);
		
		this.getSoundResources().put(SoundResources.SOUND_INTRO_MISSILE_ALERT, introMissileAlert);
		this.getSoundResources().put(SoundResources.SOUND_INTRO_SIREN, introSiren);
		this.getSoundResources().put(SoundResources.SOUND_CUTSCENE_ALISASMESSAGE, alisasMessage);
		
		this.getSoundResources().put(SoundResources.SOUND_TEXT_TYPING, textTyping);
		this.getSoundResources().put(SoundResources.SOUND_SLIDING, sliding);
		this.getSoundResources().put(SoundResources.SOUND_SECRET_DOOR_SPIKES, secretDoorSpikes);
		this.getSoundResources().put(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS, secretDoorHydraulics);
		this.getSoundResources().put(SoundResources.SOUND_SECRET_DOOR_ENERGY, secretDoorEnergy);
		this.getSoundResources().put(SoundResources.SOUND_SECRET_DOOR_WALL, secretDoorWall);

		this.getSoundResources().put(SoundResources.SOUND_PORTALS_GUNSHOT, portalsGunshot);
		this.getSoundResources().put(SoundResources.SOUND_PORTALS_JAMMED, portalsJammed);
		this.getSoundResources().put(SoundResources.SOUND_PORTALS_CREATED, portalsCreated);
		this.getSoundResources().put(SoundResources.SOUND_PORTALS_FAILED, portalsFailed);
		this.getSoundResources().put(SoundResources.SOUND_PORTALS_CANCELED, portalsCanceled);
		this.getSoundResources().put(SoundResources.SOUND_PORTALS_TELEPORTED, portalsTeleported);
		this.getSoundResources().put(SoundResources.SOUND_BUTTON_PUSHED, buttonPushed);
		this.getSoundResources().put(SoundResources.SOUND_SENSOR_PRESENCE_DETECTED, sensorPresenceDetected);
		this.getSoundResources().put(SoundResources.SOUND_SENSOR_PRESENCE_LOST, sensorPresenceLost);
		this.getSoundResources().put(SoundResources.SOUND_ALERT, alert);
		
		
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
		SpriteSheet tileset1Sheet = new SpriteSheet("data/tileset00000.png", 16, 16);
		SpriteSheet tileset2Sheet = new SpriteSheet("data/tileset00001.png", 16, 16);
		SpriteSheet symbolsSheet = new SpriteSheet("data/symbols.png", 16, 16);
		
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
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_TILES1, tileset1Sheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_TILES2, tileset2Sheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_SYMBOLS, symbolsSheet);
		
		
		AnimationBuilder builder = new AnimationBuilder();
		Map<String, GenericResourceWrapper<Animation>> animationResourceWrappers = builder.build();
		for (String key : animationResourceWrappers.keySet()) {
			this.getAnimations().put(key, animationResourceWrappers.get(key).getEntity());
		}

		images.put("space.background", new Image("data/space_background.png", false, Image.FILTER_NEAREST));

		images.put("ui.ingame.background", new Image("data/ui/background.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.lifeframe", new Image("data/ui/lifeframe2.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.barbackground", new Image("data/ui/barbackground.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.barframestart", new Image("data/ui/barframestart.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.barframemiddle", new Image("data/ui/barframemiddle.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.barframeend", new Image("data/ui/barframeend.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.gamelogframe", new Image("data/ui2/gamelogfield.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.popupframe", new Image("data/ui2/textfield.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.frame", new Image("data/ui2/ui2.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingamemenuframe", new Image("data/ui/ingamemenuinterface.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingameinventory", new Image("data/ui/ingameinventory.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingamemap", new Image("data/ui/ingamemap.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingamelogs", new Image("data/ui/ingamelogs.png", false, Image.FILTER_NEAREST));
		
		images.put("ui.ingame.compasspointer", new Image("data/ui/compasspointer.png", false, Image.FILTER_NEAREST));
		
		images.put("ui.ingame.weaponboxsimple", new Image("data/ui/weaponboxsimple.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.weaponboxdouble", new Image("data/ui/weaponboxdouble.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.weaponboxtriple", new Image("data/ui/weaponboxtriple.png", false, Image.FILTER_NEAREST));
		
		images.put("ui.ingame.ingameinventoryitembox", new Image("data/ui/ingameinventoryitembox.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingameinventoryitemboxselected", new Image("data/ui/ingameinventoryitemboxselected.png", false, Image.FILTER_NEAREST));

		//This big image is not loaded eagerly when initializing animations in the application context.
		//That causes a delay when opening map for the first time. Initializing the map image explicitly to
		//avoid this.
//		Image mapImage = getAnimations().get("completechart").getImage(0);
//		mapImage.draw();
		
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
			this.gameTexts.put("intro", gameLogBuilder.buildGameLog("data/writing/intro/intro"));
			this.gameTexts.put("outro", gameLogBuilder.buildGameLog("data/writing/outro/outro"));
			this.gameTexts.put("credits", gameLogBuilder.buildGameLog("data/writing/credits/credits"));
			this.gameTexts.put("references", gameLogBuilder.buildGameLog("data/writing/references/references"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load game logs.", e);
		}
		
		DyingHintsBuilder dyingHintsBuilder = new DyingHintsBuilderImpl();
		
		try {
			this.dyingHints.addAll(dyingHintsBuilder.build("data/writing/dyinghints/dyinghints"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load dying hints.", e);
		}
		
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

	public List<String> getDyingHints() {
		return dyingHints;
	}
	
	public Map<String, GameLog> getGameTexts() {
		return gameTexts;
	}

	public MusicResources getMusicResources() {
		return musicResources;
	}
	
}
