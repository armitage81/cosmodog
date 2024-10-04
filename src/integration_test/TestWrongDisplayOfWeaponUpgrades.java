package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestWrongDisplayOfWeaponUpgrades {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setMaxLife(100);
				player.setLife(100);

				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));

				Weapon pistol = player.getArsenal().getWeaponsCopy().get(WeaponType.PISTOL);
				pistol.upgrade();
				pistol.upgrade();

				Weapon shotgun = player.getArsenal().getWeaponsCopy().get(WeaponType.SHOTGUN);
				shotgun.upgrade();
				shotgun.upgrade();
				shotgun.setAmmunition(0);

				Weapon rifle = player.getArsenal().getWeaponsCopy().get(WeaponType.RIFLE);
				rifle.upgrade();

				Weapon machinegun = player.getArsenal().getWeaponsCopy().get(WeaponType.MACHINEGUN);
				machinegun.upgrade();
				machinegun.setAmmunition(1);

				Weapon rpg = player.getArsenal().getWeaponsCopy().get(WeaponType.RPG);
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
