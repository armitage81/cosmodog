package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestCrumbledWallPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPositionX(24);
		player.setPositionY(103);
		
		player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
		player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		player.getGameProgress().addInfobank();
		
	}

}
