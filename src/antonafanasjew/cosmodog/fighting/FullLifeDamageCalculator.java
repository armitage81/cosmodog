package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

public class FullLifeDamageCalculator extends AbstractPlayerAttackDamageCalculator {

	public static final int SIMPLE_PLAYER_DAMAGE = 1;
	
	@Override
	public int playerAttackDamageInternal(Player player, Enemy enemy) {
		int damage = enemy.getLife();
		return damage;
	}

}
