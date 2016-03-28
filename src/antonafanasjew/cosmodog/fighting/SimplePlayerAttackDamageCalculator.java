package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;

/**
 * Returns the damage as difference between players constant base damage and the damage absorption from the enemy's armor type.
 * Cannot be less than 1.
 */
public class SimplePlayerAttackDamageCalculator extends AbstractPlayerAttackDamageCalculator {

	public static final int SIMPLE_PLAYER_DAMAGE = 10;
	
	@Override
	public int playerAttackDamageInternal(Player player, Enemy enemy) {
		int damageAbsorption = enemy.getArmorType().getDamageAbsorption();
		
		int damage = SIMPLE_PLAYER_DAMAGE - damageAbsorption;
		
		if (damage < 1) {
			damage = 1;
		}
		
		return damage;
		
	}

}
