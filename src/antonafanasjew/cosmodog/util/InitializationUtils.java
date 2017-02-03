package antonafanasjew.cosmodog.util;

import java.util.List;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.calendar.ComposedPlanetaryCalendarListener;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.calendar.listeners.FoodConsumer;
import antonafanasjew.cosmodog.calendar.listeners.FuelConsumer;
import antonafanasjew.cosmodog.calendar.listeners.WaterConsumer;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.listener.life.PlayerLifeListener;
import antonafanasjew.cosmodog.listener.movement.PlayerMovementListener;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Effect;
import antonafanasjew.cosmodog.model.Mark;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.User;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.actors.builder.EnemyFactory;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.AxeInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.FuelTankInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.JacketInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MacheteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MineDetectorInventoryItem;
import antonafanasjew.cosmodog.model.inventory.PickInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.resourcehandling.GenericResourceWrapper;
import antonafanasjew.cosmodog.resourcehandling.ResourceWrapperBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.enemyfactory.EnemyFactoryBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.ItemNotificationRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.MultiInstancePieceRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.PieceRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.RegionDependentCommentRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.RegionDependentDialogRuleBuilder;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.rules.RuleBookMovementListener;
import antonafanasjew.cosmodog.rules.RuleBookPieceInteractionListener;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.FeatureBoundAction;
import antonafanasjew.cosmodog.rules.actions.GetScoreForCollectibleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.rules.actions.WinningAction;
import antonafanasjew.cosmodog.rules.actions.async.DialogAction;
import antonafanasjew.cosmodog.rules.actions.async.PauseAction;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.DeactivateMinesAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.SwitchOnSewageToDelayWormAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.SwitchOnVentilationToDelayWormAction;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;
import antonafanasjew.cosmodog.rules.factories.PoisonDeactivationRuleFactory;
import antonafanasjew.cosmodog.rules.factories.TeleportRuleFactory;
import antonafanasjew.cosmodog.rules.triggers.EnteringRegionTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.InteractingWithEveryCollectibleTrigger;
import antonafanasjew.cosmodog.rules.triggers.InventoryBasedTrigger;
import antonafanasjew.cosmodog.rules.triggers.NewGameTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.timing.Chronometer;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.view.transitions.ActorTransitionRegistry;
import antonafanasjew.cosmodog.view.transitions.TeleportationTransition;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxStateUpdater;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class InitializationUtils {

	public static int INFOBIT_TILE_ID = 191;
	public static int INFOBYTE_TILE_ID = 3069;
	public static int INFOBANK_TILE_ID = 3070;
	public static int SOFTWARE_TILE_ID = 3071;
	public static int CHART_TILE_ID = 3072;
	public static int MINEDETECTOR_TILE_ID = 3073;
	public static int ANTIDOTE_TILE_ID = 3074;
	public static int INSIGHT_TILE_ID = 196;
	public static int SOULESSENCE_TILE_ID = 197;
	public static int ARMOR_TILE_ID = 206;
	public static int SUPPLIES_TILE_ID = 190;
	public static int MEDIPACK_TILE_ID = 192;
	public static int VEHICLE_TILE_ID = 193;
	public static int BOAT_TILE_ID = 194;
	public static int DYNAMITE_TILE_ID = 198;
	public static int GEIGERZAEHLER_TILE_ID = 207;
	public static int SUPPLYTRACKER_TILE_ID = 3061;
	public static int BINOCULARS_TILE_ID = 3062;
	public static int JACKET_TILE_ID = 3063;
	public static int SKI_TILE_ID = 3064;

	public static int PICK_TILE_ID = 3066;
	public static int MACHETE_TILE_ID = 3067;
	public static int AXE_TILE_ID = 3068;

	public static int PLATFORM_TILE_ID = 3065;
	public static int BOTTLE_TILE_ID = 201;
	public static int FOOD_COMPARTMENT_TILE_ID = 205;
	public static String LAYER_NAME_COLLECTIBLES = "Meta_collectibles";

	public static void initializeCosmodogGameNonTransient(CosmodogGame cosmodogGame, StateBasedGame game, CustomTiledMap customTiledMap, String userName) throws SlickException, TiledMapIoException {
		CosmodogMap cosmodogMap = initializeCosmodogMap(customTiledMap);
		cosmodogGame.setMap(cosmodogMap);

		User user = new User();
		user.setUserName(userName);
		cosmodogGame.setUser(user);

		Player player = Player.fromPosition(248, 114);
		player.setMaxLife(50);
		player.setLife(50);

		for (int i = 0; i < 999; i++) {
			player.getGameProgress().addInfobit();
		}

		ArsenalInventoryItem arsenal = (ArsenalInventoryItem) player.getInventory().get(InventoryItemType.ARSENAL);
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.RIFLE, 1));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN, 1));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.RPG, 1));

		player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
		player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());

		player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
		player.getInventory().put(InventoryItemType.AXE, new AxeInventoryItem());
		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
		player.getInventory().put(InventoryItemType.FUEL_TANK, new FuelTankInventoryItem());
		player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new GeigerZaehlerInventoryItem());
		player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
		player.getInventory().put(InventoryItemType.MACHETE, new MacheteInventoryItem());
		player.getInventory().put(InventoryItemType.PICK, new PickInventoryItem());
		player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
		player.getInventory().put(InventoryItemType.SUPPLYTRACKER, new SupplyTrackerInventoryItem());
		player.getInventory().put(InventoryItemType.MINEDETECTOR, new MineDetectorInventoryItem());

		cosmodogGame.setPlayer(player);

		PlanetaryCalendar planetaryCalendar = new PlanetaryCalendar();
		planetaryCalendar.setYear(2314);
		planetaryCalendar.setMonth(1);
		planetaryCalendar.setDay(27);
		planetaryCalendar.setHour(8);
		planetaryCalendar.setMinute(20);
		cosmodogGame.setPlanetaryCalendar(planetaryCalendar);

	}

	public static void initializeCosmodogGameTransient(CosmodogGame cosmodogGame) {

		cosmodogGame.getMap().setCustomTiledMap(ApplicationContextUtils.getCustomTiledMap());
		cosmodogGame.setActionRegistry(new ActionRegistry());
		cosmodogGame.setInterfaceActionRegistry(new ActionRegistry());
		cosmodogGame.setTeleportationTransition(new TeleportationTransition());
		cosmodogGame.setActorTransitionRegistry(new ActorTransitionRegistry());
		cosmodogGame.setChronometer(new Chronometer());
		cosmodogGame.setRuleBook(new RuleBook());

		// We initialize the text box here as we need to refer to it when
		// posting Alisa's comments in the updatable writing state.
		DrawingContext dialogBoxDc = ApplicationContext.instance().getDialogBoxDrawingContext();
		DrawingContext dialogWritingDc = DrawingContextUtils.writingContentDcFromDialogBoxDc(dialogBoxDc);
		WritingTextBox dialogWritingTextBox = new WritingTextBox(dialogWritingDc.w(), dialogWritingDc.h(), 0, 3, 18, 22);
		cosmodogGame.setCommentsStateUpdater(new WritingTextBoxStateUpdater(3000, dialogWritingTextBox));

		initializeRuleBook(cosmodogGame);

		Player player = cosmodogGame.getPlayer();
		PlayerMovementListener playerMovementListener = new PlayerMovementListener();
		playerMovementListener.getPieceInteractionListeners().add(new RuleBookPieceInteractionListener());
		player.getMovementListeners().clear();
		player.getMovementListeners().add(playerMovementListener);
		player.getMovementListeners().add(new RuleBookMovementListener());
		player.getMovementListeners().add(PlayerMovementCache.getInstance());
		PlayerLifeListener playerLifeListener = new PlayerLifeListener();
		player.getLifeListeners().add(playerLifeListener);

		PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
		ComposedPlanetaryCalendarListener listener = new ComposedPlanetaryCalendarListener();
		listener.getUnderlyings().add(new FoodConsumer());
		listener.getUnderlyings().add(new WaterConsumer());
		listener.getUnderlyings().add(new FuelConsumer());
		planetaryCalendar.setListener(listener);
	}

	public static CosmodogGame initializeCosmodogGame(StateBasedGame game, CustomTiledMap customTiledMap, String userName) throws SlickException, TiledMapIoException {

		CosmodogGame cosmodogGame = new CosmodogGame();

		initializeCosmodogGameNonTransient(cosmodogGame, game, customTiledMap, userName);
		initializeCosmodogGameTransient(cosmodogGame);

		return cosmodogGame;
	}

	public static CosmodogMap initializeCosmodogMap(CustomTiledMap customTiledMap) throws SlickException, TiledMapIoException {

		CosmodogMap map = new CosmodogMap(customTiledMap);

		map.getMapModification().modifyTile(7, 2, Layers.LAYER_WATER, TileType.WATER_CENTER);
		map.getMapModification().modifyTile(7, 2, Layers.LAYER_META_COLLISIONS, TileType.COLLISION_WATER);

		int collectiblesLayerIndex = Layers.LAYER_META_COLLECTIBLES;
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {
				int tileId = map.getTileId(k, l, collectiblesLayerIndex);
				if (tileId == INFOBIT_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.infobit);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == INFOBYTE_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.infobyte);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == INFOBANK_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.infobank);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == INSIGHT_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.insight);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == SOFTWARE_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.software);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == CHART_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.chart);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == SUPPLIES_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.supplies);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == MEDIPACK_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.medipack);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == SOULESSENCE_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.soulessence);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				if (tileId == ARMOR_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.armor);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == BOTTLE_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.bottle);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == FOOD_COMPARTMENT_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.foodcompartment);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == VEHICLE_TILE_ID) {
					Vehicle vehicle = Vehicle.fromPosition(k, l);
					map.getMapPieces().add(vehicle);
				}

				if (tileId == PLATFORM_TILE_ID) {
					Platform platform = Platform.fromPosition(k, l);
					map.getMapPieces().add(platform);
				}

				if (tileId == BOAT_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.boat);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == DYNAMITE_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.dynamite);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == GEIGERZAEHLER_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.geigerzaehler);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == SUPPLYTRACKER_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.supplytracker);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == BINOCULARS_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.binoculars);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == JACKET_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.jacket);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}
				
				if (tileId == ANTIDOTE_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.antidote);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == MINEDETECTOR_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.minedetector);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == SKI_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.ski);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == PICK_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.pick);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == MACHETE_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.machete);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (tileId == AXE_TILE_ID) {
					CollectibleTool c = new CollectibleTool(CollectibleTool.ToolType.axe);
					c.setPositionX(k);
					c.setPositionY(l);
					map.getMapPieces().add(c);
				}

				if (TileType.FUEL.getTileId() == tileId) {
					Mark m = new Mark(Mark.FUEL_MARK_TYPE);
					m.setPositionX(k);
					m.setPositionY(l);
					map.getMarkedTilePieces().add(m);
				}

				if (TileType.WEAPONS_TILES.contains(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileId))) {

					WeaponType weaponType = Mappings.COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileId));
					Weapon weapon = new Weapon(weaponType);
					CollectibleWeapon collWeapon = new CollectibleWeapon(weapon);
					collWeapon.setPositionX(k);
					collWeapon.setPositionY(l);
					map.getMapPieces().add(collWeapon);

				}

				if (TileType.AMMO_TILES.contains(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileId))) {
					WeaponType weaponType = Mappings.COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileId));
					CollectibleAmmo collAmmo = new CollectibleAmmo(weaponType);
					collAmmo.setPositionX(k);
					collAmmo.setPositionY(l);
					map.getMapPieces().add(collAmmo);
				}

				tileId = customTiledMap.getTileId(k, l, Layers.LAYER_META_EFFECTS);

				if (TileType.TELEPORT_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_TELEPORT);
					effect.setPositionX(k);
					effect.setPositionY(l);
					map.getEffectPieces().add(effect);
				}

				if (TileType.FIRE_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_FIRE);
					effect.setPositionX(k);
					effect.setPositionY(l);
					map.getEffectPieces().add(effect);
				}

				if (TileType.SMOKE_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_SMOKE);
					effect.setPositionX(k);
					effect.setPositionY(l);
					map.getEffectPieces().add(effect);
				}

				if (TileType.ELECTRICITY_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_ELECTRICITY);
					effect.setPositionX(k);
					effect.setPositionY(l);
					map.getEffectPieces().add(effect);
				}

				if (TileType.ENERGY_WALL_TILES.contains(TileType.getByLayerAndTileId(Layers.LAYER_META_EFFECTS, tileId))) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_ENERGYWALL);
					effect.setPositionX(k);
					effect.setPositionY(l);
					map.getEffectPieces().add(effect);
				}

			}

		}

		initializeTiledMapObjects(customTiledMap, map);
		initializeEnemies(customTiledMap, map);
		initializeDynamicTiles(customTiledMap, map);

		return map;

	}

	private static void initializeDynamicTiles(CustomTiledMap tiledMap, CosmodogMap map) {
		int dynamicTilesLayerIndex = Layers.LAYER_META_DYNAMIC_PIECES;
		int mapWidth = tiledMap.getWidth();
		int mapHeight = tiledMap.getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {

				int tileId = tiledMap.getTileId(k, l, dynamicTilesLayerIndex);

				if (tileId == TileType.DYNAMIC_PIECE_STONE.getTileId()) {
					Stone stone = Stone.create(k, l);
					map.getDynamicPieces().put(Stone.class, stone);
				}

				if (tileId == TileType.DYNAMIC_PIECE_HARDSTONE.getTileId()) {
					HardStone hardStone = HardStone.create(k, l);
					map.getDynamicPieces().put(HardStone.class, hardStone);
				}

				if (tileId == TileType.DYNAMIC_PIECE_TREE.getTileId()) {
					Tree tree = Tree.create(k, l);
					map.getDynamicPieces().put(Tree.class, tree);
				}

				if (tileId == TileType.DYNAMIC_PIECE_BAMBOO.getTileId()) {
					Bamboo bamboo = Bamboo.create(k, l);
					map.getDynamicPieces().put(Bamboo.class, bamboo);
				}

				if (tileId == TileType.DYNAMIC_PIECE_CRATE.getTileId()) {
					Crate crate = Crate.create(k, l);
					map.getDynamicPieces().put(Crate.class, crate);
				}

				if (tileId == TileType.DYNAMIC_PIECE_MINE.getTileId()) {
					Mine mine = Mine.create(k, l);
					map.getDynamicPieces().put(Mine.class, mine);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_POISON.getTileId()) {
					Poison poison = Poison.create(k, l);
					map.getDynamicPieces().put(Poison.class, poison);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_PRESSUREBUTTON.getTileId()) {
					PressureButton pressureButton = PressureButton.create(k, l);
					map.getDynamicPieces().put(PressureButton.class, pressureButton);
				}
			}
		}
	}

	private static void initializeTiledMapObjects(CustomTiledMap tiledMap, CosmodogMap cosmodogMap) {
		/*
		 * int objectGroupsCount = tiledMap.getObjectGroupCount(); for (int i =
		 * 0; i < objectGroupsCount; i++) { TiledObjectGroup objectGroup = new
		 * TiledObjectGroup(); objectGroup.setName(String.valueOf(i)); int
		 * objectsCount = tiledMap.getObjectCount(i); for (int j = 0; j <
		 * objectsCount; j++) {
		 * 
		 * TiledRectObject object = new TiledRectObject(); object.setId(j);
		 * object.setName(tiledMap.getObjectName(i, j));
		 * object.setType(tiledMap.getObjectType(i, j));
		 * object.setX(tiledMap.getObjectX(i, j));
		 * object.setY(tiledMap.getObjectY(i, j));
		 * object.setWidth(tiledMap.getObjectWidth(i, j));
		 * object.setHeight(tiledMap.getObjectHeight(i, j));
		 * objectGroup.getObjects().add(object);
		 * 
		 * } cosmodogMap.getObjectGroups().put(objectGroup.getName(),
		 * objectGroup); }
		 */
	}

	@SuppressWarnings("unchecked")
	private static void initializeRuleBook(CosmodogGame cosmodogGame) {
		RuleBook ruleBook = new RuleBook();

		ResourceWrapperBuilder<Rule> ruleBuilder;
		Map<String, GenericResourceWrapper<Rule>> ruleResourceWrappers;

		// Add all region dependent dialog rules.
		ruleBuilder = new RegionDependentDialogRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Add all region dependent comment rules (like dialogs, but simpler and
		// not blocking)
		ruleBuilder = new RegionDependentCommentRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Add all pieces related dialog rules.
		ruleBuilder = new PieceRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Add all multiinstance piece collection dialog rules.
		ruleBuilder = new MultiInstancePieceRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Add all item notification rules.
		ruleBuilder = new ItemNotificationRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Beginning of the game.
		AsyncAction dialogAsyncAction = new DialogAction(NarrativeSequenceUtils.STORY_MAIN_0001_AFTERLANDING);
		RuleAction dialogRegisteringAction = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, dialogAsyncAction);

		List<RuleAction> tutorialActions = Lists.newArrayList();

		for (int i = 0; i < TutorialUtils.INITIAL_TUTORIAL_TEXTS.size(); i++) {
			RuleAction pauseAction = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, new PauseAction(500));
			pauseAction = new FeatureBoundAction(Features.FEATURE_TUTORIAL, pauseAction);
			tutorialActions.add(pauseAction);
			String tutorialText = TutorialUtils.INITIAL_TUTORIAL_TEXTS.get(i);
			AsyncAction tutorialAsyncAction = new PopUpNotificationAction(tutorialText);
			RuleAction tutorialRegistrationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, tutorialAsyncAction);
			tutorialRegistrationAction = new FeatureBoundAction(Features.FEATURE_TUTORIAL, tutorialRegistrationAction);
			tutorialActions.add(tutorialRegistrationAction);
		}

		List<RuleAction> atTheBeginningActions = Lists.newArrayList();
		atTheBeginningActions.add(new FeatureBoundAction(Features.FEATURE_STORY, dialogRegisteringAction));
		atTheBeginningActions.add(new SetGameProgressPropertyAction(GameProgress.GAME_PROGRESS_PROPERTY_AFTERLANDING, "true"));
		atTheBeginningActions.addAll(tutorialActions);

		Rule rule = new Rule(Rule.RULE_DIALOG_AFTER_LANDING, new NewGameTrigger(), BlockAction.block(atTheBeginningActions));
		ruleBook.put(rule.getId(), rule);

		// Winning rule
		rule = new Rule(Rule.RULE_WINNING, Lists.newArrayList(GameEventEndedTurn.class), new InventoryBasedTrigger(InventoryItemType.INSIGHT, InventoryItem.INVENTORY_ITEM_INSIGHT_MAX_COUNT), new WinningAction(), Rule.RULE_PRIORITY_EARLIEST);
		ruleBook.put(rule.getId(), rule);

		// Score rewards rule
		rule = new Rule(Rule.RULE_SCORE_REWARD, Lists.newArrayList(GameEventPieceInteraction.class), new InteractingWithEveryCollectibleTrigger(), new GetScoreForCollectibleAction(), Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);

		// Teleport rules
		Map<String, Rule> teleportRules = TeleportRuleFactory.getInstance().buildRules(cosmodogGame);
		ruleBook.putAll(teleportRules);
		
		// Poison deactivation rules
		Map<String, Rule> poisonDeactivationRules = PoisonDeactivationRuleFactory.getInstance().buildRules(cosmodogGame);
		ruleBook.putAll(poisonDeactivationRules);

		// Mine deactivation rules
		for (QuadrandType quadrandType : QuadrandType.values()) {
			RuleTrigger deactivateMinesForQuadrandTrigger = new GameProgressPropertyTrigger("MinesDeactivatedForQuadrand" + quadrandType, "false");
			deactivateMinesForQuadrandTrigger = AndTrigger.and(new EnteringRegionTrigger(ObjectGroups.OBJECT_GROUP_ID_REGIONS, "DeactivateMines" + quadrandType), deactivateMinesForQuadrandTrigger);
			AsyncAction asyncAction = new PopUpNotificationAction("The console controls the land mines<br>in the quadrand " + quadrandType.getRepresentation() + ".<br>You use it to disarm the mines.<br><br>[Press ENTER]");
			RuleAction notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
			RuleAction deactivateMinesAction = new DeactivateMinesAction(quadrandType);
			deactivateMinesAction = BlockAction.block(deactivateMinesAction, new SetGameProgressPropertyAction("MinesDeactivatedForQuadrand" + quadrandType, "true"), notificationAction);

			rule = new Rule(deactivateMinesAction.getClass().getSimpleName() + ":" + quadrandType, Lists.newArrayList(GameEventChangedPosition.class), deactivateMinesForQuadrandTrigger, deactivateMinesAction, Rule.RULE_PRIORITY_LATEST);
			ruleBook.put(rule.getId(), rule);

		}

		// Worm delay rules
		RuleTrigger switchOnVentilationTrigger = new GameProgressPropertyTrigger("WormAreaVentilationOn", "false");
		switchOnVentilationTrigger = AndTrigger.and(new EnteringRegionTrigger(ObjectGroups.OBJECT_GROUP_ID_REGIONS, "SwitchOnVentilation"), switchOnVentilationTrigger);

		AsyncAction asyncAction = new PopUpNotificationAction("This is the control panel for the ventilation.<br>You activate it.<br>The worm will have harder time to locate you.<br><br>[Press ENTER]");
		RuleAction notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		RuleAction switchOnVentilationAction = new SwitchOnVentilationToDelayWormAction();
		switchOnVentilationAction = BlockAction.block(switchOnVentilationAction, new SetGameProgressPropertyAction("WormAreaVentilationOn", "true"), notificationAction);

		rule = new Rule(Rule.RULE_WORM_DELAY_PHASE2, Lists.newArrayList(GameEventChangedPosition.class), switchOnVentilationTrigger, switchOnVentilationAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);

		RuleTrigger switchOnSewageTrigger = new GameProgressPropertyTrigger("WormAreaSewageOn", "false");
		switchOnSewageTrigger = AndTrigger.and(new EnteringRegionTrigger(ObjectGroups.OBJECT_GROUP_ID_REGIONS, "SwitchOnSewage"), switchOnSewageTrigger);

		asyncAction = new PopUpNotificationAction("This is the control panel for the sewage.<br>You activate it.<br>The worm will have even harder time to locate you.<br><br>[Press ENTER]");
		notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		RuleAction switchOnSewageAction = new SwitchOnSewageToDelayWormAction();
		switchOnSewageAction = BlockAction.block(switchOnSewageAction, new SetGameProgressPropertyAction("WormAreaSewageOn", "true"), notificationAction);

		rule = new Rule(Rule.RULE_WORM_DELAY_PHASE3, Lists.newArrayList(GameEventChangedPosition.class), switchOnSewageTrigger, switchOnSewageAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);

		cosmodogGame.setRuleBook(ruleBook);

	}

	private static void initializeEnemies(CustomTiledMap customTiledMap, CosmodogMap map) {
		Map<UnitType, EnemyFactory> enemyFactories = initializeEnemyFactories();
		int enemyLayerIndex = Layers.LAYER_META_NPC;

		int mapWidth = customTiledMap.getWidth();
		int mapHeight = customTiledMap.getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {
				int tileId = customTiledMap.getTileId(k, l, enemyLayerIndex);

				// Will be null in most cases.
				UnitType unitType = Mappings.ENEMY_TILE_TYPE_TO_UNIT_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_NPC, tileId));

				if (unitType != null) {
					EnemyFactory enemyFactory = enemyFactories.get(unitType);
					Enemy enemy = enemyFactory.buildEnemy();

					enemy.setPositionX(k);
					enemy.setPositionY(l);

					// Check if the enemy stands on one of a home regions and
					// assign it to him, if this is the case.
					// Take note: Assuming no overlapped home regions.
					Map<String, TiledObject> homeRegions = customTiledMap.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_NPC_HOME_REGIONS).getObjects();
					for (TiledObject homeRegion : homeRegions.values()) {

						int x = k * customTiledMap.getTileWidth();
						int y = l * customTiledMap.getTileHeight();
						int w = customTiledMap.getTileWidth();
						int h = customTiledMap.getTileHeight();

						PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h);

						boolean intersects = CollisionUtils.intersects(r, homeRegion);

						if (intersects) {
							enemy.setHomeRegionName(homeRegion.getName());

							// Now check if the region 'deactivates' units at
							// night.
							String dayOnlyPropertyValue = homeRegion.getProperties().get("DayOnly");
							boolean dayOnly = dayOnlyPropertyValue != null && Boolean.valueOf(dayOnlyPropertyValue);
							enemy.setActiveAtDayTimeOnly(dayOnly);

						}

					}

					enemy.setDirection(DirectionType.random());

					map.getEnemies().add(enemy);
				}

			}
		}

	}

	private static Map<UnitType, EnemyFactory> initializeEnemyFactories() {
		ResourceWrapperBuilder<EnemyFactory> enemyFactoryBuilder = new EnemyFactoryBuilder();
		Map<String, GenericResourceWrapper<EnemyFactory>> enemyFactories = enemyFactoryBuilder.build();
		Map<UnitType, EnemyFactory> retVal = Maps.newHashMap();
		for (String key : enemyFactories.keySet()) {
			retVal.put(UnitType.valueOf(key), enemyFactories.get(key).getEntity());
		}
		return retVal;
	}

}
