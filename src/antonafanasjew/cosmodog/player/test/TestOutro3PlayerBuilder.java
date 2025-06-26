package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;

public class TestOutro3PlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPosition(Position.fromCoordinates(211, 250, MapType.MAIN));
		
		InsightInventoryItem insight = (InsightInventoryItem)player.getInventory().get(InventoryItemType.INSIGHT);
		
		for (int i = 0; i < 33; i++) {
			
			if (insight == null) {
				insight = new InsightInventoryItem();
				player.getInventory().put(InventoryItemType.INSIGHT, insight);
			} else {
				insight.increaseNumber();
			}
			
		}
		
	}

}
