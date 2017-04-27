package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogMap;

public class FootstepUtils {

	public static String footStepsSoundType(int positionX, int positionY) {
		if (targetInWater(positionX, positionY)) {
			return SoundResources.SOUND_FOOTSTEPS_WATER;
		} else if (targetInHighGrass(positionX, positionY)) {
			return SoundResources.SOUND_FOOTSTEPS_HIGH_GRASS;
		} else if (targetInGrass(positionX, positionY)) {
			return SoundResources.SOUND_FOOTSTEPS_GRASS;
		} else if (targetInSand(positionX, positionY)) {
			return SoundResources.SOUND_FOOTSTEPS_SAND;
		} else if (targetInSnow(positionX, positionY)) {
			return SoundResources.SOUND_FOOTSTEPS_SNOW;
		} else if (targetInSolidTerrain(positionX, positionY)) {
			return SoundResources.SOUND_FOOTSTEPS_ROAD;
		} else {
			return SoundResources.SOUND_FOOTSTEPS;
		}
	}
	
	public static final boolean targetInWater(int positionX, int positionY) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(positionX, positionY, Layers.LAYER_META_COLLISIONS);
		boolean retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_WATER);
		return retVal;
	}
	
	public static final boolean targetInHighGrass(int positionX, int positionY) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(positionX, positionY, Layers.LAYER_META_GROUNDTYPES);
		boolean retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_PLANTS);
		return retVal;
	}
	
	public static final boolean targetInSnow(int positionX, int positionY) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(positionX, positionY, Layers.LAYER_META_GROUNDTYPES);
		boolean retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_SNOW);
		return retVal;
	}
	
	public static final boolean targetInSand(int positionX, int positionY) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(positionX, positionY, Layers.LAYER_META_GROUNDTYPES);
		boolean retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_SAND);
		return retVal;
	}
	
	public static final boolean targetInGrass(int positionX, int positionY) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(positionX, positionY, Layers.LAYER_META_GROUNDTYPES);
		boolean retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(TileType.GROUND_TYPE_GRASS);
		return retVal;
	}
	
	public static final boolean targetInSolidTerrain(int positionX, int positionY) {
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		int tileId = map.getTileId(positionX, positionY, Layers.LAYER_META_TERRAINTYPES);
		boolean retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_TERRAINTYPES, tileId).equals(TileType.TERRAIN_TYPE_ROAD);
		return retVal;
	}
	
}
