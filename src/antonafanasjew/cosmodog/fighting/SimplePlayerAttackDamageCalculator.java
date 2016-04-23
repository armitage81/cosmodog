package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.domains.ArmorType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;

/**
 * Returns the damage as difference between players constant base damage and the damage absorption from the enemy's armor type.
 * Cannot be less than 1.
 */
public class SimplePlayerAttackDamageCalculator extends AbstractPlayerAttackDamageCalculator {

	public static final int SIMPLE_PLAYER_DAMAGE = 1;
	
	@Override
	public int playerAttackDamageInternal(Player player, Enemy enemy) {
		
		ArmorType enemyArmorType = enemy.getArmorType();
		
		ArsenalInventoryItem arsenal = (ArsenalInventoryItem)player.getInventory().get(InventoryItem.INVENTORY_ITEM_ARSENAL);
		
		WeaponType selectedWeaponType = arsenal.getSelectedWeaponType();
		
		int damage = selectedWeaponType == null ? SIMPLE_PLAYER_DAMAGE : selectedWeaponType.getDamage(enemyArmorType);
		
		return damage;
		
	}

}
