package antonafanasjew.cosmodog.util;

import java.util.*;

import antonafanasjew.cosmodog.actions.spacelift.SpaceLiftAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.*;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.*;
import antonafanasjew.cosmodog.model.portals.ReflectionType;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.ActivatableHolder;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.model.portals.interfaces.SwitchableHolder;
import antonafanasjew.cosmodog.resourcehandling.builder.enemyfactory.JsonBasedEnemyFactoryBuilder;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.notification.OnScreenNotificationAction;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.QuadrandType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontProvider;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.listener.life.PlayerLifeListener;
import antonafanasjew.cosmodog.listener.movement.PlayerMovementListener;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.builder.EnemyFactory;
import antonafanasjew.cosmodog.model.dynamicpieces.AlienBaseBlockade;
import antonafanasjew.cosmodog.model.dynamicpieces.Bamboo;
import antonafanasjew.cosmodog.model.dynamicpieces.BinaryIndicator;
import antonafanasjew.cosmodog.model.dynamicpieces.Block;
import antonafanasjew.cosmodog.model.dynamicpieces.Crate;
import antonafanasjew.cosmodog.model.dynamicpieces.CrumbledWall;
import antonafanasjew.cosmodog.model.dynamicpieces.Door;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorAppearanceType;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.dynamicpieces.Gate;
import antonafanasjew.cosmodog.model.dynamicpieces.HardStone;
import antonafanasjew.cosmodog.model.dynamicpieces.LetterPlate;
import antonafanasjew.cosmodog.model.dynamicpieces.Mine;
import antonafanasjew.cosmodog.model.dynamicpieces.Poison;
import antonafanasjew.cosmodog.model.dynamicpieces.PressureButton;
import antonafanasjew.cosmodog.model.dynamicpieces.SecretDoor;
import antonafanasjew.cosmodog.model.dynamicpieces.Stone;
import antonafanasjew.cosmodog.model.dynamicpieces.Terminal;
import antonafanasjew.cosmodog.model.dynamicpieces.Tree;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.resourcehandling.GenericResourceWrapper;
import antonafanasjew.cosmodog.resourcehandling.ResourceWrapperBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.ItemNotificationRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.MultiInstancePieceRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.RegionDependentDialogRuleBuilder;
import antonafanasjew.cosmodog.resourcehandling.builder.rules.RegionDependentPopupRuleBuilder;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.PlaySoundRuleAction;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.FeatureBoundAction;
import antonafanasjew.cosmodog.rules.actions.GetScoreForCollectibleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.rules.actions.WinningAction;
import antonafanasjew.cosmodog.actions.popup.PauseAction;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.DamageLastBossAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.DeactivateMinesAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.DeactivateWormAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.SwitchOnSewageToDelayWormAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.SwitchOnVentilationToDelayWormAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.UpdateAlienBaseGateSequenceAction;
import antonafanasjew.cosmodog.rules.actions.gameprogress.UpdateAlienBaseTeleportSequenceAction;
import antonafanasjew.cosmodog.rules.actions.pickupitems.PickupKeyAction;
import antonafanasjew.cosmodog.rules.actions.sokoban.OperateSecretDoorsInSokobanAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;
import antonafanasjew.cosmodog.rules.events.GameEventTeleported;
import antonafanasjew.cosmodog.rules.factories.PoisonDeactivationRuleFactory;
import antonafanasjew.cosmodog.rules.factories.TeleportRuleFactory;
import antonafanasjew.cosmodog.rules.triggers.EnteringRegionTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressWinningConditionTrigger;
import antonafanasjew.cosmodog.rules.triggers.InteractingWithEveryCollectibleTrigger;
import antonafanasjew.cosmodog.rules.triggers.InventoryBasedTrigger;
import antonafanasjew.cosmodog.rules.triggers.NewGameTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.InvertedTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.OrTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.TrueTrigger;
import antonafanasjew.cosmodog.sound.AmbientSoundRegistry;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.timing.Timer;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;

public class InitializationUtils {

	public static String LAYER_NAME_COLLECTIBLES = "Meta_collectibles";

	public static void initializeCosmodogGameNonTransient(CosmodogGame cosmodogGame, StateBasedGame game, Map<MapType, CustomTiledMap> customTiledMaps, String userName) throws SlickException, TiledMapIoException {
		
		Map<MapType, CosmodogMap> cosmodogMaps = initializeCosmodogMaps(customTiledMaps);
		cosmodogGame.getMaps().clear();
		cosmodogGame.getMaps().putAll(cosmodogMaps);
		
		User user = new User();
		user.setUserName(userName);
		cosmodogGame.setUser(user);

		PlayerBuilder playerBuilder = ApplicationContextUtils.getCosmodog().getPlayerBuilder();
		Player player = playerBuilder.buildPlayer();
				
		cosmodogGame.setPlayer(player);

		PlanetaryCalendar planetaryCalendar = new PlanetaryCalendar();
		planetaryCalendar.setYear(2314);
		planetaryCalendar.setMonth(1);
		planetaryCalendar.setDay(27);
		planetaryCalendar.setHour(8);
		planetaryCalendar.setMinute(0);
		cosmodogGame.setPlanetaryCalendar(planetaryCalendar);
		
		Timer timer = new Timer();
		cosmodogGame.setTimer(timer);
		cosmodogGame.getTimer().initPlayTime();

	}

	public static void initializeCosmodogGameTransient(CosmodogGame cosmodogGame) {

		for (MapType mapType : MapType.values()) {
			cosmodogGame.getMaps().get(mapType).setCustomTiledMap(ApplicationContextUtils.getCustomTiledMaps().get(mapType));
		}

		cosmodogGame.setActionRegistry(new ActionRegistry());
		cosmodogGame.setAmbientSoundRegistry(new AmbientSoundRegistry());
		cosmodogGame.setInterfaceActionRegistry(new ActionRegistry());
		cosmodogGame.setRuleBook(new RuleBook());

		initializeRuleBook(cosmodogGame);
		
		Player player = cosmodogGame.getPlayer();
		player.setMovementListener(new PlayerMovementListener());

		PlayerLifeListener playerLifeListener = new PlayerLifeListener();
		player.getLifeListeners().clear();
		player.getLifeListeners().add(playerLifeListener);
		
		cosmodogGame.getTimer().setReferenceTimeSupplier(System::currentTimeMillis);
		cosmodogGame.getTimer().initLastUpdateTime();

	}

	public static CosmodogGame initializeCosmodogGame(StateBasedGame game, Map<MapType, CustomTiledMap> customTiledMaps, String userName) throws SlickException, TiledMapIoException {

		//We just have to initialize this heavy-weight enum to avoid lazy loading with delays in game.
		@SuppressWarnings("unused")
		FontType fontType = FontProvider.getInstance().fontType(FontTypeName.LicenseText);	
		CosmodogGame cosmodogGame = new CosmodogGame();

		initializeCosmodogGameNonTransient(cosmodogGame, game, customTiledMaps, userName);
		initializeCosmodogGameTransient(cosmodogGame);

		return cosmodogGame;
	}

	public static Map<MapType, CosmodogMap> initializeCosmodogMaps(Map<MapType, CustomTiledMap> customTiledMaps) throws SlickException, TiledMapIoException {

		Map<MapType, CosmodogMap> retVal = Maps.newHashMap();

		for (MapType mapType : MapType.values()) {

			CustomTiledMap customTiledMap = customTiledMaps.get(mapType);
			CosmodogMap map = new CosmodogMap(customTiledMap);

			map.setMapType(mapType);
			initializeEffects(customTiledMap, map);
			initializeTiledMapObjects(customTiledMap, map);
			initializeEnemies(customTiledMap, map);
			initializeDynamicTiles(customTiledMap, map);
			initializeSwitchableAndActivatableConnectors(map);
			//This method call relies on the dynamic piece initialization, so don't shift it before the dynamic piece initialization method.
			initializeMoveableGroups(customTiledMap, map);
			//This method call relies on the enemy initialization, so don't shift it before the enemy initialization method.
			initializeCollectibles(customTiledMap, map);

			retVal.put(mapType, map);
		}

		return retVal;

	}

	private static void initializeMoveableGroups(CustomTiledMap customTiledMap, CosmodogMap map) {

		TiledObjectGroup moveableGroupObjectGroup = customTiledMap.getObjectGroups().get("MoveableGroups");
		Map<String, TiledObject> moveableGroupRegions = moveableGroupObjectGroup.getObjects();
		Set<String> moveableGroupRegionNames = moveableGroupRegions.keySet();
		for (String moveableGroupRegionName : moveableGroupRegionNames) {
			
			TiledObject moveableGroupRegion = moveableGroupRegions.get(moveableGroupRegionName);
			
			List<MoveableDynamicPiece> moveablesInRegion = map
					.getDynamicPieces()
					.values()
					.stream()
					.filter(e -> (e instanceof MoveableDynamicPiece))
					.filter(e -> RegionUtils.pieceInRegion(e, map.getMapType(), moveableGroupRegion))
					.map(e -> (MoveableDynamicPiece)e)
					.toList();

			List<Position> originalPositions = moveablesInRegion
					.stream()
					.map(Piece::getPosition)
					.toList();
			
			
			List<Position> sokobanGoalsPositions = Lists.newArrayList();
			
			String sokobanGoalsX = moveableGroupRegion.getProperties().get("sokobanGoalsX");
			String sokobanGoalsY = moveableGroupRegion.getProperties().get("sokobanGoalsY");
			
			if (sokobanGoalsX != null && sokobanGoalsY != null) {
				List<Integer> positionsX = Arrays.stream(sokobanGoalsX.split(",")).map(Integer::valueOf).toList();
				List<Integer> positionsY = Arrays.stream(sokobanGoalsY.split(",")).map(Integer::valueOf).toList();
				for (int i = 0; i < positionsX.size(); i++) {
					int posX = positionsX.get(i);
					int posY = positionsY.get(i);
					sokobanGoalsPositions.add(Position.fromCoordinates(posX, posY, map.getMapType()));
				}

			}
			
			List<SecretDoor> secretDoorsInRegion = map
					.getDynamicPieces()
					.values()
					.stream()
					.filter(e -> (e instanceof SecretDoor))
					.filter(e -> RegionUtils.pieceInRegion(e, map.getMapType(), moveableGroupRegion))
					.map(e -> (SecretDoor)e)
					.toList();

			
			int x = Integer.parseInt(moveableGroupRegion.getProperties().get("playerStartPosX"));
			int y = Integer.parseInt(moveableGroupRegion.getProperties().get("playerStartPosY"));
			Position playerStartPosition = Position.fromCoordinates(x, y, map.getMapType());

			String resetableValue = (String)moveableGroupRegion.getProperties().get("resetable");
			boolean resetable = resetableValue == null || Boolean.parseBoolean(resetableValue);
			
			MoveableGroup moveableGroup = new MoveableGroup();
			moveableGroup.setRegion(moveableGroupRegion);
			moveableGroup.getMoveables().addAll(moveablesInRegion);
			moveableGroup.getOriginalPositions().addAll(originalPositions);
			moveableGroup.getGoalPositions().addAll(sokobanGoalsPositions);
			moveableGroup.getSecretDoors().addAll(secretDoorsInRegion);
			moveableGroup.setPlayerStartPosition(playerStartPosition);
			moveableGroup.setResetable(resetable);
			map.getMoveableGroups().add(moveableGroup);
			
		}
	}

	private static void initializeCollectibles(CustomTiledMap customTiledMap, CosmodogMap map) {

		int collectiblesLayerIndex = Layers.LAYER_META_COLLECTIBLES;
		int mapWidth = map.getMapType().getWidth();
		int mapHeight = map.getMapType().getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {

				Position position = Position.fromCoordinates(k, l, map.getMapType());

				int tileId = map.getTileId(position, collectiblesLayerIndex);

				TileType collectibleTileType = TileType.getByLayerAndTileId(collectiblesLayerIndex, tileId);

				Piece piece = PieceFactory.createPieceFromTileType(collectibleTileType);

				if (piece != null) {

					piece.setPosition(position);

					if (piece instanceof Collectible) {
						Enemy enemy = map.enemyAtTile(position);
						if (enemy != null) {
							InventoryItem inventoryItem = InventoryItemFactory.createInventoryItem((Collectible)piece);
							enemy.setInventoryItem(inventoryItem);
						} else {
							map.getMapPieces().put(piece.getPosition(), piece);
						}
					} else {
						map.getMapPieces().put(piece.getPosition(), piece);
					}
				}
			}

		}
	}

	private static void initializeEffects(CustomTiledMap customTiledMap, CosmodogMap map) {
		int mapWidth = map.getMapType().getWidth();
		int mapHeight = map.getMapType().getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {

				Position position = Position.fromCoordinates(k, l, map.getMapType());

				int tileId = customTiledMap.getTileId(position, Layers.LAYER_META_EFFECTS);

				if (TileType.TELEPORT_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_TELEPORT);
					effect.setPosition(position);
					map.getEffectPieces().add(effect);
				}

				if (TileType.FIRE_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_FIRE);
					effect.setPosition(position);
					map.getEffectPieces().add(effect);
				}

				if (TileType.SMOKE_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_SMOKE);
					effect.setPosition(position);
					map.getEffectPieces().add(effect);
				}

				if (TileType.ELECTRICITY_EFFECT.getTileId() == tileId) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_ELECTRICITY);
					effect.setPosition(position);
					map.getEffectPieces().add(effect);
				}

				if (TileType.ENERGY_WALL_TILES.contains(TileType.getByLayerAndTileId(Layers.LAYER_META_EFFECTS, tileId))) {
					Effect effect = new Effect(Effect.EFFECT_TYPE_ENERGYWALL);
					effect.setPosition(position);
					map.getEffectPieces().add(effect);
				}

			}

		}
	}

	private static void initializeDynamicTiles(CustomTiledMap tiledMap, CosmodogMap map) {
		int dynamicTilesLayerIndex = Layers.LAYER_META_DYNAMIC_PIECES;
		int mapWidth = tiledMap.getWidth();
		int mapHeight = tiledMap.getHeight();

		for (int k = 0; k < mapWidth; k++) {
			for (int l = 0; l < mapHeight; l++) {

				Position position = Position.fromCoordinates(k, l, map.getMapType());

				int tileId = tiledMap.getTileId(position, dynamicTilesLayerIndex);

				if (tileId == TileType.DYNAMIC_PIECE_SENSOR.getTileId()) {
					Sensor sensor = Sensor.create(position);
					map.getDynamicPieces().put(Sensor.class, sensor);
				}

				if (tileId == TileType.DYNAMIC_PIECE_CUBE.getTileId()) {
					Cube cube = Cube.create(position, false);
					map.getDynamicPieces().put(Cube.class, cube);
				}

				if (tileId == TileType.DYNAMIC_PIECE_TRANSPARENT_CUBE.getTileId()) {
					Cube cube = Cube.create(position, true);
					map.getDynamicPieces().put(Cube.class, cube);
				}

				if (tileId == TileType.DYNAMIC_PIECE_ONE_WAY_BOLLARD_WEST.getTileId()) {
					OneWayBollard oneWayBollard = OneWayBollard.create(position, DirectionType.LEFT);
					map.getDynamicPieces().put(OneWayBollard.class, oneWayBollard);
				}

				if (tileId == TileType.DYNAMIC_PIECE_ONE_WAY_BOLLARD_NORTH.getTileId()) {
					OneWayBollard oneWayBollard = OneWayBollard.create(position, DirectionType.UP);
					map.getDynamicPieces().put(OneWayBollard.class, oneWayBollard);
				}

				if (tileId == TileType.DYNAMIC_PIECE_ONE_WAY_BOLLARD_EAST.getTileId()) {
					OneWayBollard oneWayBollard = OneWayBollard.create(position, DirectionType.RIGHT);
					map.getDynamicPieces().put(OneWayBollard.class, oneWayBollard);
				}

				if (tileId == TileType.DYNAMIC_PIECE_ONE_WAY_BOLLARD_SOUTH.getTileId()) {
					OneWayBollard oneWayBollard = OneWayBollard.create(position, DirectionType.DOWN);
					map.getDynamicPieces().put(OneWayBollard.class, oneWayBollard);
				}

				if (tileId == TileType.DYNAMIC_PIECE_REFLECTOR_NE.getTileId()) {
					Reflector reflector = Reflector.create(position, ReflectionType.NORTH_EAST);
					map.getDynamicPieces().put(Reflector.class, reflector);
				}

				if (tileId == TileType.DYNAMIC_PIECE_REFLECTOR_NW.getTileId()) {
					Reflector reflector = Reflector.create(position, ReflectionType.NORTH_WEST);
					map.getDynamicPieces().put(Reflector.class, reflector);
				}

				if (tileId == TileType.DYNAMIC_PIECE_REFLECTOR_SE.getTileId()) {
					Reflector reflector = Reflector.create(position, ReflectionType.SOUTH_EAST);
					map.getDynamicPieces().put(Reflector.class, reflector);
				}

				if (tileId == TileType.DYNAMIC_PIECE_REFLECTOR_SW.getTileId()) {
					Reflector reflector = Reflector.create(position, ReflectionType.SOUTH_WEST);
					map.getDynamicPieces().put(Reflector.class, reflector);
				}

				if (tileId == TileType.DYNAMIC_PIECE_JAMMER.getTileId()) {
					Jammer jammer = Jammer.create(position);
					map.getDynamicPieces().put(Jammer.class, jammer);
				}

                if (tileId == TileType.DYNAMIC_PIECE_BOLLARD_RISEN.getTileId()) {
                    Bollard bollard = Bollard.create(position, false);
                    map.getDynamicPieces().put(Bollard.class, bollard);
                }

                if (tileId == TileType.DYNAMIC_PIECE_BOLLARD_SUNK.getTileId()) {
                    Bollard bollard = Bollard.create(position, true);
                    map.getDynamicPieces().put(Bollard.class, bollard);
                }

				if (tileId == TileType.DYNAMIC_PIECE_AUTOBOLLARD.getTileId()) {
					AutoBollard autoBollard = AutoBollard.create(position);
					map.getDynamicPieces().put(AutoBollard.class, autoBollard);
				}

                if (tileId == TileType.DYNAMIC_PIECE_SWITCH.getTileId()) {
                    Switch aSwitch = Switch.createInstance(position);
                    map.getDynamicPieces().put(Switch.class, aSwitch);
                }

				if (tileId == TileType.DYNAMIC_PIECE_MOVEABLE_BLOCK.getTileId()) {
					Block block = Block.create(position);
					block.setStil(Block.STIL_BLOCK);
					map.getDynamicPieces().put(Block.class, block);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_MOVEABLE_CONTAINER.getTileId()) {
					Block block = Block.create(position);
					block.setStil(Block.STIL_CONTAINER);
					map.getDynamicPieces().put(Block.class, block);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_MOVEABLE_ICE.getTileId()) {
					Block block = Block.create(position);
					block.setStil(Block.STIL_ICE);
					map.getDynamicPieces().put(Block.class, block);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_MOVEABLE_PLANT.getTileId()) {
					Block block = Block.create(position);
					block.setStil(Block.STIL_PLANT);
					map.getDynamicPieces().put(Block.class, block);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_GUIDETERMINAL.getTileId()) {
					Terminal terminal = Terminal.create(position);
					map.getDynamicPieces().put(Terminal.class, terminal);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_STONE.getTileId()) {
					Stone stone = Stone.create(position);
					map.getDynamicPieces().put(Stone.class, stone);
				}

				if (tileId == TileType.DYNAMIC_PIECE_HARDSTONE.getTileId()) {
					HardStone hardStone = HardStone.create(position);
					map.getDynamicPieces().put(HardStone.class, hardStone);
				}

				if (tileId == TileType.DYNAMIC_PIECE_TREE.getTileId()) {
					Tree tree = Tree.create(position);
					map.getDynamicPieces().put(Tree.class, tree);
				}

				if (tileId == TileType.DYNAMIC_PIECE_BAMBOO.getTileId()) {
					Bamboo bamboo = Bamboo.create(position);
					map.getDynamicPieces().put(Bamboo.class, bamboo);
				}

				if (tileId == TileType.DYNAMIC_PIECE_CRUMBLED_WALL_MONTAIN.getTileId()) {
					CrumbledWall wall = CrumbledWall.create(position, CrumbledWall.SHAPE_MONTAIN);
					map.getDynamicPieces().put(CrumbledWall.class, wall);
				}

				if (tileId == TileType.DYNAMIC_PIECE_BINARY_INDICATOR_ALIEN_BASE.getTileId()) {
					BinaryIndicator binaryIndicator = BinaryIndicator.create(position);
					map.getDynamicPieces().put(BinaryIndicator.class, binaryIndicator);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_CRUMBLED_WALL_ALIEN_BASE.getTileId()) {
					CrumbledWall wall = CrumbledWall.create(position, CrumbledWall.SHAPE_ALIEN_BASE);
					map.getDynamicPieces().put(CrumbledWall.class, wall);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_GATE.getTileId()) {
					Gate gate = Gate.create(position);
					map.getDynamicPieces().put(Gate.class, gate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_SECRET_DOOR_SPIKES.getTileId()) {
					SecretDoor secretDoor = SecretDoor.create(position);
					secretDoor.setStil(SecretDoor.STIL_SPIKES);
					map.getDynamicPieces().put(SecretDoor.class, secretDoor);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_SECRET_DOOR_HYDRAULICS.getTileId()) {
					SecretDoor secretDoor = SecretDoor.create(position);
					secretDoor.setStil(SecretDoor.STIL_HYDRAULICS);
					map.getDynamicPieces().put(SecretDoor.class, secretDoor);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_SECRET_DOOR_ENERGY.getTileId()) {
					SecretDoor secretDoor = SecretDoor.create(position);
					secretDoor.setStil(SecretDoor.STIL_ENERGY);
					map.getDynamicPieces().put(SecretDoor.class, secretDoor);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_SECRET_DOOR_WALL.getTileId()) {
					SecretDoor secretDoor = SecretDoor.create(position);
					secretDoor.setStil(SecretDoor.STIL_WALL);
					map.getDynamicPieces().put(SecretDoor.class, secretDoor);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_CRATE.getTileId()) {
					Crate crate = Crate.create(position);
					map.getDynamicPieces().put(Crate.class, crate);
				}

				if (tileId == TileType.DYNAMIC_PIECE_MINE.getTileId()) {
					Mine mine = Mine.create(position);
					map.getDynamicPieces().put(Mine.class, mine);
				}

				if (tileId == TileType.DYNAMIC_PIECE_POISON.getTileId()) {
					Poison poison = Poison.create(position);
					map.getDynamicPieces().put(Poison.class, poison);
				}

				if (tileId == TileType.DYNAMIC_PIECE_PRESSUREBUTTON.getTileId()) {
					PressureButton pressureButton = PressureButton.create(position);
					map.getDynamicPieces().put(PressureButton.class, pressureButton);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_ALIEN_BASE_BLOCKADE.getTileId()) {
					AlienBaseBlockade alienBaseBlockade = AlienBaseBlockade.create(Position.fromCoordinates(k, l, map.getMapType()));
					map.getDynamicPieces().put(AlienBaseBlockade.class, alienBaseBlockade);
				}

				if (TileType.MIN_ALIEN_DOOR.getTileId() <= tileId && tileId <= TileType.MAX_ALIEN_DOOR.getTileId()) {
					DoorType doorType = Mappings.TILE_ID_TO_DOOR_TYPE.get(tileId);
					DoorAppearanceType doorAppearanceType = Mappings.TILE_ID_TO_DOOR_APPEARANCE_TYPE.get(tileId);
					DirectionType directionType = Mappings.TILE_ID_TO_DOOR_DIRECTION_TYPE.get(tileId);
					Door door = new Door(directionType, doorType, doorAppearanceType);
					door.setPosition(position);
					map.getDynamicPieces().put(Door.class, door);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_1.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[0]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_2.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[1]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_3.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[2]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_4.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[3]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_5.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[4]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_6.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[5]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_7.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[6]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_8.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[7]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
				if (tileId == TileType.DYNAMIC_PIECE_LETTERPLATE_9.getTileId()) {
					LetterPlate letterPlate = LetterPlate.create(position, LetterPlate.ALPHABETHS[8]);
					map.getDynamicPieces().put(LetterPlate.class, letterPlate);
				}
				
			}
		}
	}

	private static void initializeSwitchableAndActivatableConnectors(CosmodogMap map) {
		CustomTiledMap tiledMap = map.getCustomTiledMap();
		TiledObjectGroup connectorsGroup = map.getCustomTiledMap().getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_SWITCHABLES_AND_ACTIVATABLES);

		if (connectorsGroup == null) {
			return;
		}

		List<TiledLineObject> connectors = connectorsGroup.getObjects().values().stream().map(e -> (TiledLineObject)e).toList();

		int tileLength = TileUtils.tileLengthSupplier.get();

		for (TiledLineObject connector : connectors) {
			String priorityPropertyValue = connector.getProperties().get("priority");
			int priority = (priorityPropertyValue == null) ? 0 : Integer.parseInt(priorityPropertyValue);
			TiledLineObject.Point start = connector.getPoints().get(0);
			TiledLineObject.Point end = connector.getPoints().get(1);
			Position startPosition = Position.fromCoordinates((float)((int)start.x / tileLength), (float)((int)start.y / tileLength), map.getMapType());
			Position endPosition = Position.fromCoordinates((float)((int)end.x / tileLength), (float)((int)end.y / tileLength), map.getMapType());
			Optional<SwitchableHolder> optSwitchableHolder = map.switchableHolderAtPosition(startPosition);
			if (optSwitchableHolder.isPresent()) {
				Optional<Switchable> optSwitchable = map.switchableAtPosition(endPosition);
				if (optSwitchable.isPresent()) {
					optSwitchableHolder.get().addSwitchable(priority, optSwitchable.get());
				}
			} else {
				Optional<ActivatableHolder> optActivatableHolder = map.activatableHolderAtPosition(startPosition);
				if (optActivatableHolder.isPresent()) {
					Optional<Activatable> optActivatable = map.activatableAtPosition(endPosition);
					if (optActivatable.isPresent()) {
						optActivatableHolder.get().addActivatable(priority, optActivatable.get());
					}
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

		//Space lift rules
		for (MapType mapType : MapType.values()) {

			CosmodogMap map = cosmodogGame.getMaps().get(mapType);

			TiledObjectGroup liftObjectGroup = map.getCustomTiledMap().getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_LIFTS);

			if (liftObjectGroup == null) {
				continue;
			}

			List<TiledObject> liftRegions = liftObjectGroup.getObjects().values().stream().toList();

			for (TiledObject liftRegion : liftRegions) {
				RuleTrigger enterLiftTrigger = new EnteringRegionTrigger(mapType, ObjectGroups.OBJECT_GROUP_ID_LIFTS, liftRegion.getName());
				SpaceLiftAction spaceLiftAction = new SpaceLiftAction(mapType == MapType.MAIN);
				RuleAction changePositionAction = new AsyncActionRegistrationRuleAction(AsyncActionType.SPACE_LIFT, spaceLiftAction, false);
				RuleAction composedAction = new BlockAction(changePositionAction);
				Rule spaceliftAtCosmodromRule = new Rule("SpaceLiftRule_" + mapType + "." + liftRegion.getName(), Lists.newArrayList(GameEventChangedPosition.class), enterLiftTrigger, composedAction, Rule.RULE_PRIORITY_LATEST);
				ruleBook.put(spaceliftAtCosmodromRule.getId(), spaceliftAtCosmodromRule);
			}
		}
		// Region dependent dialog rules.
		ruleBuilder = new RegionDependentDialogRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Region dependent popup rules (e.g. tutorial messages)
		ruleBuilder = new RegionDependentPopupRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Multiinstance piece collection dialog rules.
		ruleBuilder = new MultiInstancePieceRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Item notification rules.
		ruleBuilder = new ItemNotificationRuleBuilder();
		ruleResourceWrappers = ruleBuilder.build();
		for (String s : ruleResourceWrappers.keySet()) {
			ruleBook.put(s, ruleResourceWrappers.get(s).getEntity());
		}

		// Tutorials at the beginning of the game.
		List<RuleAction> tutorialActions = Lists.newArrayList();

		for (int i = 0; i < TutorialUtils.INITIAL_TUTORIAL_TEXTS.size(); i++) {
			RuleAction pauseAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, new PauseAction(500));
			pauseAction = new FeatureBoundAction(Features.FEATURE_TUTORIAL, pauseAction);
			tutorialActions.add(pauseAction);
			String tutorialText = TutorialUtils.INITIAL_TUTORIAL_TEXTS.get(i);
			AsyncAction tutorialAsyncAction = new PopUpNotificationAction(tutorialText);
			RuleAction tutorialRegistrationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, tutorialAsyncAction);
			tutorialRegistrationAction = new FeatureBoundAction(Features.FEATURE_TUTORIAL, tutorialRegistrationAction);
			tutorialActions.add(tutorialRegistrationAction);
		}
		List<RuleAction> atTheBeginningActions = Lists.newArrayList();
		atTheBeginningActions.add(new SetGameProgressPropertyAction(GameProgress.GAME_PROGRESS_PROPERTY_AFTERLANDING, "true"));
		atTheBeginningActions.addAll(tutorialActions);
		Rule rule = new Rule(Rule.RULE_DIALOG_AFTER_LANDING, new NewGameTrigger(), BlockAction.block(atTheBeginningActions));
		ruleBook.put(rule.getId(), rule);

		// Winning rule
		rule = new Rule(Rule.RULE_WINNING, Lists.newArrayList(GameEventEndedTurn.class), new GameProgressWinningConditionTrigger(), new WinningAction(), Rule.RULE_PRIORITY_EARLIEST);
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
			
			//Trigger: Not yet deactivated AND entered region AND having deactivation codes.
			RuleTrigger deactivateMinesForQuadrandTrigger = new GameProgressPropertyTrigger("MinesDeactivatedForQuadrand" + quadrandType, "false");
			deactivateMinesForQuadrandTrigger = AndTrigger.and(new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "DeactivateMines" + quadrandType), deactivateMinesForQuadrandTrigger);
			deactivateMinesForQuadrandTrigger = AndTrigger.and(new InventoryBasedTrigger(InventoryItemType.MINEDEACTIVATIONCODES, 1), deactivateMinesForQuadrandTrigger);
			//Action: Deactivate mines AND set deactivated property to true AND print notification.
			AsyncAction asyncAction = new PopUpNotificationAction("The console controls the land mines in the quadrand " + quadrandType.getRepresentation() + ". You deactivate the mines.");
			RuleAction notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
			RuleAction deactivateMinesAction = new DeactivateMinesAction(quadrandType);
			deactivateMinesAction = BlockAction.block(deactivateMinesAction, new SetGameProgressPropertyAction("MinesDeactivatedForQuadrand" + quadrandType, "true"), notificationAction);
			//Create rule
			rule = new Rule(deactivateMinesAction.getClass().getSimpleName() + ":" + quadrandType, Lists.newArrayList(GameEventChangedPosition.class), deactivateMinesForQuadrandTrigger, deactivateMinesAction, Rule.RULE_PRIORITY_LATEST);
			ruleBook.put(rule.getId(), rule);
			
			//Trigger: Not yet deactivated AND entered region AND NOT having deactivation codes.
			deactivateMinesForQuadrandTrigger = new GameProgressPropertyTrigger("MinesDeactivatedForQuadrand" + quadrandType, "false");
			deactivateMinesForQuadrandTrigger = AndTrigger.and(new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "DeactivateMines" + quadrandType), deactivateMinesForQuadrandTrigger);
			RuleTrigger notHavingDeactivationCodesTrigger = new InventoryBasedTrigger(InventoryItemType.MINEDEACTIVATIONCODES, 1);
			notHavingDeactivationCodesTrigger = InvertedTrigger.not(notHavingDeactivationCodesTrigger);
			deactivateMinesForQuadrandTrigger = AndTrigger.and(notHavingDeactivationCodesTrigger, deactivateMinesForQuadrandTrigger);
			//Action: print notification that deactivation codes are missing.
			asyncAction = new PopUpNotificationAction("The console controls the land mines in the quadrand " + quadrandType.getRepresentation() + ". Unfortunately, you do not have the deactivation codes.");
			notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
			//Create rule
			rule = new Rule(deactivateMinesAction.getClass().getSimpleName() + ":" + quadrandType + "_NoDeactivationCodes", Lists.newArrayList(GameEventChangedPosition.class), deactivateMinesForQuadrandTrigger, notificationAction, Rule.RULE_PRIORITY_LATEST);
			ruleBook.put(rule.getId(), rule);

		}

		// Worm delay rules
		RuleTrigger switchOnVentilationTrigger = new GameProgressPropertyTrigger("WormAreaVentilationOn", "false");
		switchOnVentilationTrigger = AndTrigger.and(new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "SwitchOnVentilation"), switchOnVentilationTrigger);
		AsyncAction asyncAction = new PopUpNotificationAction("This is the control panel for the ventilation. You activate it. The worm will have harder time to locate you.");
		RuleAction notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
		RuleAction switchOnVentilationAction = new SwitchOnVentilationToDelayWormAction();
		switchOnVentilationAction = BlockAction.block(PlaySoundRuleAction.fromSoundResource(SoundResources.SOUND_CONSOLE), switchOnVentilationAction, new SetGameProgressPropertyAction("WormAreaVentilationOn", "true"), notificationAction);
		rule = new Rule(Rule.RULE_WORM_DELAY_PHASE2, Lists.newArrayList(GameEventChangedPosition.class), switchOnVentilationTrigger, switchOnVentilationAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);
		
		RuleTrigger switchOnSewageTrigger = new GameProgressPropertyTrigger("WormAreaSewageOn", "false");
		switchOnSewageTrigger = AndTrigger.and(new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "SwitchOnSewage"), switchOnSewageTrigger);
		asyncAction = new PopUpNotificationAction("This is the control panel for the sewage. You activate it. The worm will have even harder time to locate you.");
		notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
		RuleAction switchOnSewageAction = new SwitchOnSewageToDelayWormAction();
		switchOnSewageAction = BlockAction.block(PlaySoundRuleAction.fromSoundResource(SoundResources.SOUND_CONSOLE), switchOnSewageAction, new SetGameProgressPropertyAction("WormAreaSewageOn", "true"), notificationAction);
		rule = new Rule(Rule.RULE_WORM_DELAY_PHASE3, Lists.newArrayList(GameEventChangedPosition.class), switchOnSewageTrigger, switchOnSewageAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);
		
		RuleTrigger switchOnDrillsTrigger = new GameProgressPropertyTrigger("WormAreaDrillOn", "false");
		switchOnDrillsTrigger = AndTrigger.and(new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "SwitchOnDrills"), switchOnDrillsTrigger);
		asyncAction = new PopUpNotificationAction("This is the control panel for the underground drill machines. You activate it. The worm cannot locate you anymore.");
		notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
		RuleAction switchOnDrillAction = new DeactivateWormAction();
		switchOnDrillAction = BlockAction.block(PlaySoundRuleAction.fromSoundResource(SoundResources.SOUND_CONSOLE), switchOnDrillAction, new SetGameProgressPropertyAction("WormAreaDrillOn", "true"), notificationAction);
		rule = new Rule(Rule.RULE_WORM_DELAY_PHASE4, Lists.newArrayList(GameEventChangedPosition.class), switchOnDrillsTrigger, switchOnDrillAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);

		//Pickup blue keycard rule.
		RuleTrigger approachBlueKeyCardTrigger = new GameProgressPropertyTrigger("CollectedBlueKeyCard", "false");
		approachBlueKeyCardTrigger = AndTrigger.and(new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "RegionWithBlueKeyCard"), approachBlueKeyCardTrigger);
		asyncAction = new PopUpNotificationAction("There is something shiny on the shelf. You grab the item. It is a key card for the mass hall (blue).");
		notificationAction = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
		RuleAction pickUpBlueKeyCardAction = new PickupKeyAction(DoorType.blueKeycardDoor);
		pickUpBlueKeyCardAction = BlockAction.block(pickUpBlueKeyCardAction, new SetGameProgressPropertyAction("CollectedBlueKeyCard", "true"), notificationAction);
		rule = new Rule(Rule.RULE_PICK_UP_BLUE_KEYCARD, Lists.newArrayList(GameEventChangedPosition.class), approachBlueKeyCardTrigger, pickUpBlueKeyCardAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);
		
		//Open gate rule.
		RuleTrigger openGateTrigger = OrTrigger.or(
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "AlienBaseGateSwitch1"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "AlienBaseGateSwitch2"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "AlienBaseGateSwitch3"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "AlienBaseGateSwitch4"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "AlienBaseGateSwitch5")
				);
		
		RuleAction updateAlienBaseGateSequenceAction = new UpdateAlienBaseGateSequenceAction();
		rule = new Rule(Rule.RULE_OPEN_GATE_TO_LAUNCH_POD, Lists.newArrayList(GameEventChangedPosition.class), openGateTrigger, updateAlienBaseGateSequenceAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);

		RuleTrigger activateTeleportTrigger = OrTrigger.or(
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "TeleportConsole1"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "TeleportConsole2"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "TeleportConsole3"),
				new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "TeleportConsole4")
				);
		
		RuleAction updateTeleportSequenceAction = new UpdateAlienBaseTeleportSequenceAction();
		rule = new Rule(Rule.RULE_ACTIVATE_TELEPORT, Lists.newArrayList(GameEventChangedPosition.class), activateTeleportTrigger, updateTeleportSequenceAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);
		
		
		// Last boss damage.
		RuleTrigger damageLastBossTrigger = new EnteringRegionTrigger(MapType.MAIN, ObjectGroups.OBJECT_GROUP_ID_REGIONS, "LastBossConsole");
		RuleAction damageLastBossAction = new DamageLastBossAction();
		rule = new Rule(Rule.RULE_DAMAGE_LAST_BOSS, Lists.newArrayList(GameEventChangedPosition.class), damageLastBossTrigger, damageLastBossAction, Rule.RULE_PRIORITY_LATEST);
		ruleBook.put(rule.getId(), rule);

		// Secret found
		for (MapType mapType : MapType.values()) {
			CosmodogMap map = cosmodogGame.getMaps().get(mapType);
			TiledObjectGroup secretsObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_SECRETS);
			Map<String, TiledObject> secretObjects = secretsObjectGroup.getObjects();
			for (String secretObjectKey : secretObjects.keySet()) {
				RuleTrigger secretEntranceTrigger = new EnteringRegionTrigger(mapType, ObjectGroups.OBJECT_GROUP_SECRETS, secretObjectKey);
				secretEntranceTrigger = AndTrigger.and(secretEntranceTrigger, new GameProgressPropertyTrigger("SecretCollected." + secretObjectKey, "false"));

				RuleAction action = new SetGameProgressPropertyAction("SecretCollected." + secretObjectKey, "true");
				asyncAction = new OnScreenNotificationAction("Secret found", 1500, SoundResources.SOUND_SECRET_FOUND);
				RuleAction updateGameProgress = new AbstractRuleAction() {

					private static final long serialVersionUID = 3465808157964130895L;

					@Override
					public void execute(GameEvent event) {
						Player player = ApplicationContextUtils.getPlayer();
						player.getGameProgress().increaseNumberOfFoundSecrets();
					}
				};
				action = BlockAction.block(new AsyncActionRegistrationRuleAction(AsyncActionType.ONSCREEN_NOTIFICATION, asyncAction, false), updateGameProgress, action);
				rule = new Rule(Rule.RULE_FOUND_SECRET + "." + secretObjectKey, Lists.newArrayList(GameEventChangedPosition.class), secretEntranceTrigger, action, Rule.RULE_PRIORITY_LATEST);
				ruleBook.put(rule.getId(), rule);
			}
		}
		//Check sokoban solution rule
		RuleTrigger sokobanSolutionCheckTrigger = new TrueTrigger();
		RuleAction sokobanSolutionAction = new OperateSecretDoorsInSokobanAction();
		rule = new Rule(Rule.RULE_OPERATE_SECRET_DOORS_IN_SOKOBAN, Lists.newArrayList(GameEventChangedPosition.class, GameEventTeleported.class), sokobanSolutionCheckTrigger, sokobanSolutionAction, Rule.RULE_PRIORITY_LATEST);
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

				Position position = Position.fromCoordinates(k, l, map.getMapType());

				int tileId = customTiledMap.getTileId(position, enemyLayerIndex);

				// Will be null in most cases.
				UnitType unitType = Mappings.ENEMY_TILE_TYPE_TO_UNIT_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_NPC, tileId));

				if (unitType != null) {
					EnemyFactory enemyFactory = enemyFactories.get(unitType);
					Enemy enemy = enemyFactory.buildEnemy();

					enemy.setPosition(position);

					// Check if the enemy stands on one of a home regions and
					// assign it to him, if this is the case.
					// Take note: Assuming no overlapped home regions.
					Map<String, TiledObject> homeRegions = customTiledMap.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_NPC_HOME_REGIONS).getObjects();
					for (TiledObject homeRegion : homeRegions.values()) {

						int x = k * customTiledMap.getTileWidth();
						int y = l * customTiledMap.getTileHeight();
						int w = customTiledMap.getTileWidth();
						int h = customTiledMap.getTileHeight();

						PlacedRectangle r = PlacedRectangle.fromAnchorAndSize(x, y, w, h, enemy.getPosition().getMapType());

						boolean intersects = CollisionUtils.intersects(r, enemy.getPosition().getMapType(), homeRegion);

						if (intersects) {
							enemy.setHomeRegionName(homeRegion.getName());
						}

					}

					enemy.setDirection(DirectionType.random());

					map.getEnemies().add(enemy);
				}

			}
		}

	}

	private static Map<UnitType, EnemyFactory> initializeEnemyFactories() {
		ResourceWrapperBuilder<EnemyFactory> enemyFactoryBuilder = new JsonBasedEnemyFactoryBuilder();
		Map<String, GenericResourceWrapper<EnemyFactory>> enemyFactories = enemyFactoryBuilder.build();
		Map<UnitType, EnemyFactory> retVal = Maps.newHashMap();
		for (String key : enemyFactories.keySet()) {
			retVal.put(UnitType.valueOf(key), enemyFactories.get(key).getEntity());
		}
		return retVal;
	}

}
