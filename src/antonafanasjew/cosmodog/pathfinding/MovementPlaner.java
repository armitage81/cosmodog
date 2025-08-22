package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Entrance;

public interface MovementPlaner {

	MovementPlan calculateMovementPlan(
			Actor actor,
			int costBudget,
			CollisionValidator collisionValidator,
			Entrance targetEntrance
	);
	
}
