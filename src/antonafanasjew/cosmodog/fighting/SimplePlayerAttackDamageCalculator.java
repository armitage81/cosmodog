package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.util.PositionUtils;

/**
 * Returns the damage as difference between players constant base damage and the damage absorption from the enemy's armor type.
 * Cannot be less than 1.
 */
public class SimplePlayerAttackDamageCalculator extends AbstractPlayerAttackDamageCalculator {

	public static final int SIMPLE_PLAYER_DAMAGE = 1;
	
	@Override
	public int playerAttackDamageInternal(Player player, Enemy enemy) {
		
		ArmorType enemyArmorType = enemy.getArmorType();
		
		Arsenal arsenal = player.getArsenal();
		
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		
		int damage;
		if (Features.getInstance().featureOn(Features.FEATURE_GODFISTS)) {
			damage = selectedWeaponType == WeaponType.FISTS ? 1000 : selectedWeaponType.getDamage(enemyArmorType);
		} else {
			damage = selectedWeaponType == null ? SIMPLE_PLAYER_DAMAGE : selectedWeaponType.getDamage(enemyArmorType);
		}
		
		
		
		
		DirectionType playerDirection = player.getDirection();
		DirectionType enemyDirection = enemy.getDirection();
		DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(player, enemy);
		
		boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
		boolean enemyLooksAway = enemyDirection.equals(playerDirection);
		//Disallow critical hits for the ranged and stationary units as they always see the player if he approaches.
		boolean criticalHitsAllowed = !enemy.getUnitType().isRangedUnit() && !(enemy.getSpeedFactor() == 0.0f);
		
		if (playerLooksAtEnemy && enemyLooksAway && criticalHitsAllowed) {
			damage *= 2;
		}
		
		return damage;
		
	}

}
