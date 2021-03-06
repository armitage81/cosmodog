package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestCollectingNeededInsightsPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPositionX(363);
		player.setPositionY(392);
		
		InsightInventoryItem insight = (InsightInventoryItem)player.getInventory().get(InventoryItemType.INSIGHT);
		
		for (int i = 0; i < Constants.MIN_INSIGHTS_TO_OPEN_ALIEN_BASE - 1; i++) {
			
			if (insight == null) {
				insight = new InsightInventoryItem();
				player.getInventory().put(InventoryItemType.INSIGHT, insight);
			} else {
				insight.increaseNumber();
			}
			
		}
		
	}

}