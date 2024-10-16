package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;

public class TestAlertStatusPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
		player.setPosition(Position.fromCoordinates(265, 374));
		player.setMaxLife(100);
		player.setLife(100);
		
	}

}
