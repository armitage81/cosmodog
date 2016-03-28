package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Validator for water places. It checks whether a given tiled map tile is a 'water place', that is, contains water.
 */
public interface WaterValidator {
	
	/**
	 * Returns true if water is in reach of the given tile, false otherwise.
	 * 
	 * @param actor Actor for whom the validation takes place.
	 * @param map Tiled map with the tile information.
	 * @param tileX x coordinate of tiles location.
	 * @param tileY y coordinate of tiles location.
	 * @return true if the tile is a water place (f.i. as taken from the meta layers of the tiled map), false otherwise.
	 */
	boolean waterInReach(Actor actor, CustomTiledMap map, int tileX, int tileY);
}
