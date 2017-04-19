package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestAlertStatusPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
		player.setPositionX(265);
		player.setPositionY(374);
		player.setMaxLife(100);
		player.setLife(100);
		
	}

}
