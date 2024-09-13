package antonafanasjew.cosmodog.globals;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
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

	FAN_ON_A_WALL(Layers.LAYER_RUINS, 1047),
	FAN_ON_A_ROOF(Layers.LAYER_TOWERS_TOP, 4059),

	GRASS_OBJECT_FLOWER(Layers.LAYER_GRASS_OBJECTS_PASSABLE, 688),
	TRAFFIC_LIGHT(Layers.LAYER_ROADS_OBJECTS_MANUAL, 1613),

	LAVA_CENTER(Layers.LAYER_LAVA, 478),
	LAVA_BORDER_W(Layers.LAYER_LAVA, 479),
	LAVA_BORDER_NW(Layers.LAYER_LAVA, 480),
	LAVA_BORDER_N(Layers.LAYER_LAVA, 481),
	LAVA_BORDER_NE(Layers.LAYER_LAVA, 482),
	LAVA_BORDER_E(Layers.LAYER_LAVA, 483),
	LAVA_BORDER_SE(Layers.LAYER_LAVA, 484),
	LAVA_BORDER_S(Layers.LAYER_LAVA, 485),
	LAVA_BORDER_SW(Layers.LAYER_LAVA, 486),

	LAVA_BORDER_S_AND_E(Layers.LAYER_LAVA, 487),
	LAVA_BORDER_S_AND_W(Layers.LAYER_LAVA, 489),
	LAVA_BORDER_N_AND_W(Layers.LAYER_LAVA, 495),
	LAVA_BORDER_N_AND_E(Layers.LAYER_LAVA, 493),

	LAVA_RIVER_VERT(Layers.LAYER_LAVA, 497),
	LAVA_RIVER_HOR(Layers.LAYER_LAVA, 501),
	LAVA_RIVER_W_AND_S(Layers.LAYER_LAVA, 498),
	LAVA_RIVER_N_AND_E(Layers.LAYER_LAVA, 502),
	LAVA_RIVER_E_AND_S(Layers.LAYER_LAVA, 496),
	LAVA_RIVER_N_AND_W(Layers.LAYER_LAVA, 504),

	LAVA_RIVER_N_S_E(Layers.LAYER_LAVA, 508),
	LAVA_RIVER_N_S_W(Layers.LAYER_LAVA, 510),
	LAVA_RIVER_W_E_N(Layers.LAYER_LAVA, 512),
	LAVA_RIVER_W_E_S(Layers.LAYER_LAVA, 506),
	LAVA_RIVER_W_E_N_S(Layers.LAYER_LAVA, 500),

	LAVA_RIVER_PATCH(Layers.LAYER_LAVA, 509),
	LAVA_RIVER_DELTA_N(Layers.LAYER_LAVA, 515),
	LAVA_RIVER_DELTA_S(Layers.LAYER_LAVA, 521),
	LAVA_RIVER_DELTA_W(Layers.LAYER_LAVA, 517),
	LAVA_RIVER_DELTA_E(Layers.LAYER_LAVA, 519),
	LAVA_RIVER_BRIDGE_VERT(Layers.LAYER_LAVA, 514),
	LAVA_RIVER_BRIDGE_HOR(Layers.LAYER_LAVA, 516),


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
	
	
	REV_RIVER_VERT(Layers.LAYER_LAVA, 2002),
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
	
	INFOBIT(Layers.LAYER_META_COLLECTIBLES,191),
	INFOBYTE(Layers.LAYER_META_COLLECTIBLES,3069),
	INFOBANK(Layers.LAYER_META_COLLECTIBLES,3070),
	SOFTWARE(Layers.LAYER_META_COLLECTIBLES,3071),
	CHART(Layers.LAYER_META_COLLECTIBLES,3072),
	MINEDETECTOR(Layers.LAYER_META_COLLECTIBLES,3073),
	ANTIDOTE(Layers.LAYER_META_COLLECTIBLES,3074),
	COGNITION(Layers.LAYER_META_COLLECTIBLES,3075),
	INSIGHT(Layers.LAYER_META_COLLECTIBLES,196),
	SOULESSENCE(Layers.LAYER_META_COLLECTIBLES,197),
	ARMOR(Layers.LAYER_META_COLLECTIBLES,206),
	SUPPLIES(Layers.LAYER_META_COLLECTIBLES,190),
	MEDIPACK(Layers.LAYER_META_COLLECTIBLES,192),
	VEHICLE(Layers.LAYER_META_COLLECTIBLES,193),
	BOAT(Layers.LAYER_META_COLLECTIBLES,194),
	DYNAMITE(Layers.LAYER_META_COLLECTIBLES,198),
	GEIGERZAEHLER(Layers.LAYER_META_COLLECTIBLES,207), //Take note: Geiger counter collectible is for both the actual geiger counter and the protective suit.
	SUPPLYTRACKER(Layers.LAYER_META_COLLECTIBLES,3061),
	BINOCULARS(Layers.LAYER_META_COLLECTIBLES,3062),
	JACKET(Layers.LAYER_META_COLLECTIBLES,3063),
	SKI(Layers.LAYER_META_COLLECTIBLES,3064),

	PICK(Layers.LAYER_META_COLLECTIBLES,3066),
	MACHETE(Layers.LAYER_META_COLLECTIBLES,3067),
	ARCHEOLOGISTS_JOURNAL(Layers.LAYER_META_COLLECTIBLES,3076),
	WEAPON_FIRMWARE_UPGRADE(Layers.LAYER_META_COLLECTIBLES,3077),
	AXE(Layers.LAYER_META_COLLECTIBLES,3068),

	PLATFORM(Layers.LAYER_META_COLLECTIBLES,3065),
	BOTTLE(Layers.LAYER_META_COLLECTIBLES,201),
	FOOD_COMPARTMENT(Layers.LAYER_META_COLLECTIBLES,205),
	FUEL_TANK(Layers.LAYER_META_COLLECTIBLES, 3078),

	
	RED_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4510),
	LILA_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4511),
	BLUE_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4512),
	CYAN_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4513),
	GREEN_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4514),
	YELLOW_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4515),
	BROWN_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4516),
	PURPLE_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4517),
	DARKBLUE_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4518),
	WHITE_ALIEN_KEYCARD(Layers.LAYER_META_COLLECTIBLES,4519),

	
	LOG_CARD_SERIES_0(Layers.LAYER_META_COLLECTIBLES,4528),
	LOG_CARD_SERIES_1(Layers.LAYER_META_COLLECTIBLES,4529),
	LOG_CARD_SERIES_2(Layers.LAYER_META_COLLECTIBLES,4530),
	LOG_CARD_SERIES_3(Layers.LAYER_META_COLLECTIBLES,4531),
	LOG_CARD_SERIES_4(Layers.LAYER_META_COLLECTIBLES,4532),
	LOG_CARD_SERIES_5(Layers.LAYER_META_COLLECTIBLES,4533),
	LOG_CARD_SERIES_6(Layers.LAYER_META_COLLECTIBLES,4534),
	LOG_CARD_SERIES_7(Layers.LAYER_META_COLLECTIBLES,4535),
	LOG_CARD_SERIES_8(Layers.LAYER_META_COLLECTIBLES,4536),
	LOG_CARD_SERIES_9(Layers.LAYER_META_COLLECTIBLES,4537),
	LOG_CARD_SERIES_10(Layers.LAYER_META_COLLECTIBLES,4538),
	LOG_CARD_SERIES_11(Layers.LAYER_META_COLLECTIBLES,4539),
	LOG_CARD_SERIES_12(Layers.LAYER_META_COLLECTIBLES,4540),
	LOG_CARD_SERIES_13(Layers.LAYER_META_COLLECTIBLES,4541),
	LOG_CARD_SERIES_14(Layers.LAYER_META_COLLECTIBLES,4542),
	LOG_CARD_SERIES_15(Layers.LAYER_META_COLLECTIBLES,4543),
	LOG_CARD_SERIES_16(Layers.LAYER_META_COLLECTIBLES,4544),
	LOG_CARD_SERIES_17(Layers.LAYER_META_COLLECTIBLES,4545),
	
	LOG_CARD_0(Layers.LAYER_META_COLLECTIBLES,4546),
	LOG_CARD_1(Layers.LAYER_META_COLLECTIBLES,4547),
	LOG_CARD_2(Layers.LAYER_META_COLLECTIBLES,4548),
	LOG_CARD_3(Layers.LAYER_META_COLLECTIBLES,4549),
	LOG_CARD_4(Layers.LAYER_META_COLLECTIBLES,4550),
	LOG_CARD_5(Layers.LAYER_META_COLLECTIBLES,4551),
	LOG_CARD_6(Layers.LAYER_META_COLLECTIBLES,4552),
	LOG_CARD_7(Layers.LAYER_META_COLLECTIBLES,4553),
	LOG_CARD_8(Layers.LAYER_META_COLLECTIBLES,4554),
	LOG_CARD_9(Layers.LAYER_META_COLLECTIBLES,4555),
	LOG_CARD_10(Layers.LAYER_META_COLLECTIBLES,4556),
	LOG_CARD_11(Layers.LAYER_META_COLLECTIBLES,4557),
	LOG_CARD_12(Layers.LAYER_META_COLLECTIBLES,4558),
	LOG_CARD_13(Layers.LAYER_META_COLLECTIBLES,4559),
	LOG_CARD_14(Layers.LAYER_META_COLLECTIBLES,4560),
	LOG_CARD_15(Layers.LAYER_META_COLLECTIBLES,4561),
	LOG_CARD_16(Layers.LAYER_META_COLLECTIBLES,4562),
	LOG_CARD_17(Layers.LAYER_META_COLLECTIBLES,4563),
	LOG_CARD_18(Layers.LAYER_META_COLLECTIBLES,4564),
	LOG_CARD_19(Layers.LAYER_META_COLLECTIBLES,4565),
	LOG_CARD_20(Layers.LAYER_META_COLLECTIBLES,4566),
	LOG_CARD_21(Layers.LAYER_META_COLLECTIBLES,4567),
	LOG_CARD_22(Layers.LAYER_META_COLLECTIBLES,4568),
	LOG_CARD_23(Layers.LAYER_META_COLLECTIBLES,4569),
	LOG_CARD_24(Layers.LAYER_META_COLLECTIBLES,4570),
	LOG_CARD_25(Layers.LAYER_META_COLLECTIBLES,4571),
	LOG_CARD_26(Layers.LAYER_META_COLLECTIBLES,4572),
	LOG_CARD_27(Layers.LAYER_META_COLLECTIBLES,4573),
	LOG_CARD_28(Layers.LAYER_META_COLLECTIBLES,4574),
	LOG_CARD_29(Layers.LAYER_META_COLLECTIBLES,4575),
	LOG_CARD_30(Layers.LAYER_META_COLLECTIBLES,4576),
	LOG_CARD_31(Layers.LAYER_META_COLLECTIBLES,4577),
	LOG_CARD_32(Layers.LAYER_META_COLLECTIBLES,4578),
	LOG_CARD_33(Layers.LAYER_META_COLLECTIBLES,4579),
	LOG_CARD_34(Layers.LAYER_META_COLLECTIBLES,4580),
	LOG_CARD_35(Layers.LAYER_META_COLLECTIBLES,4581),
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	MIN_ALIEN_DOOR(Layers.LAYER_META_DYNAMIC_PIECES,4474),
	MAX_ALIEN_DOOR(Layers.LAYER_META_DYNAMIC_PIECES,4509),
	
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
	COLLISION_VEHICLE(Layers.LAYER_META_COLLISIONS, 29),
	COLLISION_FREE(Layers.LAYER_META_COLLISIONS, 30),
	COLLISION_SNOW(Layers.LAYER_META_COLLISIONS, 32),
	COLLISION_WATER(Layers.LAYER_META_COLLISIONS, 33),
	
	//FLOWERS(-1000, 1504),
	//HIGHGRASS(-1000, 1576),
	
	
	
	META_ENEMY_TILE_LIGHTTANK(Layers.LAYER_META_NPC, 2269),
	META_ENEMY_TILE_ROBOT(Layers.LAYER_META_NPC, 2270),
	META_ENEMY_TILE_DRONE(Layers.LAYER_META_NPC, 2271),
	META_ENEMY_TILE_TURRET(Layers.LAYER_META_NPC, 2272),
	META_ENEMY_TILE_PIGRAT(Layers.LAYER_META_NPC, 2273),
	META_ENEMY_TILE_ARTILLERY(Layers.LAYER_META_NPC, 2274),
	META_ENEMY_TILE_SCOUT(Layers.LAYER_META_NPC, 1974),
	META_ENEMY_TILE_FLOATER(Layers.LAYER_META_NPC, 2275),
	META_ENEMY_TILE_CONDUCTOR(Layers.LAYER_META_NPC, 2276),
	META_ENEMY_TILE_GUARDIAN(Layers.LAYER_META_NPC, 2277),
	META_ENEMY_TILE_SOLARTANK(Layers.LAYER_META_NPC, 1975),
	
	META_TEMPERATURE_COLD(Layers.LAYER_META_TEMPERATURE, 33),
	
	ENERGYWALL_EFFECT_1(Layers.LAYER_META_EFFECTS, 2332),
	ENERGYWALL_EFFECT_2(Layers.LAYER_META_EFFECTS, 2333),
	ENERGYWALL_EFFECT_3(Layers.LAYER_META_EFFECTS, 2334),
	ENERGYWALL_EFFECT_4(Layers.LAYER_META_EFFECTS, 2335),
	ENERGYWALL_EFFECT_5(Layers.LAYER_META_EFFECTS, 2336),
	ENERGYWALL_EFFECT_6(Layers.LAYER_META_EFFECTS, 2337),
	ENERGYWALL_EFFECT_7(Layers.LAYER_META_EFFECTS, 2338),
	ENERGYWALL_EFFECT_8(Layers.LAYER_META_EFFECTS, 2339),
	ENERGYWALL_EFFECT_9(Layers.LAYER_META_EFFECTS, 2340),
	ENERGYWALL_EFFECT_10(Layers.LAYER_META_EFFECTS, 2341),
	ENERGYWALL_EFFECT_11(Layers.LAYER_META_EFFECTS, 2342),
	ENERGYWALL_EFFECT_12(Layers.LAYER_META_EFFECTS, 2343),
	ENERGYWALL_EFFECT_13(Layers.LAYER_META_EFFECTS, 2344),
	ENERGYWALL_EFFECT_14(Layers.LAYER_META_EFFECTS, 2345),
	ENERGYWALL_EFFECT_15(Layers.LAYER_META_EFFECTS, 2346),
	ENERGYWALL_EFFECT_16(Layers.LAYER_META_EFFECTS, 2347),
	ENERGYWALL_EFFECT_17(Layers.LAYER_META_EFFECTS, 2348),
	ENERGYWALL_EFFECT_18(Layers.LAYER_META_EFFECTS, 2349),
	
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
	ELECTRICITY(Layers.LAYER_META_RADIATION, 32),
	
	FREE_PLATFORM_PASSAGE(Layers.LAYER_META_PLATFORM_COLLISION, 30),
	
	
	DYNAMIC_PIECE_STONE(Layers.LAYER_META_DYNAMIC_PIECES, 2110),
	DYNAMIC_PIECE_HARDSTONE(Layers.LAYER_META_DYNAMIC_PIECES, 2111),
	DYNAMIC_PIECE_TREE(Layers.LAYER_META_DYNAMIC_PIECES, 2112),
	DYNAMIC_PIECE_BAMBOO(Layers.LAYER_META_DYNAMIC_PIECES, 2113),
	DYNAMIC_PIECE_CRATE(Layers.LAYER_META_DYNAMIC_PIECES, 2114),
	DYNAMIC_PIECE_MINE(Layers.LAYER_META_DYNAMIC_PIECES, 2115),
	DYNAMIC_PIECE_POISON(Layers.LAYER_META_DYNAMIC_PIECES, 2120),
	DYNAMIC_PIECE_PRESSUREBUTTON(Layers.LAYER_META_DYNAMIC_PIECES, 2119),
	DECONTAMINATION_SPOT(Layers.LAYER_GEAR, 1399),
	DYNAMIC_PIECE_CRUMBLED_WALL_MONTAIN(Layers.LAYER_META_DYNAMIC_PIECES, 2121),
	DYNAMIC_PIECE_CRUMBLED_WALL_ALIEN_BASE(Layers.LAYER_META_DYNAMIC_PIECES, 2122),
	DYNAMIC_PIECE_GATE(Layers.LAYER_META_DYNAMIC_PIECES, 2123),
	DYNAMIC_PIECE_BINARY_INDICATOR_ALIEN_BASE(Layers.LAYER_META_DYNAMIC_PIECES, 2124),
	DYNAMIC_PIECE_ALIEN_BASE_BLOCKADE(Layers.LAYER_META_DYNAMIC_PIECES, 2128),
	
	DYNAMIC_PIECE_LETTERPLATE_1(Layers.LAYER_META_DYNAMIC_PIECES, 2230),
	DYNAMIC_PIECE_LETTERPLATE_2(Layers.LAYER_META_DYNAMIC_PIECES, 2231),
	DYNAMIC_PIECE_LETTERPLATE_3(Layers.LAYER_META_DYNAMIC_PIECES, 2232),
	DYNAMIC_PIECE_LETTERPLATE_4(Layers.LAYER_META_DYNAMIC_PIECES, 2239),
	DYNAMIC_PIECE_LETTERPLATE_5(Layers.LAYER_META_DYNAMIC_PIECES, 2240),
	DYNAMIC_PIECE_LETTERPLATE_6(Layers.LAYER_META_DYNAMIC_PIECES, 2241),
	DYNAMIC_PIECE_LETTERPLATE_7(Layers.LAYER_META_DYNAMIC_PIECES, 2248),
	DYNAMIC_PIECE_LETTERPLATE_8(Layers.LAYER_META_DYNAMIC_PIECES, 2249),
	DYNAMIC_PIECE_LETTERPLATE_9(Layers.LAYER_META_DYNAMIC_PIECES, 2250),
	
	DYNAMIC_PIECE_GUIDETERMINAL(Layers.LAYER_META_DYNAMIC_PIECES, 4598),
	
	DYNAMIC_PIECE_MOVEABLE_BLOCK(Layers.LAYER_META_DYNAMIC_PIECES, 3079),
	DYNAMIC_PIECE_MOVEABLE_CONTAINER(Layers.LAYER_META_DYNAMIC_PIECES, 3080),
	DYNAMIC_PIECE_MOVEABLE_ICE(Layers.LAYER_META_DYNAMIC_PIECES, 3081),
	DYNAMIC_PIECE_MOVEABLE_PLANT(Layers.LAYER_META_DYNAMIC_PIECES, 3082),
	
	DYNAMIC_PIECE_SECRET_DOOR_SPIKES(Layers.LAYER_META_DYNAMIC_PIECES, 3083),
	DYNAMIC_PIECE_SECRET_DOOR_HYDRAULICS(Layers.LAYER_META_DYNAMIC_PIECES, 3084),
	DYNAMIC_PIECE_SECRET_DOOR_ENERGY(Layers.LAYER_META_DYNAMIC_PIECES, 3085),
	DYNAMIC_PIECE_SECRET_DOOR_WALL(Layers.LAYER_META_DYNAMIC_PIECES, 3086),
	
	//Doors have different variants, so they are not stored here.
	
	//This is a dummy tile type to represent a tile that is not in the tile type enum.
	//It can be used to be returned when searching tile types by tile id in case the id does not match any type.
	UNKNOWN(-1, -1),
	
	NO_RADIATION_MARKUP(Layers.LAYER_META_RADIATION, 0);
	
	static class LayerAndTile {
		
		public int layerId;
		public int tileId;
		
		public static LayerAndTile fromIds(int layerId, int tileId) {
			LayerAndTile retVal = new LayerAndTile();
			retVal.layerId = layerId;
			retVal.tileId = tileId;
			return retVal;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + layerId;
			result = prime * result + tileId;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LayerAndTile other = (LayerAndTile) obj;
			if (layerId != other.layerId)
				return false;
			if (tileId != other.tileId)
				return false;
			return true;
		}
		
	}
	
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
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_1);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_2);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_3);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_4);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_5);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_6);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_7);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_8);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_9);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_10);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_11);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_12);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_13);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_14);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_15);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_16);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_17);
		ENERGY_WALL_TILES.add(ENERGYWALL_EFFECT_18);
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
	
	private static Cache<LayerAndTile, TileType> tileTypePerLayerAndTileIdCache = CacheBuilder.newBuilder().build();
	
	/**
	 * Returns the tile type for given layer id and the tile id.
	 * @param layerId Layer id.
	 * @param tileId Tile id.
	 * @return The tile type for given ids or UNKNOWN if nothing matches.
	 */
	public static TileType getByLayerAndTileId(int layerId, int tileId) {
		
		LayerAndTile layerAndTile = LayerAndTile.fromIds(layerId, tileId);
		
		try {
			TileType tileType = tileTypePerLayerAndTileIdCache.get(layerAndTile, new Callable<TileType>() {
	
				@Override
				public TileType call() {
						for (TileType tileType : VALUES) {
							if (tileType.getLayerId() == layerId && tileType.getTileId() == tileId) {
								return tileType;
							}
						}
						return TileType.UNKNOWN;
				}
				
			});
			return tileType;
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		
	}
	
}
