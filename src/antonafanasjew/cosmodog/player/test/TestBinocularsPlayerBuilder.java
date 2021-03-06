package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestBinocularsPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		for (int i = 0; i < 20; i++) {
			player.getGameProgress().addInfobit();
		}
		
		player.setPositionX(293);
		player.setPositionY(101);
		player.setMaxLife(100);
		player.setLife(100);
		
		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
	}

}
