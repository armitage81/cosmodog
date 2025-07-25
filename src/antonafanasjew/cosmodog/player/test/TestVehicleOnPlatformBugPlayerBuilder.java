package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;

public class TestVehicleOnPlatformBugPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

		player.setPosition(Position.fromCoordinates(24, 103, MapType.MAIN));
		
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
		
	}

}
