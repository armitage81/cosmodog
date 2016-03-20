package antonafanasjew.cosmodog.globals;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Contains tile id constants. Note: The ids are related to the offset that is used in the application. They do not match the id's in the Pyxel tool.
 * To calculate a real id from the Pyxel tool id: Take the pyxel tool id and add 1
 */
public class Tiles {

	public static final int FIRE_EFFECT_TILE_ID = 28;
	public static final int SMOKE_EFFECT_TILE_ID = 35;
	public static final int BIRDS_EFFECT_TILE_ID = 36;
	public static final int ELECTRICITY_EFFECT_TILE_ID = 33;
	
	
	public static final int WATERPLACE_TILE_ID = 30;
	public static final int NEAR_WATERPLACE_TILE_ID = 29;
	
	public static final int FUEL_TILE_ID = 195;
	
	
	public static final int TERRAIN_TYPE_OBSTACLE_TILE_ID = 28;
	public static final int TERRAIN_TYPE_WATER_TILE_ID = 33;
	public static final int TERRAIN_TYPE_ROAD_TILE_ID = 35;
	public static final int TERRAIN_TYPE_PLAIN_TILE_ID = 30;
	public static final int TERRAIN_TYPE_UNEVEN_TILE_ID = 29;
	public static final int TERRAIN_TYPE_ROUGH_TILE_ID = 34;
	public static final int TERRAIN_TYPE_BROKEN_TILE_ID = 36;
	
	public static final int GROUND_TYPE_PLANTS_TILE_ID = 29;
	
	
	public static final int COLLISION_TILE_ID = 28;
	public static final int WATER_TILE_ID = 33;
	public static final int FLOWERS_TILE_ID = 1504;
	public static final int HIGHGRASS_TILE_ID = 1576;
	
	public static final int COLLISION_VEHICLE_TILE_ID = 29;
	
	
	public static final int META_ENEMY_TILE_LIGHTTANK = 2269;
	public static final int META_ENEMY_TILE_ROBOT = 2270;
	public static final int META_ENEMY_TILE_DRONE = 2271;
	
	
	public static final int ENERGYWALL_EFFECT_10_TILE_ID = 2332;
	public static final int ENERGYWALL_EFFECT_20_TILE_ID = 2333;
	public static final int ENERGYWALL_EFFECT_30_TILE_ID = 2334;
	public static final int ENERGYWALL_EFFECT_40_TILE_ID = 2335;
	public static final int ENERGYWALL_EFFECT_50_TILE_ID = 2336;
	public static final int ENERGYWALL_EFFECT_60_TILE_ID = 2337;
	public static final int ENERGYWALL_EFFECT_70_TILE_ID = 2338;
	public static final int ENERGYWALL_EFFECT_80_TILE_ID = 2339;
	public static final int ENERGYWALL_EFFECT_90_TILE_ID = 2340;
	public static final int ENERGYWALL_EFFECT_100_TILE_ID = 2341;
	public static final int ENERGYWALL_EFFECT_150_TILE_ID = 2342;
	public static final int ENERGYWALL_EFFECT_200_TILE_ID = 2343;
	public static final int ENERGYWALL_EFFECT_250_TILE_ID = 2344;
	public static final int ENERGYWALL_EFFECT_300_TILE_ID = 2345;
	public static final int ENERGYWALL_EFFECT_350_TILE_ID = 2346;
	public static final int ENERGYWALL_EFFECT_500_TILE_ID = 2347;
	public static final int ENERGYWALL_EFFECT_1000_TILE_ID = 2348;
	public static final int ENERGYWALL_EFFECT_10000_TILE_ID = 2349;
	
	public static final List<Integer> ENERGY_WALL_TILE_IDS = Lists.newArrayList();
	
	static {
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_10_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_20_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_30_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_40_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_50_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_60_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_70_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_80_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_90_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_100_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_150_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_200_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_250_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_300_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_350_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_500_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_1000_TILE_ID);
		ENERGY_WALL_TILE_IDS.add(ENERGYWALL_EFFECT_10000_TILE_ID);
	}
	
	
	public static final int ENERGY_WALL_GENERATOR_TILE_ID = 2350;
	
}
