package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;

/**
 * Abstract implementation of a path finder to hold common logic.
 */
public abstract class AbstractPathFinder implements PathFinder {

	@Override
	public MovementActionResult calculateMovementResult(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		return calculateMovementResultInternal(actor, costBudget, collisionValidator, playersTargetEntrance);
	}	

	protected abstract MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance);

}
