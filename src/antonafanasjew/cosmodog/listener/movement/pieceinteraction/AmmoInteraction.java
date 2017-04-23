package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Map;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class AmmoInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CollectibleAmmo collectibleAmmo = (CollectibleAmmo)piece;
		
		WeaponType weaponType = collectibleAmmo.getWeaponType();
		
		Arsenal arsenal = player.getArsenal();
		Map<WeaponType, Weapon> owningWeapons = arsenal.getWeaponsCopy();
		
		Weapon weapon = owningWeapons.get(weaponType);
		
		if (weapon != null) {
			weapon.setAmmunition(weapon.getMaxAmmunition());
			
			//Will assign the first available weapon if not selected yet.
			arsenal.getSelectedWeaponType();
			
		}
		
	}

	
	@Override
	public String soundResource() {
		return SoundResources.SOUND_RELOAD;
	}
}
