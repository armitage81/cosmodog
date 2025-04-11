package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

/**
 * Abstract implementation of the collision validator to hold common logic.
 * Currently, it implements no passage status in case the coordinates of the tile are beyond map borders. 
 */
public abstract class AbstractCollisionValidator implements CollisionValidator {

	/**
	 * Returns 'no passage' for tiles beyond map borders and delegates the concrete validation to sub classes.
	 */
	@Override
	public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		
		CollisionStatus notPassable = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED);
		
		if (!entrance.getPosition().inMapBounds(cosmodogGame.mapOfPlayerLocation())) {
			return notPassable;
		}
		
		return calculateStatusWithinMap(cosmodogGame, actor, map, entrance);
		
	}

	public abstract CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance);

}
