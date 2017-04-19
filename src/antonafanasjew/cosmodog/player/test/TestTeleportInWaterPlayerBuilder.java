package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestTeleportInWaterPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
		player.setPositionX(308);
		player.setPositionY(53);
				
	}

}