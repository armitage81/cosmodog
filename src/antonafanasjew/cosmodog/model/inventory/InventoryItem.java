package antonafanasjew.cosmodog.model.inventory;

import java.io.Serializable;

public abstract class InventoryItem implements Serializable {

	private static final long serialVersionUID = -8368308128040034996L;
	
	public static final String INVENTORY_ITEM_VEHICLE = "inventoryitem.vehicle";
	public static final String INVENTORY_ITEM_BOAT = "inventoryitem.boat";
	public static final String INVENTORY_ITEM_DYNAMITE = "inventoryitem.dynamite";
	public static final String INVENTORY_ITEM_FUEL_TANK = "inventoryitem.fueltank";
	public static final String INVENTORY_ITEM_INSIGHT = "inventoryitem.insight";
	
	public static final int INVENTORY_ITEM_INSIGHT_MAX_COUNT = 20;
	
	private String inventoryItemType;

	public InventoryItem(String inventoryItemType) {
		this.inventoryItemType = inventoryItemType;
	}
	
	public String getInventoryItemType() {
		return inventoryItemType;
	}

}
