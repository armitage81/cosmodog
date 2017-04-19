package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestWeaponsPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				
	}

}