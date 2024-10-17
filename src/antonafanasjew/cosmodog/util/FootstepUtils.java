package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.topology.Position;

public class FootstepUtils {

	public static String footStepsSoundType(Position position) {
		if (targetInWater(position)) {
			return SoundResources.SOUND_FOOTSTEPS_WATER;
		} else if (targetInHighGrass(position)) {
			return SoundResources.SOUND_FOOTSTEPS_HIGH_GRASS;
		} else if (targetInGrass(position)) {
			return SoundResources.SOUND_FOOTSTEPS_GRASS;
		} else if (targetInSand(position)) {
			return SoundResources.SOUND_FOOTSTEPS_SAND;
		} else if (targetInSnow(position)) {
			return SoundResources.SOUND_FOOTSTEPS_SNOW;
		} else if (targetInSolidTerrain(position)) {
			return SoundResources.SOUND_FOOTSTEPS_ROAD;
		} else {
			return SoundResources.SOUND_FOOTSTEPS;
		}
	}
	
	public static boolean targetInWater(Position position) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(position, Layers.LAYER_META_COLLISIONS);
        return TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_WATER);
	}
	
	public static boolean targetInHighGrass(Position position) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(position, Layers.LAYER_META_GROUNDTYPES);
        return TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_PLANTS);
	}
	
	public static boolean targetInSnow(Position position) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(position, Layers.LAYER_META_GROUNDTYPES);
        return TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_SNOW);
	}
	
	public static boolean targetInSand(Position position) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(position, Layers.LAYER_META_GROUNDTYPES);
        return TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_SAND);
	}
	
	public static boolean targetInGrass(Position position) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(position, Layers.LAYER_META_GROUNDTYPES);
        return TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_GRASS);
	}
	
	public static boolean targetInSolidTerrain(Position position) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(position, Layers.LAYER_META_TERRAINTYPES);
        return TileType.getByLayerAndTileId(Layers.LAYER_META_TERRAINTYPES, tileId).equals(TileType.TERRAIN_TYPE_ROAD);
	}
	
}
