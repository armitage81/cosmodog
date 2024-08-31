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
		super(InventoryItemType.VEHICLE);
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

	@Override
	public String description() {
		int maxFuel = vehicle.getMaxFuel();
		int fuel = vehicle.getFuel();
		return "A real vehicle with a combustion engine. You saw one once in a museum but never sat inside one. "
				+ "Still, the controls are peanuts for a space cargo ship pilot. "
				+ "Look out for gas stations to refuel the car. "
				+ "If too late, you still can walk to one of them on foot and take a canister of fuel. "
				+ "Cars cannot drive in rough terrain but they are still very helpful as they provide protection against "
				+ "the enemy attacks and speed up your travel. Fuel: " + fuel + "/" + maxFuel + ".";
	}
	
}
