package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.domains.WeaponType;

public class CollectibleAmmo extends Collectible {

	private static final long serialVersionUID = 5997815770768935991L;
	private WeaponType weaponType;

	public CollectibleAmmo(WeaponType weaponType) {
		super(Collectible.CollectibleType.AMMO);
		this.setWeaponType(weaponType);
	}

	public WeaponType getWeaponType() {
		return weaponType;
	}

	public void setWeaponType(WeaponType weaponType) {
		this.weaponType = weaponType;
	}


}
