package antonafanasjew.cosmodog.util;

import java.util.Map;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.NpcActionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.PlayerAppearanceType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Tiles;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class Mappings {

	public static final Map<InventoryItemType, String> INVENTORY_ITEM_TYPE_TO_ANIMATION_ID = Maps.newHashMap();
	
	static {
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.FUEL_TANK, "inventoryItemFuel");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.INSIGHT, "inventoryItemCosmodog");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.BOAT, "inventoryItemBoat");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.DYNAMITE, "inventoryItemDynamite");
		INVENTORY_ITEM_TYPE_TO_ANIMATION_ID.put(InventoryItemType.VEHICLE, "inventoryItemVehicle");
	}
	
	public static final Map<Integer, UnitType> ENEMY_TILE_ID_TO_UNIT_TYPE = Maps.newHashMap();
	
	static {
		ENEMY_TILE_ID_TO_UNIT_TYPE.put(Tiles.META_ENEMY_TILE_LIGHTTANK, UnitType.LIGHT_TANK);
		ENEMY_TILE_ID_TO_UNIT_TYPE.put(Tiles.META_ENEMY_TILE_ROBOT, UnitType.ROBOT);
		ENEMY_TILE_ID_TO_UNIT_TYPE.put(Tiles.META_ENEMY_TILE_DRONE, UnitType.DRONE);
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
	
	public static Map<Integer, Integer> ENERGY_WALL_COSTS = Maps.newHashMap();
	
	static  {
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_10_TILE_ID, 10);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_20_TILE_ID, 20);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_30_TILE_ID, 30);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_40_TILE_ID, 40);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_50_TILE_ID, 50);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_60_TILE_ID, 60);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_70_TILE_ID, 70);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_80_TILE_ID, 80);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_90_TILE_ID, 90);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_100_TILE_ID, 100);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_150_TILE_ID, 150);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_200_TILE_ID, 200);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_250_TILE_ID, 250);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_300_TILE_ID, 300);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_350_TILE_ID, 350);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_500_TILE_ID, 500);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_1000_TILE_ID, 1000);
		ENERGY_WALL_COSTS.put(Tiles.ENERGYWALL_EFFECT_10000_TILE_ID, 10000);
	}
	
	
	public static Map<Integer, WeaponType> COLLECTIBLE_WEAPON_TILE_ID_2_WEAPON_TYPE = Maps.newHashMap();
	
	static {
		COLLECTIBLE_WEAPON_TILE_ID_2_WEAPON_TYPE.put(Tiles.WEAPONS_PISTOL_TILE_ID, WeaponType.PISTOL);
		COLLECTIBLE_WEAPON_TILE_ID_2_WEAPON_TYPE.put(Tiles.WEAPONS_SHOTGUN_TILE_ID, WeaponType.SHOTGUN);
		COLLECTIBLE_WEAPON_TILE_ID_2_WEAPON_TYPE.put(Tiles.WEAPONS_RIFLE_TILE_ID, WeaponType.RIFLE);
		COLLECTIBLE_WEAPON_TILE_ID_2_WEAPON_TYPE.put(Tiles.WEAPONS_MACHINEGUN_TILE_ID, WeaponType.MACHINEGUN);
		COLLECTIBLE_WEAPON_TILE_ID_2_WEAPON_TYPE.put(Tiles.WEAPONS_ROCKETLAUNCHER_TILE_ID, WeaponType.RPG);
	}
	
	public static Map<WeaponType, String> WEAPON_TYPE_2_ANIMATION_ID = Maps.newHashMap();
	
	static {
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.PISTOL, "pistol");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.SHOTGUN, "shotgun");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.RIFLE, "rifle");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.MACHINEGUN, "machinegun");
		WEAPON_TYPE_2_ANIMATION_ID.put(WeaponType.RPG, "rpg");
	}
	
	
	public static Map<Integer, WeaponType> COLLECTIBLE_AMMO_TILE_ID_2_WEAPON_TYPE = Maps.newHashMap();
	
	static {
		COLLECTIBLE_AMMO_TILE_ID_2_WEAPON_TYPE.put(Tiles.AMMO_PISTOL_TILE_ID, WeaponType.PISTOL);
		COLLECTIBLE_AMMO_TILE_ID_2_WEAPON_TYPE.put(Tiles.AMMO_SHOTGUN_TILE_ID, WeaponType.SHOTGUN);
		COLLECTIBLE_AMMO_TILE_ID_2_WEAPON_TYPE.put(Tiles.AMMO_RIFLE_TILE_ID, WeaponType.RIFLE);
		COLLECTIBLE_AMMO_TILE_ID_2_WEAPON_TYPE.put(Tiles.AMMO_MACHINEGUN_TILE_ID, WeaponType.MACHINEGUN);
		COLLECTIBLE_AMMO_TILE_ID_2_WEAPON_TYPE.put(Tiles.AMMO_ROCKETLAUNCHER_TILE_ID, WeaponType.RPG);
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
	
	
	
	
	
	
	
	
	
	
	
	
}
