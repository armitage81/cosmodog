package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class EnemyAlertBasedDecisionMovementPlaner extends AbstractMovementPlaner {

	private final MovementPlaner idleMovementPlaner;
	private final MovementPlaner alertedMovementPlaner;
	
	public EnemyAlertBasedDecisionMovementPlaner(MovementPlaner idleMovementPlaner, MovementPlaner alertedMovementPlaner) {
		this.idleMovementPlaner = idleMovementPlaner;
		this.alertedMovementPlaner = alertedMovementPlaner;
	}

	@Override
	protected MovementPlan calculateMovementPlanInternal(Actor actor, int costBudget, CollisionValidator collisionValidator, Entrance playersTargetEntrance) {

		MovementPlaner relevantMovementPlaner;
		Player player = ApplicationContextUtils.getPlayer();
		boolean playerHasPlatform = player.getInventory().hasPlatform();
		
		Enemy enemy = (Enemy)actor;
	
		if (enemy.getAlertLevel() > 0 && !playerHasPlatform) {
			relevantMovementPlaner = alertedMovementPlaner;
		} else {
			relevantMovementPlaner = idleMovementPlaner;
		}

		return relevantMovementPlaner.calculateMovementPlan(actor, costBudget, collisionValidator, playersTargetEntrance);
		
	}

	
}
