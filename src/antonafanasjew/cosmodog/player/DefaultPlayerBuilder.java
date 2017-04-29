package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
//		player.setMaxLife(100);
//		player.setLife(100);
		player.setPositionX(248);
		player.setPositionY(113);
	}

}
