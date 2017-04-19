package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestManyEnemiesPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPositionX(33);
		player.setPositionY(326);
		player.setMaxLife(100);
		player.setLife(100);
		VehicleInventoryItem vii = new VehicleInventoryItem(new Vehicle());
		player.getInventory().put(InventoryItemType.VEHICLE, vii);
		
	}

}
