package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Returns the collision status for a given tile based on actors properties. The status is basically the information
 * if the tile is passable or not passable.
 * 
 *  Take note: 'Collision' is misleading. In the most cases this interface is used for collision detection,
 *  but it also can be used for all obstacles for movement, like 'Car has no fuel' or 'passage condition not met'.
 *  
 */
public interface CollisionValidator {
	

	/**
	 * Returns the collision status for a tile.
	 * 
	 * @param cosmodogGame The cosmodog game object
	 * @param actor The actor object. (f.i. the player figure)
	 * @param cosmodogMap The tiled map object to retrieve the tile informations.
	 * @param entrance The position of the tile (in tiles, not in pixels) and also the direction from which the tile should be entered.
	 * @return The collision status object. It contains all information about the tiles as well as the passable/not passable property.
	 */
	CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CosmodogMap cosmodogMap, Entrance entrance);
	
}
