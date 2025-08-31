package antonafanasjew.cosmodog.model.enemyinventory;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class AmmoInventoryItem extends EnemyInventoryItem {
	
	private WeaponType weaponType;

	public AmmoInventoryItem(WeaponType weaponType) {
		super(EnemyInventoryItemType.AMMO);
		this.weaponType = weaponType;
	}
	
	public WeaponType getWeaponType() {
		return weaponType;
	}

}
