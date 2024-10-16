package antonafanasjew.cosmodog.waterplaces;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Validator for water places. It checks whether a given tiled map tile is a 'water place', that is, contains water.
 */
public interface WaterValidator {
	
	/**
	 * Returns true if water is in reach of the given tile, false otherwise.
	 * 
	 * @param actor Actor for whom the validation takes place.
	 * @param map Tiled map with the tile information.
	 * @param position position of the tile (in tiles, not pixels).
	 * @return true if the tile is a water place (f.i. as taken from the meta layers of the tiled map), false otherwise.
	 */
	boolean waterInReach(Actor actor, CosmodogMap map, Position position);
}
