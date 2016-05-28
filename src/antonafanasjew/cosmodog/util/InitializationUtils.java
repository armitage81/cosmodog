package antonafanasjew.cosmodog.util;

import java.util.List;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.calendar.ComposedPlanetaryCalendarListener;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.calendar.listeners.FoodConsumer;
import antonafanasjew.cosmodog.calendar.listeners.FuelConsumer;
import antonafanasjew.cosmodog.calendar.listeners.WaterConsumer;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.Layers;
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
import antonafanasjew.cosmodog.model.User;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.actors.builder.EnemyFactory;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
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
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.FeatureBoundAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.rules.actions.WinningAction;
import antonafanasjew.cosmodog.rules.actions.async.DialogAction;
import antonafanasjew.cosmodog.rules.actions.async.PauseAction;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.rules.factories.TeleportRuleFactory;
import antonafanasjew.cosmodog.rules.triggers.InventoryBasedTrigger;
import antonafanasjew.cosmodog.rules.triggers.NewGameTrigger;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBox;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxStateUpdater;

public class InitializationUtils {

	public static int INFOBIT_TILE_ID = 191;
	public static int INSIGHT_TILE_ID = 196;
	public static int SOULESSENCE_TILE_ID = 197;
	public static int ARMOR_TILE_ID = 206;
	public static int SUPPLIES_TILE_ID = 190;
	public static int MEDIPACK_TILE_ID = 192;
	public static int VEHICLE_TILE_ID = 193;
	public static int BOAT_TILE_ID = 194;
	public static int DYNAMITE_TILE_ID = 198;
	public static int GEIGERZAEHLER_TILE_ID = 207;
	public static String LAYER_NAME_COLLECTIBLES = "Meta_collectibles";

	public static CosmodogGame initializeCosmodogGame(StateBasedGame game, CustomTiledMap tiledMap, CustomTiledMap customTiledMap, String userName) throws SlickException, TiledMapIoException {
		
		CosmodogGame cosmodogGame = new CosmodogGame();
		
		//We initialize the text box here as we need to refer to it when posting Alisa's comments in the updatable writing state.
		DrawingContext dialogBoxDc = ApplicationContext.instance().getDialogBoxDrawingContext();
		DrawingContext dialogWritingDc = DrawingContextUtils.writingContentDcFromDialogBoxDc(dialogBoxDc);
		WritingTextBox dialogWritingTextBox = new WritingTextBox(dialogWritingDc.w(), dialogWritingDc.h(), 0, 3, 18, 22);
		cosmodogGame.setCommentsStateUpdater(new WritingTextBoxStateUpdater(3000, dialogWritingTextBox));
		
		CosmodogMap cosmodogMap = initializeCosmodogMap(tiledMap, customTiledMap);
		
		RuleBook ruleBook = initializeRuleBook(game);

		User user = new User();
		user.setUserName(userName);
		
		//Player player = Player.fromPosition(5, 3);
		Player player = Player.fromPosition(31, 294);
		
		ArsenalInventoryItem arsenal = (ArsenalInventoryItem)player.getInventory().get(InventoryItemType.ARSENAL);
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.RPG));
		
		PlayerMovementListener playerMovementListener = new PlayerMovementListener();
		playerMovementListener.getPieceInteractionListeners().add(new RuleBookPieceInteractionListener());
		player.getMovementListeners().add(playerMovementListener);
		player.getMovementListeners().add(new RuleBookMovementListener());
		
		PlayerLifeListener playerLifeListener = new PlayerLifeListener();
		player.getLifeListeners().add(playerLifeListener);
		
		cosmodogGame.setMap(cosmodogMap);
		cosmodogGame.setRuleBook(ruleBook);
		cosmodogGame.setPlayer(player);
		cosmodogGame.setUser(user);
		PlanetaryCalendar planetaryCalendar = new PlanetaryCalendar();
		planetaryCalendar.setYear(2314);
		planetaryCalendar.setMonth(1);
		planetaryCalendar.setDay(27);
		planetaryCalendar.setHour(12);
		planetaryCalendar.setMinute(20);
		
		ComposedPlanetaryCalendarListener listener = new ComposedPlanetaryCalendarListener();
		listener.getUnderlyings().add(new FoodConsumer());
		listener.getUnderlyings().add(new WaterConsumer());
		listener.getUnderlyings().add(new FuelConsumer());
		planetaryCalendar.setListener(listener);
		
		cosmodogGame.setPlanetaryCalendar(planetaryCalendar);

		return cosmodogGame;
	}

	public static CosmodogMap initializeCosmodogMap(CustomTiledMap tiledMap, CustomTiledMap customTiledMap) throws SlickException, TiledMapIoException {

		int collectiblesLayerIndex = Layers.LAYER_META_COLLECTIBLES;
		int mapWidth = tiledMap.getWidth();
		int mapHeight = tiledMap.getHeight();

		
		CosmodogMap map = new CosmodogMap();
		
		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {
				int tileId = tiledMap.getTileId(k, l, collectiblesLayerIndex);
				if (tileId == INFOBIT_TILE_ID) {
					CollectibleGoodie c = new CollectibleGoodie(CollectibleGoodie.GoodieType.infobit);
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
				if (tileId == VEHICLE_TILE_ID) {
					Vehicle vehicle = Vehicle.fromPosition(k, l);
					map.getMapPieces().add(vehicle);
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
				
				tileId = tiledMap.getTileId(k, l, Layers.LAYER_META_EFFECTS);
				
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

		initializeTiledMapObjects(tiledMap, map);
		initializeEnemies(tiledMap, customTiledMap, map);
		
		return map;

	}
	
	private static void initializeTiledMapObjects(CustomTiledMap tiledMap, CosmodogMap cosmodogMap) {
		/*
		int objectGroupsCount = tiledMap.getObjectGroupCount();
		for (int i = 0; i < objectGroupsCount; i++) {
			TiledObjectGroup objectGroup = new TiledObjectGroup();
			objectGroup.setName(String.valueOf(i));
			int objectsCount = tiledMap.getObjectCount(i);
			for (int j = 0; j < objectsCount; j++) {
				
				TiledRectObject object = new TiledRectObject();
				object.setId(j);
				object.setName(tiledMap.getObjectName(i, j));
				object.setType(tiledMap.getObjectType(i, j));
				object.setX(tiledMap.getObjectX(i, j));
				object.setY(tiledMap.getObjectY(i, j));
				object.setWidth(tiledMap.getObjectWidth(i, j));
				object.setHeight(tiledMap.getObjectHeight(i, j));
				objectGroup.getObjects().add(object);
				
			}
			cosmodogMap.getObjectGroups().put(objectGroup.getName(), objectGroup);
		}
		*/
	}

	@SuppressWarnings("unchecked")
	private static RuleBook initializeRuleBook(StateBasedGame game) {
		RuleBook ruleBook = new RuleBook();

		ResourceWrapperBuilder<Rule> ruleBuilder;
		Map<String, GenericResourceWrapper<Rule>> ruleResourceWrappers;
		
		//Add all region dependent dialog rules.
		ruleBuilder = new RegionDependentDialogRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}
		
		//Add all region dependent comment rules (like dialogs, but simpler and not blocking)
		ruleBuilder = new RegionDependentCommentRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}
		
		//Add all pieces related dialog rules.
		ruleBuilder = new PieceRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}
		
		//Add all multiinstance piece collection dialog rules.
		ruleBuilder = new MultiInstancePieceRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}
		
		//Add all item notification rules.
		ruleBuilder = new ItemNotificationRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}
		
		//Beginning of the game.
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
				
		Rule rule = new Rule(
			Rule.RULE_DIALOG_AFTER_LANDING,
			new NewGameTrigger(),
			BlockAction.block(atTheBeginningActions)
		);
		ruleBook.put(rule.getId(), rule);
		
		
		//Winning rule
		rule = new Rule(
			Rule.RULE_WINNING,
			Lists.newArrayList(GameEventEndedTurn.class),
			new InventoryBasedTrigger(InventoryItemType.INSIGHT, InventoryItem.INVENTORY_ITEM_INSIGHT_MAX_COUNT),
			new WinningAction(game),
			Rule.RULE_PRIORITY_EARLIEST
		);
		ruleBook.put(rule.getId(), rule);
		
		
		//Teleport rules
		Map<String, Rule> teleportRules = TeleportRuleFactory.getInstance().buildRules();
		ruleBook.putAll(teleportRules);
		
		
		
		return ruleBook;
	}
	
	private static void initializeEnemies(CustomTiledMap tiledMap, CustomTiledMap customTiledMap, CosmodogMap map) {
		Map<UnitType, EnemyFactory> enemyFactories = initializeEnemyFactories();
		int enemyLayerIndex = Layers.LAYER_META_NPC;
		
		int mapWidth = tiledMap.getWidth();
		int mapHeight = tiledMap.getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {
				int tileId = tiledMap.getTileId(k, l, enemyLayerIndex);
				
				//Will be null in most cases.
				UnitType unitType = Mappings.ENEMY_TILE_TYPE_TO_UNIT_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_NPC, tileId));
						
				if (unitType != null) {
					EnemyFactory enemyFactory = enemyFactories.get(unitType);
					Enemy enemy = enemyFactory.buildEnemy();
					
					enemy.setPositionX(k);
					enemy.setPositionY(l);
					
					//Check if the enemy stands on one of a home regions and assign it to him, if this is the case.
					//Take note: Assumming no overlapped home regions.
					Map<String, TiledObject> homeRegions = customTiledMap.getObjectGroups().get(ObjectGroupUtils.OBJECT_GROUP_ID_NPC_HOME_REGIONS).getObjects();
					for (TiledObject homeRegion : homeRegions.values()) {
						
						int x = k * customTiledMap.getTileWidth();
						int y = l * customTiledMap.getTileHeight();
						int w = tiledMap.getTileWidth();
						int h = tiledMap.getTileHeight();
						
						PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h);
						
						boolean intersects = CollisionUtils.intersects(r, homeRegion);
						
						if (intersects) {
							enemy.setHomeRegionName(homeRegion.getName());
						}
						
					}
					
					
					enemy.setDirection(DirectionType.LEFT);
					
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
