package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.fight.impl.ArtilleryAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.impl.DefaultEnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.impl.DefaultEnemyDestructionActionPhase;
import antonafanasjew.cosmodog.actions.fight.impl.PlayerAttackActionPhase;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

public class FightActionPhaseFactory {

	
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
	
	
	public static EnemyDestructionActionPhase enemyDestructionActionPhase(Player player, Enemy enemy) {
		return new DefaultEnemyDestructionActionPhase(player, enemy);
	}
	
	
}
