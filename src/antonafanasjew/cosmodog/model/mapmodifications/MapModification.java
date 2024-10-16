package antonafanasjew.cosmodog.model.mapmodifications;

import java.io.Serializable;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Represents a persistent modification of a tiled map.
 * It will be used mainly to represent changes of tiles on the tiled map
 * that have been done by the player during the game. If, for example, the player uses a lever to 
 * blast a wall, the tile of the wall should be changed to a tile of rubble. This is not been done
 * by modifying the tiled map tile itself, but via a map modification.
 * if the player saves the game and then loads it again, the original tiled map will be loaded and then
 * the modification will be used to return changed tile ids instead of the original ones.
 * Take note: An object of MapModification represents ALL modifications that have been done to the tiled map.
 */
public interface MapModification extends Serializable {

	int getTileId(CustomTiledMap tiledMap, Position position, int layerIndex);
	
	void modifyTile(Position position, int layerIndex, TileType tileType);
	
}
