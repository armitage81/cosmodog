package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
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
		
		ArsenalInventoryItem arsenal = (ArsenalInventoryItem)player.getInventory().get(InventoryItemType.ARSENAL);
		
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		
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
