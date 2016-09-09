package antonafanasjew.cosmodog.globals;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Tiled map layer constants.
 */
public class Layers {

	public static final int LAYER_META_OVERRIDEREGIONS_FORGRASSLAYER = 0;
	public static final int LAYER_HELPERS = LAYER_META_OVERRIDEREGIONS_FORGRASSLAYER + 1;
	public static final int LAYER_BACKGROUND = LAYER_HELPERS + 1;
	public static final int LAYER_BACKGROUND_OBJECTS_MANUAL = LAYER_BACKGROUND + 1;
	public static final int LAYER_BACKGROUND_OBJECTS_PASSABLE = LAYER_BACKGROUND_OBJECTS_MANUAL + 1;
	public static final int LAYER_SWAMP = LAYER_BACKGROUND_OBJECTS_PASSABLE + 1;
	public static final int LAYER_SAND = LAYER_SWAMP + 1;
	public static final int LAYER_SAND_OBJECTS_PASSABLE = LAYER_SAND + 1;
	public static final int LAYER_SAND_OBJECTS_MANUAL = LAYER_SAND_OBJECTS_PASSABLE + 1;
	public static final int LAYER_SNOW = LAYER_SAND_OBJECTS_MANUAL + 1;
	public static final int LAYER_SNOW_OBJECTS_PASSABLE = LAYER_SNOW + 1;
	public static final int LAYER_ICE = LAYER_SNOW_OBJECTS_PASSABLE + 1;
	public static final int LAYER_ICE_OBJECTS_PASSABLE = LAYER_ICE + 1;
	public static final int LAYER_ICE_OBJECTS_MANUAL = LAYER_ICE_OBJECTS_PASSABLE + 1;
	public static final int LAYER_GRASS = LAYER_ICE_OBJECTS_MANUAL + 1;
	public static final int LAYER_GRASS_OBJECTS_PASSABLE = LAYER_GRASS + 1;
	public static final int LAYER_GRASS_OBJECTS_MANUAL = LAYER_GRASS_OBJECTS_PASSABLE + 1;
	public static final int LAYER_GRASS_OBJECTS_MANUAL_2 = LAYER_GRASS_OBJECTS_MANUAL + 1;
	public static final int LAYER_FLOWERS = LAYER_GRASS_OBJECTS_MANUAL_2 + 1;
	public static final int LAYER_INFLOWERS_OBJECTS_RANDOM = LAYER_FLOWERS + 1;
	public static final int LAYER_WATER = LAYER_INFLOWERS_OBJECTS_RANDOM + 1;
	public static final int LAYER_WATER_OBJECTS_PASSABLE = LAYER_WATER + 1;
	public static final int LAYER_ASPHALT = LAYER_WATER_OBJECTS_PASSABLE + 1;
	public static final int LAYER_ASPHALT_OBJECTS_MANUAL = LAYER_ASPHALT + 1;
	public static final int LAYER_RUBBLE = LAYER_ASPHALT_OBJECTS_MANUAL + 1;
	public static final int LAYER_SCORCHEDEARTH = LAYER_RUBBLE + 1;
	public static final int LAYER_LAVA = LAYER_SCORCHEDEARTH + 1;
	public static final int LAYER_ROADS = LAYER_LAVA + 1;
	public static final int LAYER_ROADS_OBJECTS_MANUAL = LAYER_ROADS + 1;
	public static final int LAYER_RUINS_INFLOWERS = LAYER_ROADS_OBJECTS_MANUAL + 1;
	public static final int LAYER_CLIFFS = LAYER_RUINS_INFLOWERS + 1;
	public static final int LAYER_GRASSONCLIFFS = LAYER_CLIFFS + 1;
	public static final int LAYER_GRASSONCLIFFS_OBJECTS_PASSABLE = LAYER_GRASSONCLIFFS + 1;
	public static final int LAYER_ROCKS = LAYER_GRASSONCLIFFS_OBJECTS_PASSABLE + 1;
	public static final int LAYER_ROCKS_OBJECTS_PASSABLE = LAYER_ROCKS + 1;
	public static final int LAYER_ROCKS_OBJECTS_MANUAL = LAYER_ROCKS_OBJECTS_PASSABLE + 1;
	public static final int LAYER_ROCKS_UNDERGROUND = LAYER_ROCKS_OBJECTS_MANUAL + 1;
	public static final int LAYER_FOREST = LAYER_ROCKS_UNDERGROUND + 1;
	public static final int LAYER_FOREST_OBJECTS_RANDOM = LAYER_FOREST + 1;
	public static final int LAYER_FOREST_OBJECTS_MANUAL = LAYER_FOREST_OBJECTS_RANDOM + 1;
	public static final int LAYER_CRATERS = LAYER_FOREST_OBJECTS_MANUAL + 1;
	public static final int LAYER_RUINS = LAYER_CRATERS + 1;
	public static final int LAYER_RUINS_OBJECTS_RANDOM = LAYER_RUINS + 1;
	public static final int LAYER_RUINS_OBJECTS_MANUAL = LAYER_RUINS_OBJECTS_RANDOM + 1;
	public static final int LAYER_RUINS_OBJECTS_MANUAL_2 = LAYER_RUINS_OBJECTS_MANUAL + 1;
	public static final int LAYER_TOWERS = LAYER_RUINS_OBJECTS_MANUAL_2 + 1;
	public static final int LAYER_TOWERS_OBJECTS_MANUAL = LAYER_TOWERS + 1;
	public static final int LAYER_GEAR = LAYER_TOWERS_OBJECTS_MANUAL + 1;
	public static final int LAYER_FLOWERS_TIPS = LAYER_GEAR + 1;
	public static final int LAYER_RUINS_TIPS = LAYER_FLOWERS_TIPS + 1;
	public static final int LAYER_FOREST_TOP = LAYER_RUINS_TIPS + 1;
	public static final int LAYER_FOREST_TOP_OBJECTS_RANDOM = LAYER_FOREST_TOP + 1;
	public static final int LAYER_ROCKS_TOP = LAYER_FOREST_TOP_OBJECTS_RANDOM + 1;
	public static final int LAYER_ROCKS_TOP_OBJECTS_MANUAL = LAYER_ROCKS_TOP + 1;
	public static final int LAYER_GEAR_TOP = LAYER_ROCKS_TOP_OBJECTS_MANUAL + 1;
	public static final int LAYER_RUINS_TOP = LAYER_GEAR_TOP + 1;
	public static final int LAYER_TOWERS_TOP = LAYER_RUINS_TOP + 1;
	public static final int LAYER_TOWERS_TOP_OBJECTS_MANUAL = LAYER_TOWERS_TOP + 1;
	public static final int LAYER_META_COLLISIONS = LAYER_TOWERS_TOP_OBJECTS_MANUAL + 1;
	public static final int LAYER_META_COLLECTIBLES = LAYER_META_COLLISIONS + 1;
	public static final int LAYER_META_WATERPLACES = LAYER_META_COLLECTIBLES + 1;
	public static final int LAYER_META_ALT_WATER = LAYER_META_WATERPLACES + 1;
	public static final int LAYER_META_TERRAINTYPES = LAYER_META_ALT_WATER + 1;
	public static final int LAYER_META_GROUNDTYPES = LAYER_META_TERRAINTYPES + 1;
	public static final int LAYER_META_EFFECTS = LAYER_META_GROUNDTYPES + 1;
	public static final int LAYER_META_NPC = LAYER_META_EFFECTS + 1;
	public static final int LAYER_META_RADIATION = LAYER_META_NPC + 1;
	public static final int LAYER_META_TEMPERATURE = LAYER_META_RADIATION + 1;
	public static final int LAYER_META_PLATFORM_COLLISION = LAYER_META_TEMPERATURE + 1;


	/**
	 * This layers are counting as "covering", that is, their tiles may act as
	 * 'roofs' and be removed if the player goes under them. This list is not
	 * equal to the set of layer tiles that will be removed, but it will point
	 * to the other layers f.i. 'ruins_top' is a covering layer and if the
	 * player goes under a tile of this layer, all neighboring layers will be
	 * used as inversed clipping area and the tiles on this layer and all
	 * referenced layers (f.i. ruins_top_objects) will not be shown.
	 */
	public static final List<Integer> COVERING_TOP_LAYERS = Lists.newArrayList();

	static {
		COVERING_TOP_LAYERS.add(LAYER_RUINS_TOP);
		COVERING_TOP_LAYERS.add(LAYER_GEAR_TOP);
		COVERING_TOP_LAYERS.add(LAYER_ROCKS_TOP);
		COVERING_TOP_LAYERS.add(LAYER_FOREST_TOP);
	};

	/**
	 * If player is covered by a tile from a layer that is counting as
	 * 'coverable' all tiles on this position will be removed from all layers
	 * that are referenced by that coverable layer. f.i. ruins_top layer will
	 * remove all roofs on this layer and all roof objects on the layer
	 * 'ruins_top_objects'
	 */
	public static final Multimap<Integer, Integer> COVERING_2_REMOVABLE_LAYERS = ArrayListMultimap.create();

	static {
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_RUINS_TOP, LAYER_RUINS_TOP);
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_GEAR_TOP, LAYER_GEAR_TOP);
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_ROCKS_TOP, LAYER_ROCKS_TOP);
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_ROCKS_TOP, LAYER_ROCKS_TOP_OBJECTS_MANUAL);
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_FOREST_TOP, LAYER_FOREST_TOP);
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_FOREST_TOP, LAYER_FOREST_TOP);
		COVERING_2_REMOVABLE_LAYERS.put(LAYER_FOREST_TOP, LAYER_FOREST_TOP_OBJECTS_RANDOM);
	}

}
