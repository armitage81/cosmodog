package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.model.portals.Entrance;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;

public class StayStillMovementPlaner extends AbstractMovementPlaner {

	@Override
	protected MovementPlan calculateMovementPlanInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
        return MovementPlan.instance(actor.getPosition());
	}

}
