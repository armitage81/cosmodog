package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Abstract implementation of the collision validator to hold common logic.
 * Currently, it implements no passage status in case the coordinates of the tile are beyond map borders. 
 */
public abstract class AbstractCollisionValidator implements CollisionValidator {

	/**
	 * Returns 'no passage' for tiles beyond map borders and delegates the concrete validation to sub classes.
	 */
	@Override
	public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		CollisionStatus notPassable = CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED);
		
		if (tileX < 0) {
			return notPassable;
		}
		
		if (tileY < 0) {
			return notPassable;
		}
		
		if (tileX >= map.getWidth()) {
			return notPassable;
		}
		
		if (tileY >= map.getHeight()) {
			return notPassable;
		}
		
		return calculateStatusWithinMap(cosmodogGame, actor, map, tileX, tileY);
		
	}

	protected abstract CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY);

}
