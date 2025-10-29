package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

import java.util.HashSet;
import java.util.Set;

public class FightActionPhaseFactory {

	public static AttackActionPhase attackActionPhase(FightPlan.FightPhasePlan fightPhasePlan) {
		
		Actor attacker = fightPhasePlan.isPlayerAttack() ? fightPhasePlan.getPlayer() : fightPhasePlan.getEnemy();
		
		if (attacker instanceof Player) {
			return new PlayerAttackActionPhase(fightPhasePlan);
		} else {
			Enemy enemy = (Enemy)attacker;
			UnitType unitType = enemy.getUnitType();
			if (unitType == UnitType.ARTILLERY) {
				return new ArtilleryAttackActionPhase(fightPhasePlan);
			} else {
				return new DefaultEnemyAttackActionPhase(fightPhasePlan);
			}
		}
	}

	/**
	 * Creates an enemy destruction action phase from the player and the enemy.
	 * <p>
	 * Take note: No fight phase result is needed to create this action.
	 * Take note: The enemy can drop an item. This also happens in the destruction action phase.
	 *
	 * @param player Player instance.
	 * @param enemy Enemy instance.
	 * @return The enemy destruction action phase.
	 */
	public static EnemyDestructionActionPhase enemyDestructionActionPhase(Player player, Enemy enemy) {
		Set<Enemy> enemies = new HashSet<>();
		enemies.add(enemy);
		return new DefaultEnemyDestructionActionPhase(player, enemies);
	}
}
