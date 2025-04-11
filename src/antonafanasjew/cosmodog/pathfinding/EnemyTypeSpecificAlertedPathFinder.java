package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.portals.Entrance;

public class EnemyTypeSpecificAlertedPathFinder extends AbstractPathFinder {

	TowardsPlayerPathFinder defaultPathFinder = new TowardsPlayerPathFinder();
	StayStillPathFinder stayStillPathFinder = new StayStillPathFinder();
	
	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {
		Enemy enemy = (Enemy)actor;
		UnitType unitType = enemy.getUnitType();
		PathFinder pathFinder;
		if (unitType == UnitType.ARTILLERY) {
			pathFinder = stayStillPathFinder;
		} else {
			pathFinder = defaultPathFinder;
		}
		
		return pathFinder.calculateMovementResult(actor, costBudget, collisionValidator, playersTargetEntrance);
	}

}
