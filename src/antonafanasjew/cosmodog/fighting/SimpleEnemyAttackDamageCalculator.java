package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

public class SimpleEnemyAttackDamageCalculator extends AbstractEnemyAttackDamageCalculator {

	@Override
	public int enemyAttackDamageInternal(Enemy enemy, Player player) {
		return enemy.getWeaponType().getBasicDamage();
	}

}
