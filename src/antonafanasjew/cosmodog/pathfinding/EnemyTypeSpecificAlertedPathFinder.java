package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;

public class EnemyTypeSpecificAlertedPathFinder extends AbstractPathFinder {

	TowardsPlayerPathFinder defaultPathFinder = new TowardsPlayerPathFinder();
	StayStillPathFinder stayStillPathFinder = new StayStillPathFinder();
	
	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator, MovementActionResult playerMovementActionResult) {
		Enemy enemy = (Enemy)actor;
		UnitType unitType = enemy.getUnitType();
		PathFinder pathFinder;
		if (unitType == UnitType.ARTILLERY) {
			pathFinder = stayStillPathFinder;
		} else {
			pathFinder = defaultPathFinder;
		}
		
		return pathFinder.calculateMovementResult(actor, costBudget, collisionValidator, travelTimeCalculator, playerMovementActionResult);
	}

}
