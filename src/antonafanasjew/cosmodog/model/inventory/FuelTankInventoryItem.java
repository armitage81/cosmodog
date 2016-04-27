package antonafanasjew.cosmodog.model.inventory;

/**
 * Describes the fuel tank (can)
 */
public class FuelTankInventoryItem extends InventoryItem {

	private static final long serialVersionUID = 4816134204273274955L;
	
	/**
	 * Constructor. Initializes the fuel tank item.
	 */
	public FuelTankInventoryItem() {
		super(InventoryItemType.FUEL_TANK);
	}


}
