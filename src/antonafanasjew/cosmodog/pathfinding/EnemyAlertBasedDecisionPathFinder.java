package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementActionResult;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class EnemyAlertBasedDecisionPathFinder extends AbstractPathFinder {

	private PathFinder idlePathFinder;
	private PathFinder alertedPathFinder;
	
	public EnemyAlertBasedDecisionPathFinder(PathFinder idlePathFinder, PathFinder alertedPathFinder) {
		this.idlePathFinder = idlePathFinder;
		this.alertedPathFinder = alertedPathFinder;
	}


	@Override
	protected MovementActionResult calculateMovementResultInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, TravelTimeCalculator travelTimeCalculator, MovementActionResult playerMovementActionResult) {

		PathFinder relevantPathFinder;
		Player player = ApplicationContextUtils.getPlayer();
		boolean playerHasPlatform = player.getInventory().hasPlatform();
		
		Enemy enemy = (Enemy)actor;
	
		if (enemy.getAlertLevel() > 0 && !playerHasPlatform) {
			relevantPathFinder = alertedPathFinder;
		} else {
			relevantPathFinder = idlePathFinder;
		}

		
		return relevantPathFinder.calculateMovementResult(actor, costBudget, collisionValidator, travelTimeCalculator, playerMovementActionResult);
		
	}

	
}
