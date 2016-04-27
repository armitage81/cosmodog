package antonafanasjew.cosmodog.model.inventory;

import java.util.HashMap;

/**
 * Represents player's inventory.
 * Only one item of each type can be stored.
 */
public class Inventory extends HashMap<InventoryItemType, InventoryItem> {
	
	/**
	 * Constructor. Initializes arsenal as the only inventory item type.
	 */
	public Inventory() {
		put(InventoryItemType.ARSENAL, new ArsenalInventoryItem());
	}
	
	private static final long serialVersionUID = 2066865979982208629L;

	/**
	 * Indicates whether an item for the given item key exists in the inventory.
	 * @param itemKey Item key for which the information is needed.
	 * @return true if item with this key exists, false otherwise.
	 */
	public boolean hasItem(InventoryItemType itemKey) {
		return get(itemKey) != null;
	}
	
	/**
	 * Returns the number of items in the inventory.
	 * @return Number of items in the inventory.
	 */
	public int numberOfItems() {
		return keySet().size();
	}
	
	/**
	 * Indicates whether a vehicle is in the inventory.
	 * @return true if player 'has' the vehicle, false otherwise.
	 */
	public boolean hasVehicle() {
		return hasItem(InventoryItemType.VEHICLE);
	}
	
	/**
	 * The player inventory can have 3 states in regards of the vehicle inventory type:
	 * 'has', 'has not' and 'exiting'. The last one is indicating that the player is in the
	 * vehicle but about to leave it. That might matter in collision validators, when the 
	 * status change from 'has' to 'has not' depends on the passability of the exiting tile.
	 * This method returns the 'exiting' indication.
	 * @return true, if the vehicle inventory item state is 'exiting', false if it is 'has' or 'has not'.
	 */
	public boolean exitingVehicle() {
		return hasVehicle() && ((VehicleInventoryItem)get(InventoryItemType.VEHICLE)).isExiting();
	}
	
}
