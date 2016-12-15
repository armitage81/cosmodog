package antonafanasjew.cosmodog.model.inventory;

import java.io.Serializable;

/**
 * Inventory item as item in the player's inventory container.
 * Use it as a wrapper around the actual item.
 */
public abstract class InventoryItem implements Serializable {

	private static final long serialVersionUID = -8368308128040034996L;
	
	public static final int INVENTORY_ITEM_INSIGHT_MAX_COUNT = 20;
	
	public static final int INVENTORY_ITEM_SOFTWARE_MAX_COUNT = 16;
	
	private InventoryItemType inventoryItemType;

	public InventoryItem(InventoryItemType inventoryItemType) {
		this.inventoryItemType = inventoryItemType;
	}
	
	public InventoryItemType getInventoryItemType() {
		return inventoryItemType;
	}
	
	public abstract String description();

}
