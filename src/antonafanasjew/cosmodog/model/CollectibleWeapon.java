package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class CollectibleWeapon extends Collectible {

	private static final long serialVersionUID = 6580765085162911046L;

	private Weapon weapon;

	public CollectibleWeapon(Weapon weapon) {
		super(Collectible.CollectibleType.WEAPON);
		this.weapon = weapon;
	}

	public Weapon getWeapon() {
		return weapon;
	}


}
