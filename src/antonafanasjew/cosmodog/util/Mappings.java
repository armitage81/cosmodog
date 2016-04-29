package antonafanasjew.cosmodog.util;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.NpcActionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.PlayerAppearanceType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class Mappings {

	/**
	 * Map of inventory item types to their animations in the inventory.
	 */
	public static final Map<InventoryItemType, String> INVENTORY_ITEM_TYPE_TO_ANIMATION_ID = Maps.newHashMap();
	
	static {
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.FUEL_TANK, "inventoryItemFuel");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.INSIGHT, "inventoryItemCosmodog");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.BOAT, "inventoryItemBoat");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.DYNAMITE, "inventoryItemDynamite");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.VEHICLE, "inventoryItemVehicle");
	}
	
	/**
	 * Map of enemy tile ids on the map to the type of enemy that should be initialized on map.
	 */
	public static final Map<TileType, UnitType> ENEMY_TILE_TYPE_TO_UNIT_TYPE = Maps.newHashMap();
	
	static {
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_LIGHTTANK, UnitType.LIGHT_TANK);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_ROBOT, UnitType.ROBOT);
		ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_DRONE, UnitType.DRONE);
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
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.ONBOAT, "OnBoat");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.INHIGHGRASS, "InGrass");
		ANIMATION_KEY_BY_PLAYER_APPEARANCE_TYPE.put(PlayerAppearanceType.ISTELEPORTING, "Teleporting");
		
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
	public static List<TileType> TILE_TYPES_TO_BE_ANIMATED = Lists.newArrayList();
	
	static {
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_CENTER);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_NW);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_N);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_NE);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_SE);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_S);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_SE);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_N_AND_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_N_AND_W);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_S_AND_E);
		TILE_TYPES_TO_BE_ANIMATED.add(TileType.WATER_BORDER_S_AND_W);
		
	}
	
	
}
