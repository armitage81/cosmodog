package antonafanasjew.cosmodog.model.inventory;

import java.util.HashMap;

public class Inventory extends HashMap<String, InventoryItem> {
	
	private static final long serialVersionUID = 2066865979982208629L;

	public boolean hasItem(String itemKey) {
		return get(itemKey) != null;
	}
	
	public int numberOfItems() {
		return keySet().size();
	}
	
	public boolean hasVehicle() {
		return hasItem(InventoryItem.INVENTORY_ITEM_VEHICLE);
	}
	
	public boolean exitingVehicle() {
		return hasVehicle() && ((VehicleInventoryItem)get(InventoryItem.INVENTORY_ITEM_VEHICLE)).isExiting();
	}
	
	
}
