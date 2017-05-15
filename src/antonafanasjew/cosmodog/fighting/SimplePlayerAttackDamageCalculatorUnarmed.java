package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.PositionUtils;

public class SimplePlayerAttackDamageCalculatorUnarmed extends AbstractPlayerAttackDamageCalculator {

	public static final int SIMPLE_PLAYER_DAMAGE = 1;
	
	@Override
	public int playerAttackDamageInternal(Player player, Enemy enemy) {
		
		if (Features.getInstance().featureOn(Features.FEATURE_GODFISTS)) {
			return 1000;
		}
		
		ArmorType enemyArmorType = enemy.getArmorType();
		
		WeaponType selectedWeaponType = WeaponType.FISTS;
		
		int damage = selectedWeaponType == null ? SIMPLE_PLAYER_DAMAGE : selectedWeaponType.getDamage(enemyArmorType);
		
		DirectionType playerDirection = player.getDirection();
		DirectionType enemyDirection = enemy.getDirection();
		DirectionType enemyRelatedToPlayerDirection = PositionUtils.targetDirection(player, enemy);
		
		boolean playerLooksAtEnemy = playerDirection.equals(enemyRelatedToPlayerDirection);
		boolean enemyLooksAway = enemyDirection.equals(playerDirection);
		
		if (playerLooksAtEnemy && enemyLooksAway) {
			damage *= 2;
		}
		
		return damage;
		
	}

}
