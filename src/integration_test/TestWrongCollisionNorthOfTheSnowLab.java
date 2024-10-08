package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestWrongCollisionNorthOfTheSnowLab {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPositionX(255);
				player.setPositionY(356);
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.AXE, new AxeInventoryItem());
				player.getInventory().put(InventoryItemType.PICK, new PickInventoryItem());
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.MACHETE, new MacheteInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());

				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
