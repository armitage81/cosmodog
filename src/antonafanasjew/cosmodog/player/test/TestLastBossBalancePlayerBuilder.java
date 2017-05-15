package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Key;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestLastBossBalancePlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.setMaxLife(70);
		player.setLife(70);
		player.setPositionX(227);
		player.setPositionY(258);
		
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));

		Key key = new Key();
		key.setDoorType(DoorType.whiteKeycardDoor);
		KeyRingInventoryItem krii = new KeyRingInventoryItem();
		player.getInventory().put(InventoryItemType.KEY_RING, krii);
		((KeyRingInventoryItem)player.getInventory().get(InventoryItemType.KEY_RING)).addKey(key);
		
		
		InsightInventoryItem insightInventoryItem = new InsightInventoryItem();
		for (int i = 0; i < Constants.NUMBER_OF_INSIGHTS_IN_GAME; i++) {
			insightInventoryItem.increaseNumber();
		}
		
		player.getInventory().put(InventoryItemType.INSIGHT, insightInventoryItem);
		
	}

}
