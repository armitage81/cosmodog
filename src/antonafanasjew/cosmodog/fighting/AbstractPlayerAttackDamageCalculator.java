package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Abstract class for calculation of damage dealt by the player characters to an enemy. 
 */
public abstract class AbstractPlayerAttackDamageCalculator extends AbstractDamageCalculator {

	public static final int CRITICAL_HIT_CHANCE_IN_PERCENT = 5;

	@Override
	protected int damageInternal(Actor attacker, Actor defender) {

		Player player = (Player) attacker;
		Enemy enemy = (Enemy) defender;
		
		int damage = playerAttackDamageInternal(player, enemy);

		return damage;

	}

	protected abstract int playerAttackDamageInternal(Player player, Enemy enemy);

	@Override
	public boolean criticalHit(Actor attacker, Actor defender) {
		return System.currentTimeMillis() % 100 < CRITICAL_HIT_CHANCE_IN_PERCENT;
	}
}
