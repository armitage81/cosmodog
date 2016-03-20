package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.model.actors.Vehicle;

/**
 * Wraps a collectible vehicle as an inventory item.
 */
public class VehicleInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	private Vehicle vehicle;
	private boolean exiting;

	public VehicleInventoryItem(Vehicle vehicle) {
		super(InventoryItem.INVENTORY_ITEM_VEHICLE);
		this.vehicle = vehicle;
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public boolean isExiting() {
		return exiting;
	}

	public void setExiting(boolean exiting) {
		this.exiting = exiting;
	}
	
}
