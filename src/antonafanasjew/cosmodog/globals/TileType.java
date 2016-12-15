package antonafanasjew.cosmodog.globals;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Contains tile types with the corresponding tile and layer ids. 
 * Note: Tile ids are related to the offset that is used in the application. They do not match the id's in the Pyxel tool.
 * To calculate a real id from the Pyxel tool id: Take the pyxel tool id and add 1.
 * 
 * Note: These tile types do not represent the tiles in the tile sheet, but rather their semantics in the context of the map.
 * Example: The brown marker tile with the id x can be included multiple times in the tile type enumeration, f.i. on layer enemies as ENEMY_TANK
 * and on layer effects as EFFECT_BIRDS.
 * 
 * Hence, a tile type cannot be identified by a tile id. To identify the tile type, both tile id and the layer id are needed.
 * 
 * To represent an unknown tile type, the type UNKNONW can be used.
 *
 * 
 */
public enum TileType {

	GRASS_OBJECT_FLOWER(Layers.LAYER_GRASS_OBJECTS_PASSABLE, 688),
	
	WATER_CENTER(Layers.LAYER_WATER, 55),
	WATER_BORDER_W(Layers.LAYER_WATER, 56),
	WATER_BORDER_NW(Layers.LAYER_WATER, 57),
	WATER_BORDER_N(Layers.LAYER_WATER, 58),
	WATER_BORDER_NE(Layers.LAYER_WATER, 59),
	WATER_BORDER_E(Layers.LAYER_WATER, 60),
	WATER_BORDER_SE(Layers.LAYER_WATER, 61),
	WATER_BORDER_S(Layers.LAYER_WATER, 62),
	WATER_BORDER_SW(Layers.LAYER_WATER, 63),
	
	WATER_BORDER_S_AND_E(Layers.LAYER_WATER, 66),
	WATER_BORDER_S_AND_W(Layers.LAYER_WATER, 68),
	WATER_BORDER_N_AND_W(Layers.LAYER_WATER, 70),
	WATER_BORDER_N_AND_E(Layers.LAYER_WATER, 72),
	
	RIVER_VERT(Layers.LAYER_WATER, 76),
	RIVER_HOR(Layers.LAYER_WATER, 78),
	RIVER_W_AND_S(Layers.LAYER_WATER, 77),
	RIVER_N_AND_E(Layers.LAYER_WATER, 81),
	RIVER_E_AND_S(Layers.LAYER_WATER, 75),
	RIVER_N_AND_W(Layers.LAYER_WATER, 79),
	RIVER_N_S_E(Layers.LAYER_WATER, 83),
	RIVER_N_S_W(Layers.LAYER_WATER, 87),
	RIVER_W_E_N(Layers.LAYER_WATER, 89),
	RIVER_W_E_S(Layers.LAYER_WATER, 85),
	RIVER_W_E_N_S(Layers.LAYER_WATER, 73),
	RIVER_PATCH(Layers.LAYER_WATER, 82),
	RIVER_DELTA_N(Layers.LAYER_WATER, 98),
	RIVER_DELTA_S(Layers.LAYER_WATER, 94),
	RIVER_DELTA_W(Layers.LAYER_WATER, 96),
	RIVER_DELTA_E(Layers.LAYER_WATER, 92),
	RIVER_BRIDGE_VERT(Layers.LAYER_WATER, 93),
	RIVER_BRIDGE_HOR(Layers.LAYER_WATER, 95),
	
	
	REV_RIVER_VERT(Layers.LAYER_WATER, 2002),
	REV_RIVER_HOR(Layers.LAYER_WATER, 2004),
	REV_RIVER_W_AND_S(Layers.LAYER_WATER, 2003),
	REV_RIVER_N_AND_E(Layers.LAYER_WATER, 2007),
	REV_RIVER_E_AND_S(Layers.LAYER_WATER, 2001),
	REV_RIVER_N_AND_W(Layers.LAYER_WATER, 2005),
	REV_RIVER_N_S_E(Layers.LAYER_WATER, 2009),
	REV_RIVER_N_S_W(Layers.LAYER_WATER, 2013),
	REV_RIVER_W_E_N(Layers.LAYER_WATER, 2015),
	REV_RIVER_W_E_S(Layers.LAYER_WATER, 2011),
	REV_RIVER_W_E_N_S(Layers.LAYER_WATER, 1999),
	REV_RIVER_PATCH(Layers.LAYER_WATER, 2008),
	REV_RIVER_DELTA_N(Layers.LAYER_WATER, 2024),
	REV_RIVER_DELTA_S(Layers.LAYER_WATER, 2020),
	REV_RIVER_DELTA_W(Layers.LAYER_WATER, 2022),
	REV_RIVER_DELTA_E(Layers.LAYER_WATER, 2018),
	REV_RIVER_BRIDGE_VERT(Layers.LAYER_WATER, 2019),
	REV_RIVER_BRIDGE_HOR(Layers.LAYER_WATER, 2021),
	
	
	
	FIRE_EFFECT(Layers.LAYER_META_EFFECTS, 28),
	SMOKE_EFFECT(Layers.LAYER_META_EFFECTS, 35),
	BIRDS_EFFECT(Layers.LAYER_META_EFFECTS, 36),
	ELECTRICITY_EFFECT(Layers.LAYER_META_EFFECTS, 33),
	TELEPORT_EFFECT(Layers.LAYER_META_EFFECTS, 30),
	
	
	WATERPLACE(Layers.LAYER_META_WATERPLACES, 30),
	NEAR_WATERPLACE(Layers.LAYER_META_WATERPLACES, 29),
	
	FUEL(Layers.LAYER_META_COLLECTIBLES, 195),
	
	
	TERRAIN_TYPE_OBSTACLE(Layers.LAYER_META_TERRAINTYPES, 28),
	TERRAIN_TYPE_WATER(Layers.LAYER_META_TERRAINTYPES, 33),
	TERRAIN_TYPE_ROAD(Layers.LAYER_META_TERRAINTYPES, 35),
	TERRAIN_TYPE_PLAIN(Layers.LAYER_META_TERRAINTYPES, 30),
	TERRAIN_TYPE_UNEVEN(Layers.LAYER_META_TERRAINTYPES, 29),
	TERRAIN_TYPE_ROUGH(Layers.LAYER_META_TERRAINTYPES, 34),
	TERRAIN_TYPE_BROKEN(Layers.LAYER_META_TERRAINTYPES, 36),
	
	GROUND_TYPE_PLANTS(Layers.LAYER_META_GROUNDTYPES, 29),
	GROUND_TYPE_SNOW(Layers.LAYER_META_GROUNDTYPES, 33),
	GROUND_TYPE_GRASS(Layers.LAYER_META_GROUNDTYPES, 10),
	GROUND_TYPE_SAND(Layers.LAYER_META_GROUNDTYPES, 208),
	GROUND_TYPE_SWAMP(Layers.LAYER_META_GROUNDTYPES, 172),
	
	
	COLLISION(Layers.LAYER_META_COLLISIONS, 28),
	COLLISION_WATER(Layers.LAYER_META_COLLISIONS, 33),
	COLLISION_VEHICLE(Layers.LAYER_META_COLLISIONS, 29),
	
	//FLOWERS(-1000, 1504),
	//HIGHGRASS(-1000, 1576),
	
	
	
	META_ENEMY_TILE_LIGHTTANK(Layers.LAYER_META_NPC, 2269),
	META_ENEMY_TILE_ROBOT(Layers.LAYER_META_NPC, 2270),
	META_ENEMY_TILE_DRONE(Layers.LAYER_META_NPC, 2271),
	META_ENEMY_TILE_TURRET(Layers.LAYER_META_NPC, 2272),
	META_ENEMY_TILE_PIGRAT(Layers.LAYER_META_NPC, 2273),
	
	META_TEMPERATURE_COLD(Layers.LAYER_META_TEMPERATURE, 33),
	
	ENERGYWALL_EFFECT_10(Layers.LAYER_META_EFFECTS, 2332),
	ENERGYWALL_EFFECT_20(Layers.LAYER_META_EFFECTS, 2333),
	ENERGYWALL_EFFECT_30(Layers.LAYER_META_EFFECTS, 2334),
	ENERGYWALL_EFFECT_40(Layers.LAYER_META_EFFECTS, 2335),
	ENERGYWALL_EFFECT_50(Layers.LAYER_META_EFFECTS, 2336),
	ENERGYWALL_EFFECT_60(Layers.LAYER_META_EFFECTS, 2337),
	ENERGYWALL_EFFECT_70(Layers.LAYER_META_EFFECTS, 2338),
	ENERGYWALL_EFFECT_80(Layers.LAYER_META_EFFECTS, 2339),
	ENERGYWALL_EFFECT_90(Layers.LAYER_META_EFFECTS, 2340),
	ENERGYWALL_EFFECT_100(Layers.LAYER_META_EFFECTS, 2341),
	ENERGYWALL_EFFECT_150(Layers.LAYER_META_EFFECTS, 2342),
	ENERGYWALL_EFFECT_200(Layers.LAYER_META_EFFECTS, 2343),
	ENERGYWALL_EFFECT_250(Layers.LAYER_META_EFFECTS, 2344),
	ENERGYWALL_EFFECT_300(Layers.LAYER_META_EFFECTS, 2345),
	ENERGYWALL_EFFECT_350(Layers.LAYER_META_EFFECTS, 2346),
	ENERGYWALL_EFFECT_500(Layers.LAYER_META_EFFECTS, 2347),
	ENERGYWALL_EFFECT_1000(Layers.LAYER_META_EFFECTS, 2348),
	ENERGYWALL_EFFECT_10000(Layers.LAYER_META_EFFECTS, 2349),
	
	ENERGY_WALL_GENERATOR(Layers.LAYER_ROADS_OBJECTS_MANUAL, 2350),
	
	
	WEAPONS_PISTOL(Layers.LAYER_META_COLLECTIBLES, 2386),
	WEAPONS_SHOTGUN(Layers.LAYER_META_COLLECTIBLES, 2387),
	WEAPONS_RIFLE(Layers.LAYER_META_COLLECTIBLES, 2388),
	WEAPONS_MACHINEGUN(Layers.LAYER_META_COLLECTIBLES, 2389),
	WEAPONS_ROCKETLAUNCHER(Layers.LAYER_META_COLLECTIBLES, 2390),

	AMMO_PISTOL(Layers.LAYER_META_COLLECTIBLES, 2395),
	AMMO_SHOTGUN(Layers.LAYER_META_COLLECTIBLES, 2396),
	AMMO_RIFLE(Layers.LAYER_META_COLLECTIBLES, 2397),
	AMMO_MACHINEGUN(Layers.LAYER_META_COLLECTIBLES, 2398),
	AMMO_ROCKETLAUNCHER(Layers.LAYER_META_COLLECTIBLES, 2399),
	
	RADIATION(Layers.LAYER_META_RADIATION, 29),
	
	FREE_PLATFORM_PASSAGE(Layers.LAYER_META_PLATFORM_COLLISION, 30),
	
	
	DYNAMIC_PIECE_STONE(Layers.LAYER_META_DYNAMIC_PIECES, 2110),
	DYNAMIC_PIECE_HARDSTONE(Layers.LAYER_META_DYNAMIC_PIECES, 2111),
	DYNAMIC_PIECE_TREE(Layers.LAYER_META_DYNAMIC_PIECES, 2112),
	DYNAMIC_PIECE_BAMBOO(Layers.LAYER_META_DYNAMIC_PIECES, 2113),
	DYNAMIC_PIECE_CRATE(Layers.LAYER_META_DYNAMIC_PIECES, 2114),
	
	
	//This is a dummy tile type to represent a tile that is not in the tile type enum.
	//It can be used to be returned when searching tile types by tile id in case the id does not match any type.
	UNKNOWN(-1, -1);
	
	
	private static final TileType[] VALUES = TileType.values();
	
	public static final List<TileType> WEAPONS_TILES = Lists.newArrayList();
	
	static {
		WEAPONS_TILES.add(WEAPONS_PISTOL);
		WEAPONS_TILES.add(WEAPONS_SHOTGUN);
		WEAPONS_TILES.add(WEAPONS_RIFLE);
		WEAPONS_TILES.add(WEAPONS_MACHINEGUN);
		WEAPONS_TILES.add(WEAPONS_ROCKETLAUNCHER);
	}
	
	public static final List<TileType> AMMO_TILES = Lists.newArrayList();
	
	static {
		AMMO_TILES.add(AMMO_PISTOL);
		AMMO_TILES.add(AMMO_SHOTGUN);
		AMMO_TILES.add(AMMO_RIFLE);
		AMMO_TILES.add(AMMO_MACHINEGUN);
		AMMO_TILES.add(AMMO_ROCKETLAUNCHER);
	}
	
	public static final List<TileType> ENERGY_WALL_TILES = Lists.newArrayList();
	
	static {
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_10);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_20);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_30);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_40);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_50);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_60);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_70);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_80);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_90);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_100);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_150);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_200);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_250);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_300);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_350);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_500);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_1000);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_10000);
	}

	private int layerId;
	private int tileId;
	
	
	private TileType(int layerId, int tileId) {
		this.layerId = layerId;
		this.tileId = tileId;
	}
	
	/**
	 * Returns the tile id.
	 * @return Tile id.
	 */
	public int getTileId() {
		return tileId;
	}
	
	/**
	 * Returns the layer id.
	 * @return Layer id.
	 */
	public int getLayerId() {
		return layerId;
	}
	
	/**
	 * Returns the tile type for given layer id and the tile id.
	 * @param layerId Layer id.
	 * @param tileId Tile id.
	 * @return The tile type for given ids or UNKNOWN if nothing matches.
	 */
	public static TileType getByLayerAndTileId(int layerId, int tileId) {
		for (TileType tileType : VALUES) {
			if (tileType.getLayerId() == layerId && tileType.getTileId() == tileId) {
				return tileType;
			}
		}
		return TileType.UNKNOWN;
	}
	
}
