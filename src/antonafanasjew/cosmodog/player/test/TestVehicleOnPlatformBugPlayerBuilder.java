package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class TestVehicleOnPlatformBugPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPositionX(24);
		player.setPositionY(103);
		
		player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
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
		
		VehicleInventoryItem vii = new VehicleInventoryItem(new Vehicle());
		player.getInventory().put(InventoryItemType.VEHICLE, vii);
		
		player.setPositionX(351);
		player.setPositionY(386);
		
		
	}

}
