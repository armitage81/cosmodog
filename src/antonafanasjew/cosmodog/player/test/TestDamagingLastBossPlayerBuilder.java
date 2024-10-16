package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SoftwareInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;

public class TestDamagingLastBossPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPosition(Position.fromCoordinates(231, 258));
		player.setMaxLife(100);
		player.setLife(100);
		
		SoftwareInventoryItem software = (SoftwareInventoryItem)player.getInventory().get(InventoryItemType.SOFTWARE);
		
		for (int i = 0; i < Constants.NUMBER_OF_SOFTWARE_PIECES_IN_GAME; i++) {
			
			if (software == null) {
				software = new SoftwareInventoryItem();
				player.getInventory().put(InventoryItemType.SOFTWARE, software);
			} else {
				software.increaseNumber();
			}
			
		}
		
		Arsenal arsenal = player.getArsenal();
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.RIFLE, 1));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN, 1));
		arsenal.addWeaponToArsenal(new Weapon(WeaponType.RPG, 1));
		
	}

}