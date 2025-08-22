package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;

public abstract class AbstractMovementPlaner implements MovementPlaner {

	@Override
	public MovementPlan calculateMovementPlan(
			Actor actor,
			int costBudget,
			CollisionValidator collisionValidator,
			Entrance playersTargetEntrance
	) {
		return calculateMovementPlanInternal(actor, costBudget, collisionValidator, playersTargetEntrance);
	}	

	protected abstract MovementPlan calculateMovementPlanInternal(
			Actor actor,
			int costBudget,
			CollisionValidator collisionValidator,
			Entrance playersTargetEntrance);

}
