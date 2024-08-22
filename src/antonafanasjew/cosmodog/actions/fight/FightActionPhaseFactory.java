package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Factory for creating fight action phases.
 * <p>
 * It has static factory methods to provide a simple way to create action phases for
 * attacks and enemy destruction.
 */
public class FightActionPhaseFactory {

	/**
	 * Creates an attack action phase based on the fight phase result.
	 * It can be a player attack, a melee enemy attack or a ranged enemy attack.
	 *
	 * @param fightPhaseResult Fight phase result including the attacker, the target and the damage dealt.
	 * @return The action phase which can be one of: PlayerAttackActionPhase, DefaultEnemyAttackActionPhase or ArtilleryAttackActionPhase.
	 */
	public static AttackActionPhase attackActionPhase(FightActionResult.FightPhaseResult fightPhaseResult) {
		
		Actor attacker = fightPhaseResult.isPlayerAttack() ? fightPhaseResult.getPlayer() : fightPhaseResult.getEnemy();
		
		if (attacker instanceof Player) {
			return new PlayerAttackActionPhase(fightPhaseResult);
		} else {
			Enemy enemy = (Enemy)attacker;
			UnitType unitType = enemy.getUnitType();
			if (unitType == UnitType.ARTILLERY) {
				return new ArtilleryAttackActionPhase(fightPhaseResult);
			} else {
				return new DefaultEnemyAttackActionPhase(fightPhaseResult);
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
		return new DefaultEnemyDestructionActionPhase(player, enemy);
	}
}
