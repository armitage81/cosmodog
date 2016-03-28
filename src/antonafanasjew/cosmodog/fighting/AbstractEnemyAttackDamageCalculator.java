package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Abstract class for calculation of damage dealt by an enemy to the player characters. 
 */
public abstract class AbstractEnemyAttackDamageCalculator extends AbstractDamageCalculator {

	@Override
	protected int damageInternal(Actor attacker, Actor defender) {
		Enemy enemy = (Enemy) attacker;
		Player player = (Player) defender;
		
		return enemyAttackDamageInternal(enemy, player);
	}

	protected abstract int enemyAttackDamageInternal(Enemy enemy, Player player);

}
