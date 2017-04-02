package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.domains.WeaponType;

/**
 * This inventory item is designed for enemies only. Description is irrelevant.
 */
public class AmmoInventoryItem extends InventoryItem {
	
	private static final long serialVersionUID = -4515905925505349086L;
	private WeaponType weaponType;

	public AmmoInventoryItem(WeaponType weaponType) {
		super(InventoryItemType.AMMO);
		this.weaponType = weaponType;
	}
	
	@Override
	public String description() {
		return weaponType.toString();
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

}
