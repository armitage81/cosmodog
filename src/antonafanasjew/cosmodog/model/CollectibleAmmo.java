package antonafanasjew.cosmodog.model;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;

public class CollectibleAmmo extends Collectible {

	public static final int SCORE_POINTS_FOR_FOUND_AMMO = 500;
	
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

	@Override
	public boolean interactive(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		boolean interactive = true;
		
		CollectibleAmmo ammo = (CollectibleAmmo)piece;
		WeaponType weaponType = ammo.getWeaponType();
		Arsenal arsenal = player.getArsenal();
		if (arsenal.getWeaponsCopy().get(weaponType) == null) {
			interactive = false;
		}
		
		return interactive;
	}
	
	@Override
	public String toString() {
		return "Ammo: " + this.getWeaponType().toString();
	}

}
