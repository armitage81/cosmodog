package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Abstract class for calculation of damage dealt by the player characters to an enemy. 
 */
public abstract class AbstractPlayerAttackDamageCalculator extends AbstractDamageCalculator {

	@Override
	protected int damageInternal(Actor attacker, Actor defender) {

		Player player = (Player) attacker;
		Enemy enemy = (Enemy) defender;
		
		return playerAttackDamageInternal(player, enemy);
	}

	protected abstract int playerAttackDamageInternal(Player player, Enemy enemy);

	
}
