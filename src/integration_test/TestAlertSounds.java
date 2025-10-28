package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

public class TestAlertSounds {

	/**
	 * - Not alerted enemy that gets alerted must play sound.
	 * - Already alerted enemy must not play sound.
	 * - In case there are multiple enemies and they are all alerted, sound must play only once.
	 * - An alerted enemy must play sound even if another enemy was alerted in the previous turn.
	 *
	 */

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setMaxLife(100);
				player.setLife(100);
				player.setPosition(Position.fromCoordinates(50, 300, ApplicationContextUtils.mapDescriptorMain()));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
