package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.portals.Entrance;

public class EnemyTypeSpecificAlertedMovementPlaner extends AbstractMovementPlaner {

	TowardsPlayerMovementPlaner defaultMovementPlaner = new TowardsPlayerMovementPlaner();
	StayStillMovementPlaner stayStillMovementPlaner = new StayStillMovementPlaner();
	
	@Override
	protected MovementPlan calculateMovementPlanInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		Enemy enemy = (Enemy)actor;
		UnitType unitType = enemy.getUnitType();
		MovementPlaner movementPlaner;
		if (unitType == UnitType.ARTILLERY) {
			movementPlaner = stayStillMovementPlaner;
		} else {
			movementPlaner = defaultMovementPlaner;
		}
		
		return movementPlaner.calculateMovementPlan(actor, costBudget, collisionValidator, playersTargetEntrance);
	}

}
