package antonafanasjew.cosmodog.util;

import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.NpcActionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.PlayerAppearanceType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorAppearanceType;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Mappings {

	public static Map<TileType, ToolType> MAP_TILE_TO_TOOL_TYPE = Maps.newHashMap();
	
	static {
		MAP_TILE_TO_TOOL_TYPE.put(TileType.BOAT, ToolType.boat);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.DYNAMITE, ToolType.dynamite);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.GEIGERZAEHLER, ToolType.geigerzaehler);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.SUPPLYTRACKER, ToolType.supplytracker);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.BINOCULARS, ToolType.binoculars);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.JACKET, ToolType.jacket);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.ANTIDOTE, ToolType.antidote);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.MINEDETECTOR, ToolType.minedetector);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.SKI, ToolType.ski);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.PICK, ToolType.pick);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.AXE, ToolType.axe);
		MAP_TILE_TO_TOOL_TYPE.put(TileType.MACHETE, ToolType.machete);
	}
	
	public static Map<TileType, GoodieType> MAP_TILE_TO_GOODIE_TYPE = Maps.newHashMap();
	
	static {
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.INFOBIT, GoodieType.infobit);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.INFOBYTE, GoodieType.infobyte);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.INFOBANK, GoodieType.infobank);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.INSIGHT, GoodieType.insight);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.COGNITION, GoodieType.cognition);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.SOFTWARE, GoodieType.software);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.CHART, GoodieType.chart);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.SUPPLIES, GoodieType.supplies);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.MEDIPACK, GoodieType.medipack);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.SOULESSENCE, GoodieType.soulessence);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.ARMOR, GoodieType.armor);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.BOTTLE, GoodieType.bottle);
		MAP_TILE_TO_GOODIE_TYPE.put(TileType.FOOD_COMPARTMENT, GoodieType.foodcompartment);
	}
	
	public static final Map<Integer, DoorType> TILE_ID_TO_KEY_TYPE = Maps.newHashMap();
	
	static {
		TILE_ID_TO_KEY_TYPE.put(4510, DoorType.redKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4511, DoorType.lilaKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4512, DoorType.blueKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4513, DoorType.cyanKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4514, DoorType.greenKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4515, DoorType.yellowKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4516, DoorType.brownKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4517, DoorType.purpleKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4518, DoorType.darkblueKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4519, DoorType.whiteKeycardDoor);
		TILE_ID_TO_KEY_TYPE.put(4520, DoorType.yellowKeyDoor);
		TILE_ID_TO_KEY_TYPE.put(4521, DoorType.greenKeyDoor);
		TILE_ID_TO_KEY_TYPE.put(4522, DoorType.blueKeyDoor);
		TILE_ID_TO_KEY_TYPE.put(4523, DoorType.redKeyDoor);
		TILE_ID_TO_KEY_TYPE.put(4524, DoorType.blackKeyDoor);
		TILE_ID_TO_KEY_TYPE.put(4525, null);
		TILE_ID_TO_KEY_TYPE.put(4526, null);
		TILE_ID_TO_KEY_TYPE.put(4527, null);
	}
	
	public static final Map<Integer, DoorType> TILE_ID_TO_DOOR_TYPE = Maps.newHashMap();
	
	static {
		TILE_ID_TO_DOOR_TYPE.put(4474, DoorType.redKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4475, DoorType.lilaKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4476, DoorType.blueKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4477, DoorType.cyanKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4478, DoorType.greenKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4479, DoorType.yellowKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4480, DoorType.brownKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4481, DoorType.purpleKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4482, DoorType.darkblueKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4483, DoorType.whiteKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4484, DoorType.yellowKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4485, DoorType.greenKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4486, DoorType.blueKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4487, DoorType.redKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4488, DoorType.blackKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4489, null);
		TILE_ID_TO_DOOR_TYPE.put(4490, null);
		TILE_ID_TO_DOOR_TYPE.put(4491, null);
		
		TILE_ID_TO_DOOR_TYPE.put(4492, DoorType.redKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4493, DoorType.lilaKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4494, DoorType.blueKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4495, DoorType.cyanKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4496, DoorType.greenKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4497, DoorType.yellowKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4498, DoorType.brownKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4499, DoorType.purpleKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4500, DoorType.darkblueKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4501, DoorType.whiteKeycardDoor);
		TILE_ID_TO_DOOR_TYPE.put(4502, DoorType.yellowKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4503, DoorType.greenKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4504, DoorType.blueKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4505, DoorType.redKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4506, DoorType.blackKeyDoor);
		TILE_ID_TO_DOOR_TYPE.put(4507, null);
		TILE_ID_TO_DOOR_TYPE.put(4508, null);
		TILE_ID_TO_DOOR_TYPE.put(4509, null);
		
		
	}
	
	public static final Map<Integer, DoorAppearanceType> TILE_ID_TO_DOOR_APPEARANCE_TYPE = Maps.newHashMap();

	static {
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4474, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4475, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4476, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4477, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4478, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4479, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4480, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4481, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4482, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4483, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4484, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4485, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4486, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4487, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4488, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4489, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4490, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4491, DoorAppearanceType.AlienDoor);
		
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4492, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4493, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4494, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4495, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4496, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4497, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4498, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4499, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4500, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4501, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4502, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4503, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4504, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4505, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4506, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4507, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4508, DoorAppearanceType.AlienDoor);
		TILE_ID_TO_DOOR_APPEARANCE_TYPE.put(4509, DoorAppearanceType.AlienDoor);
		
	}
	
	public static final Map<Integer, DirectionType> TILE_ID_TO_DOOR_DIRECTION_TYPE = Maps.newHashMap();
	
	static {
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4474, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4475, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4476, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4477, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4478, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4479, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4480, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4481, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4482, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4483, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4484, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4485, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4486, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4487, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4488, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4489, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4490, DirectionType.DOWN);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4491, DirectionType.DOWN);
		
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4492, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4493, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4494, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4495, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4496, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4497, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4498, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4499, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4500, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4501, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4502, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4503, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4504, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4505, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4506, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4507, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4508, DirectionType.LEFT);
		TILE_ID_TO_DOOR_DIRECTION_TYPE.put(4509, DirectionType.LEFT);
		
	}
	
	/**
	 * Map of inventory item types to their animations in the inventory.
	 */
	public static final Map<InventoryItemType, String> INVENTORY_ITEM_TYPE_TO_ANIMATION_ID = Maps.newHashMap();
	
	static {
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.FUEL_TANK, "inventoryItemFuel");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.INSIGHT, "inventoryItemCosmodog");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.SOFTWARE, "inventoryItemSoftware");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.CHART, "inventoryItemChart");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.BOAT, "inventoryItemBoat");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.DYNAMITE, "inventoryItemDynamite");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.VEHICLE, "inventoryItemVehicle");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.PLATFORM, "inventoryItemPlatform");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.GEIGERZAEHLER, "inventoryItemGeigerZaehler");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.SUPPLYTRACKER, "inventoryItemSupplyTracker");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.BINOCULARS, "inventoryItemBinoculars");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.JACKET, "inventoryItemJacket");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.ANTIDOTE, "inventoryItemAntidote");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.MINEDETECTOR, "inventoryItemMineDetector");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.SKI, "inventoryItemSki");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.PICK, "inventoryItemPick");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.MACHETE, "inventoryItemMachete");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.AXE, "inventoryItemAxe");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.KEY_RING, "inventoryItemKeyRing");
	}
	
	/**
	 * Map of enemy tile ids on the map to the type of enemy that should be initialized on map.
	 */
	public static final Map<TileType, UnitType> ENEMY_TILE_TYPE_TO_UNIT_TYPE = Maps.newHashMap();
	
	static {
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_LIGHTTANK, UnitType.LIGHT_TANK);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_ROBOT, UnitType.ROBOT);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_DRONE, UnitType.DRONE);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_TURRET, UnitType.TURRET);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_PIGRAT, UnitType.PIGRAT);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_ARTILLERY, UnitType.ARTILLERY);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_FLOATER, UnitType.FLOATER);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_CONDUCTOR, UnitType.CONDUCTOR);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_GUARDIAN, UnitType.GUARDIAN);
	}
	
	private static final Map<UnitType, String> ANIMATION_KEY_BY_UNIT_TYPE = Maps.newHashMap();
	private static final Map<DirectionType, String> ANIMATION_KEY_BY_DIRECTION_TYPE = Maps.newHashMap();
	private static final Map<NpcActionType, String> ANIMATION_KEY_BY_NPC_ACTION_TYPE = Maps.newHashMap();
	private static final Map<PlayerAppearanceType, String> ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE = Maps.newHashMap();
	private static final Map<PlayerActionType, String> ANIMATION_KEY_BY_PLAYER_ACTION_TYPE = Maps.newHashMap();
	
	static {
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.LIGHT_TANK, "Tank");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.ROBOT, "Robot");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.DRONE, "Drone");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.TURRET, "Turret");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.PIGRAT, "Pigrat");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.ARTILLERY, "Artillery");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.FLOATER, "Floater");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.CONDUCTOR, "Conductor");
		ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.GUARDIAN, "Guardian");
		
		ANIMATION_KEY_BY_DIRECTION_TYPE.put(DirectionType.UP, "Up");
		ANIMATION_KEY_BY_DIRECTION_TYPE.put(DirectionType.DOWN, "Down");
		ANIMATION_KEY_BY_DIRECTION_TYPE.put(DirectionType.LEFT, "Left");
		ANIMATION_KEY_BY_DIRECTION_TYPE.put(DirectionType.RIGHT, "Right");
		
		ANIMATION_KEY_BY_NPC_ACTION_TYPE.put(NpcActionType.INANIMATE, "Inanimated");
		ANIMATION_KEY_BY_NPC_ACTION_TYPE.put(NpcActionType.ANIMATE, "Animated");
		ANIMATION_KEY_BY_NPC_ACTION_TYPE.put(NpcActionType.SHOOTING, "Shooting");
		ANIMATION_KEY_BY_NPC_ACTION_TYPE.put(NpcActionType.EXPLODING, "Exploding");
		
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.DEFAULT, "Default");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.INVEHICLE, "InVehicle");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.INPLATFORM, "InPlatform");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.ONBOAT, "OnBoat");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.INHIGHGRASS, "InGrass");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.ISTELEPORTING, "Teleporting");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.NOFEET, "NoFeet");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.ONSKI, "OnSki");
		
		ANIMATION_KEY_BY_PLAYER_ACTION_TYPE.put(PlayerActionType.ANIMATE, "Animated");
		ANIMATION_KEY_BY_PLAYER_ACTION_TYPE.put(PlayerActionType.INANIMATE, "Inanimated");
		ANIMATION_KEY_BY_PLAYER_ACTION_TYPE.put(PlayerActionType.TAKINGDAMAGE, "Hit");
		
		
	}
	
	public static String npcAnimationId(UnitType unitType, DirectionType directionType, NpcActionType actionType) {
		return "enemy" + ANIMATION_KEY_BY_UNIT_TYPE.get(unitType) + ANIMATION_KEY_BY_NPC_ACTION_TYPE.get(actionType) + ANIMATION_KEY_BY_DIRECTION_TYPE.get(directionType);
	}
	
	public static String playerAnimationId(PlayerAppearanceType playerAppearanceType, PlayerActionType playerActionType, DirectionType directionType) {
		return "player" + ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.get(playerAppearanceType) + ANIMATION_KEY_BY_PLAYER_ACTION_TYPE.get(playerActionType) + ANIMATION_KEY_BY_DIRECTION_TYPE.get(directionType);
	}
	
	public static Map<TileType, Integer> ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS = Maps.newHashMap();
	
	static  {
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_10, 10);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_20, 20);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_30, 30);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_40, 40);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_50, 50);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_60, 60);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_70, 70);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_80, 80);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_90, 90);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_100, 100);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_150, 150);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_200, 200);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_250, 250);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_300, 300);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_350, 350);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_500, 500);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_1000, 1000);
		ENERGY_WALL_TILE_TYPE_2_INFOBIT_COSTS.put(TileType.ENERGYWALL_EFFECT_10000, 10000);
	}
	
	
	public static Map<TileType, WeaponType> COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE = Maps.newHashMap();
	
	static {
		COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.put(TileType.WEAPONS_PISTOL, WeaponType.PISTOL);
		COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.put(TileType.WEAPONS_SHOTGUN, WeaponType.SHOTGUN);
		COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.put(TileType.WEAPONS_RIFLE, WeaponType.RIFLE);
		COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.put(TileType.WEAPONS_MACHINEGUN, WeaponType.MACHINEGUN);
		COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.put(TileType.WEAPONS_ROCKETLAUNCHER, WeaponType.RPG);
	}
	
	public static Map<WeaponType, String> WEAPON_TYPE_2_ANIMATION_ID = Maps.newHashMap();
	
	static {
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.PISTOL, "pistol");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.SHOTGUN, "shotgun");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.RIFLE, "rifle");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.MACHINEGUN, "machinegun");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.RPG, "rpg");
	}
	
	
	public static Map<TileType, WeaponType> COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE = Maps.newHashMap();
	
	static {
		COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.put(TileType.AMMO_PISTOL, WeaponType.PISTOL);
		COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.put(TileType.AMMO_SHOTGUN, WeaponType.SHOTGUN);
		COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.put(TileType.AMMO_RIFLE, WeaponType.RIFLE);
		COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.put(TileType.AMMO_MACHINEGUN, WeaponType.MACHINEGUN);
		COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.put(TileType.AMMO_ROCKETLAUNCHER, WeaponType.RPG);
	}
	
	public static Map<WeaponType, String> WEAPON_TYPE_2_AMMO_ANIMATION_ID = Maps.newHashMap();
	
	static {
		WEAPON_TYPE_2_AMMO_ANIMATION_ID.put(WeaponType.PISTOL, "pistolAmmo");
		WEAPON_TYPE_2_AMMO_ANIMATION_ID.put(WeaponType.SHOTGUN, "shotgunAmmo");
		WEAPON_TYPE_2_AMMO_ANIMATION_ID.put(WeaponType.RIFLE, "rifleAmmo");
		WEAPON_TYPE_2_AMMO_ANIMATION_ID.put(WeaponType.MACHINEGUN, "machinegunAmmo");
		WEAPON_TYPE_2_AMMO_ANIMATION_ID.put(WeaponType.RPG, "rpgAmmo");
	}
	
	
	
	public static Map<WeaponType, String> WEAPON_TYPE_2_ICON_ANIMATION_ID = Maps.newHashMap();
	
	static {
		WEAPON_TYPE_2_ICON_ANIMATION_ID.put(WeaponType.PISTOL, "pistolIcon");
		WEAPON_TYPE_2_ICON_ANIMATION_ID.put(WeaponType.SHOTGUN, "shotgunIcon");
		WEAPON_TYPE_2_ICON_ANIMATION_ID.put(WeaponType.RIFLE, "rifleIcon");
		WEAPON_TYPE_2_ICON_ANIMATION_ID.put(WeaponType.MACHINEGUN, "machinegunIcon");
		WEAPON_TYPE_2_ICON_ANIMATION_ID.put(WeaponType.RPG, "rpgIcon");
	}
	
	/**
	 * Normally, each non meta tile has a corresponding tile image in the sprite sheet.
	 * For some of them, we want to draw a complete animation instead of a static image (f.i. water).
	 * This list defines such tile types.
	 * Animation ids will be calculated from the tile type enumeration names, e.g. WATER_CENTER => tile.WATER_CENTER
	 */
	public static Set<TileType> TILE_TYPES_TO_BE_ANIMATED = Sets.newHashSet();
	
	static {
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_CENTER);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_NW);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_N);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_NE);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_SE);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_SW);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_N_AND_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_N_AND_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_S_AND_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_S_AND_W);
		
		
		
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_VERT);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_HOR);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_W_AND_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_N_AND_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_E_AND_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_N_AND_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_N_S_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_N_S_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_W_E_N);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_W_E_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_W_E_N_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_PATCH);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_DELTA_N);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_DELTA_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_DELTA_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_DELTA_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_BRIDGE_VERT);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.RIVER_BRIDGE_HOR);
		
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_VERT);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_HOR);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_W_AND_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_N_AND_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_E_AND_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_N_AND_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_N_S_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_N_S_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_W_E_N);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_W_E_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_W_E_N_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_PATCH);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_DELTA_N);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_DELTA_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_DELTA_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_DELTA_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_BRIDGE_VERT);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.REV_RIVER_BRIDGE_HOR);
		
		
		
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.GRASS_OBJECT_FLOWER);
		
	}
	
	/**
	 * Defines the prefix of the animation id for the weapon animation attached to the player figure (if selected) by the weapon type.
	 */
	public static Map<WeaponType, String> WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX = Maps.newHashMap();
	
	static {
		WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.put(WeaponType.PISTOL, "Pistol");
		WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.put(WeaponType.SHOTGUN, "Shotgun");
		WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.put(WeaponType.RIFLE, "Rifle");
		WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.put(WeaponType.MACHINEGUN, "Machinegun");
		WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.put(WeaponType.RPG, "Rpg");
	}
	
	public static Map<DoorType, String> KEY_TYPE_2_ANIMATION_ID = Maps.newHashMap();
	
	static {
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.redKeycardDoor, "dynamicPieceAlienKey0");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.lilaKeycardDoor, "dynamicPieceAlienKey1");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.blueKeycardDoor, "dynamicPieceAlienKey2");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.cyanKeycardDoor, "dynamicPieceAlienKey3");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.greenKeycardDoor, "dynamicPieceAlienKey4");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.yellowKeycardDoor, "dynamicPieceAlienKey5");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.brownKeycardDoor, "dynamicPieceAlienKey6");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.purpleKeycardDoor, "dynamicPieceAlienKey7");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.darkblueKeycardDoor, "dynamicPieceAlienKey8");
		KEY_TYPE_2_ANIMATION_ID.put(DoorType.whiteKeycardDoor, "dynamicPieceAlienKey9");
	}
	
}
