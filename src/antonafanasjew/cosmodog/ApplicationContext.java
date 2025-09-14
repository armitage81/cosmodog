package antonafanasjew.cosmodog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.listener.movement.consumer.*;
import antonafanasjew.cosmodog.listener.movement.pieceinteraction.*;
import antonafanasjew.cosmodog.model.*;
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
import antonafanasjew.cosmodog.pathfinding.EnemyAlertBasedDecisionMovementPlaner;
import antonafanasjew.cosmodog.pathfinding.EnemyTypeSpecificAlertedMovementPlaner;
import antonafanasjew.cosmodog.pathfinding.MovementPlaner;
import antonafanasjew.cosmodog.pathfinding.RoamingMovementPlaner;
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
		cosmodog.setCollisionValidatorForPlayer(collisionValidatorForPlayer);
		CollisionValidator collisionValidatorForMoveable = new GeneralCollisionValidatorForMoveable();
		cosmodog.setCollisionValidatorForMoveable(collisionValidatorForMoveable);

		
		MovementPlaner alertedMovementPlaner = new EnemyTypeSpecificAlertedMovementPlaner();
		MovementPlaner patrolingMovementPlaner = new RoamingMovementPlaner();
		MovementPlaner enemyAlertBasedDecisionMovementPlaner = new EnemyAlertBasedDecisionMovementPlaner(patrolingMovementPlaner, alertedMovementPlaner);
		cosmodog.setMovementPlaner(enemyAlertBasedDecisionMovementPlaner);
		
		cosmodog.setWaterValidator(new DefaultWaterValidator());
		
		ResourceConsumer fuelConsumer = new FuelConsumer();
		
		cosmodog.setFuelConsumer(fuelConsumer);
		
		ResourceConsumer waterConsumer = new WaterConsumer();

		waterConsumer = ConditionalResourceConsumer.instanceWithResourceConsumptionActiveCondition(waterConsumer);

		cosmodog.setWaterConsumer(waterConsumer);
		
		ResourceConsumer foodConsumer = new FoodConsumer();

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
		pieceInteractionMap.put(CollectibleKey.class.getSimpleName() + "_" + DoorType.blackKeycardDoor, new KeyInteraction());
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
		
		try {
			Music musicMainMenu = new Music("assets/audio/music/EG_Map_Select_01_Loop.ogg");
			Music musicLost = new Music("assets/audio/music/EG_Negative_Stinger.ogg");
			Music musicLogo = new Music("assets/audio/music/EG_Neutral_Stinger_01.ogg");
			Music musicCutscene = new Music("assets/audio/music/EG_DangerZone_Loop.ogg");
			Music musicWon = new Music("assets/audio/music/EG_Positive_Stinger_02.ogg");
			Music musicSoundtrack = new Music("assets/audio/music/Soundtrack.ogg");
			Music musicSoundtrackSpace = new Music("assets/audio/music/Soundtrack_space.ogg");
			Music musicSoundtrackRacing = new Music("assets/audio/music/Soundtrack_racing.ogg");
			Music musicSoundtrackTension = new Music("assets/audio/music/Soundtrack_tension.ogg");
			Music musicSoundtrackMystery = new Music("assets/audio/music/Soundtrack_mystery.ogg");


			getMusicResources().put(MusicResources.MUSIC_MAIN_MENU, musicMainMenu);

			getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK, musicSoundtrack);
			getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK_SPACE, musicSoundtrackSpace);

			getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK_RACING, musicSoundtrackRacing);
			getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK_TENSION, musicSoundtrackTension);
			getMusicResources().put(MusicResources.MUSIC_SOUNDTRACK_MYSTERY, musicSoundtrackMystery);

			getMusicResources().put(MusicResources.MUSIC_LOST, musicLost);
			getMusicResources().put(MusicResources.MUSIC_LOGO, musicLogo);
			getMusicResources().put(MusicResources.MUSIC_CUTSCENE, musicCutscene);
			getMusicResources().put(MusicResources.MUSIC_WON, musicWon);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}


		Sound spaceliftLatch = new Sound("assets/audio/sounds/spacelift_latch.wav");
		Sound spacelift = new Sound("assets/audio/sounds/spacelift.wav");
		Sound collected = new Sound("assets/audio/sounds/collected.wav");
		Sound eaten = new Sound("assets/audio/sounds/eaten.wav");
		Sound drunk = new Sound("assets/audio/sounds/drunk.wav");
		Sound carstart = new Sound("assets/audio/sounds/carstart.wav");
		Sound noway = new Sound("assets/audio/sounds/noway.wav");
		Sound carmotor = new Sound("assets/audio/sounds/carmotor.wav");
		Sound motordies = new Sound("assets/audio/sounds/motordies.wav");
		Sound hit = new Sound("assets/audio/sounds/hit.wav");

		Sound shotHandgun = new Sound("assets/audio/sounds/handgun.wav");
		Sound shotShotgun = new Sound("assets/audio/sounds/shotgun.wav");
		Sound shotRifle = new Sound("assets/audio/sounds/rifle.wav");
		Sound shotMachinegun = new Sound("assets/audio/sounds/machinegun.wav");
		Sound shotRpg = new Sound("assets/audio/sounds/rpg.wav");


		Sound explosion = new Sound("assets/audio/sounds/explosion.wav");
		Sound powerup = new Sound("assets/audio/sounds/powerup.wav");
		Sound popup = new Sound("assets/audio/sounds/popup.wav");
		Sound teleport_start = new Sound("assets/audio/sounds/teleport_start.wav");
		Sound teleport_transfer = new Sound("assets/audio/sounds/teleport_transferring.wav");
		Sound teleport_end = new Sound("assets/audio/sounds/teleport_end.wav");
		Sound reload = new Sound("assets/audio/sounds/reload.wav");
		Sound droppedItem = new Sound("assets/audio/sounds/droppeditem.wav");
		Sound pressurePlate = new Sound("assets/audio/sounds/pressureplate.wav");
		Sound letterPlate = new Sound("assets/audio/sounds/letterplate.wav");
		Sound drainPoison = new Sound("assets/audio/sounds/drainpoison.wav");
		Sound artilleryShots = new Sound("assets/audio/sounds/artilleryshots.wav");
		Sound secretFound = new Sound("assets/audio/sounds/secretfound.wav");
		Sound carMoves = new Sound("assets/audio/sounds/carmoves.wav");
		Sound carDoor = new Sound("assets/audio/sounds/cardoor.wav");
		Sound console = new Sound("assets/audio/sounds/console.wav");
		Sound wormGrowl = new Sound("assets/audio/sounds/wormgrowl.wav");
		Sound shortWormGrowl = new Sound("assets/audio/sounds/shortwormgrowl.wav");
		Sound earthquake = new Sound("assets/audio/sounds/earthquake.wav");
		Sound breakcrate1 = new Sound("assets/audio/sounds/breakcrate1.wav");
		Sound breakcrate2 = new Sound("assets/audio/sounds/breakcrate2.wav");
		Sound breakcrate3 = new Sound("assets/audio/sounds/breakcrate3.wav");
		
		Sound breakstone1 = new Sound("assets/audio/sounds/breakstone1.wav");
		Sound breakstone2 = new Sound("assets/audio/sounds/breakstone2.wav");
		Sound breakstone3 = new Sound("assets/audio/sounds/breakstone3.wav");
		Sound breakhardstone1 = new Sound("assets/audio/sounds/breakhardstone1.wav");
		Sound breakhardstone2 = new Sound("assets/audio/sounds/breakhardstone2.wav");
		Sound breakhardstone3 = new Sound("assets/audio/sounds/breakhardstone3.wav");
		Sound cuttingtree1 = new Sound("assets/audio/sounds/cuttingtree1.wav");
		Sound cuttingtree2 = new Sound("assets/audio/sounds/cuttingtree2.wav");
		Sound cuttingtree3 = new Sound("assets/audio/sounds/cuttingtree3.wav");
		
		Sound cuttingbamboo1 = new Sound("assets/audio/sounds/cutbamboo1.wav");
		Sound cuttingbamboo2 = new Sound("assets/audio/sounds/cutbamboo2.wav");
		Sound cuttingbamboo3 = new Sound("assets/audio/sounds/cutbamboo3.wav");
		
		Sound lockedaliendoor = new Sound("assets/audio/sounds/lockedaliendoor.wav");
		Sound openingaliendoor = new Sound("assets/audio/sounds/openingaliendoor.wav");
		
		Sound poisoned = new Sound("assets/audio/sounds/poisoned.wav");
		
		Sound guardianDestroyed = new Sound("assets/audio/sounds/guardiandestroyed.wav");
		
		Sound menu_sub = new Sound("assets/audio/sounds/menu_sub.wav");
		Sound menu_back = new Sound("assets/audio/sounds/menu_back.wav");
		Sound menu_select = new Sound("assets/audio/sounds/menu_select.wav");
		Sound menu_move = new Sound("assets/audio/sounds/menu_move.wav");
		
		
		
		Sound footsteps = new Sound("assets/audio/sounds/footsteps.wav");
		Sound footstepsHighGrass = new Sound("assets/audio/sounds/footsteps_highgrass.wav");
		Sound footstepsGrass = new Sound("assets/audio/sounds/footsteps_grass.wav");
		Sound footstepsSnow = new Sound("assets/audio/sounds/footsteps_snow.wav");
		Sound footstepsSand = new Sound("assets/audio/sounds/footsteps_sand.wav");
		Sound footstepsRoad = new Sound("assets/audio/sounds/footsteps_road.wav");
		Sound footstepsWater = new Sound("assets/audio/sounds/footsteps_water.wav");
		
		Sound ambientElectricity = new Sound("assets/audio/sounds/ambient_electricity.wav");
		Sound ambientEnergyWall = new Sound("assets/audio/sounds/ambient_energywall.wav");
		Sound ambientFire = new Sound("assets/audio/sounds/ambient_fire.wav");
		
		Sound introMissileAlert = new Sound("assets/audio/sounds/intro/missilealert.wav");
		Sound introSiren = new Sound("assets/audio/sounds/intro/siren.wav");
		
		Sound alisasMessage = new Sound("assets/audio/sounds/alisasMessage.wav");
		
		Sound textTyping = new Sound("assets/audio/sounds/text.wav");
		
		Sound sliding = new Sound("assets/audio/sounds/slide.wav");
		
		Sound secretDoorSpikes = new Sound("assets/audio/sounds/secretdoor_spikes.wav");
		Sound secretDoorHydraulics = new Sound("assets/audio/sounds/secretdoor_hydraulics.wav");
		Sound secretDoorEnergy = new Sound("assets/audio/sounds/secretdoor_energy.wav");
		Sound secretDoorWall = new Sound("assets/audio/sounds/secretdoor_wall.wav");

		Sound portalsGunshot = new Sound("assets/audio/sounds/portals_gunshot.wav");
		Sound portalsJammed = new Sound("assets/audio/sounds/portals_jammed.wav");
		Sound portalsCreated = new Sound("assets/audio/sounds/portals_created.wav");
		Sound portalsFailed = new Sound("assets/audio/sounds/portals_failed.wav");
		Sound portalsCanceled = new Sound("assets/audio/sounds/portals_canceled.wav");
		Sound portalsTeleported = new Sound("assets/audio/sounds/portals_teleported.wav");
		Sound buttonPushed = new Sound("assets/audio/sounds/button_pushed.wav");

		Sound sensorPresenceDetected = new Sound("assets/audio/sounds/sensor_presence_detected.wav");
		Sound sensorPresenceLost = new Sound("assets/audio/sounds/sensor_presence_lost.wav");
		Sound alert = new Sound("assets/audio/sounds/alert.wav");

		Sound timeBonus = new Sound("assets/audio/sounds/timebonus.wav");
		Sound trafficbarrierreset = new Sound("assets/audio/sounds/trafficbarrierreset.wav");


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

		this.getSoundResources().put(SoundResources.SOUND_SHOT_HANDGUN, shotHandgun);
		this.getSoundResources().put(SoundResources.SOUND_SHOT_SHOTGUN, shotShotgun);
		this.getSoundResources().put(SoundResources.SOUND_SHOT_RIFLE, shotRifle);
		this.getSoundResources().put(SoundResources.SOUND_SHOT_MACHINEGUN, shotMachinegun);
		this.getSoundResources().put(SoundResources.SOUND_SHOT_RPG, shotRpg);

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

		this.getSoundResources().put(SoundResources.SOUND_TIMEBONUS, timeBonus);
		this.getSoundResources().put(SoundResources.SOUND_TRAFFICBARRIERRESET, trafficbarrierreset);

		SpriteSheet cloudSheet = new SpriteSheet("assets/graphics/decorations/cloud.png", 240, 240);
		SpriteSheet tileset1Sheet = new SpriteSheet("assets/graphics/tiles/tileset00000.png", 16, 16);
		SpriteSheet tileset2Sheet = new SpriteSheet("assets/graphics/tiles/tileset00001.png", 16, 16);
		SpriteSheet symbolsSheet = new SpriteSheet("assets/graphics/ui/symbols.png", 16, 16);
		SpriteSheet alphabethSheet = new SpriteSheet("assets/graphics/ui/alphabeth.png", 16, 16);
		
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_CLOUDS, cloudSheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_TILES1, tileset1Sheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_TILES2, tileset2Sheet);
		this.getSpriteSheets().put(SpriteSheets.SPRITESHEET_SYMBOLS, symbolsSheet);
		
		
		AnimationBuilder builder = new AnimationBuilder();
		Map<String, GenericResourceWrapper<Animation>> animationResourceWrappers = builder.build();
		for (String key : animationResourceWrappers.keySet()) {
			this.getAnimations().put(key, animationResourceWrappers.get(key).getEntity());
		}

		images.put("space.background", new Image("assets/graphics/decorations/spacebackground.png", false, Image.FILTER_NEAREST));

		images.put("ui.ingame.gamelogframe", new Image("assets/graphics/ui/ui.log.frame.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.popupframe", new Image("assets/graphics/ui/ui.popup.frame.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.frame", new Image("assets/graphics/ui/ui.frame.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingamemenuframe", new Image("assets/graphics/ui/ui.menu.frame.png", false, Image.FILTER_NEAREST));

		images.put("ui.ingame.ingameinventory", new Image("assets/graphics/ui/ui.inventory.frame.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingameinventoryitembox", new Image("assets/graphics/ui/ui.inventory.slot.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingameinventoryitemboxselected", new Image("assets/graphics/ui/ui.inventory.slot.selected.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingamemap", new Image("assets/graphics/ui/ui.map.frame.png", false, Image.FILTER_NEAREST));
		images.put("ui.ingame.ingamelogs", new Image("assets/graphics/ui/ui.logplayer.frame.png", false, Image.FILTER_NEAREST));


		LetterBuilder letterBuilder = new DefaultLetterBuilder(alphabethSheet);
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
			this.gameLogs = gameLogBuilder.buildGameLogs("assets/texts/gamelogs");
			this.gameTexts.put("intro", gameLogBuilder.buildGameLog("assets/texts/intro/intro"));
			this.gameTexts.put("outro", gameLogBuilder.buildGameLog("assets/texts/outro/outro"));
			this.gameTexts.put("credits", gameLogBuilder.buildGameLog("assets/texts/credits/credits"));
			this.gameTexts.put("references", gameLogBuilder.buildGameLog("assets/texts/references/references"));
		} catch (IOException e) {
			throw new RuntimeException("Could not load game logs.", e);
		}
		
		DyingHintsBuilder dyingHintsBuilder = new DyingHintsBuilderImpl();
		
		try {
			this.dyingHints.addAll(dyingHintsBuilder.build("assets/texts/dyinghints/dyinghints"));
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
