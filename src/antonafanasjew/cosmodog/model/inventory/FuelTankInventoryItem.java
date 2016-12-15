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

	@Override
	public String description() {
		return "A canister full with diesel or whatever they use here to fuel their vehicles. You filled it at an abandoned gas station. "
				+ "You could also have driven inside with the car to fuel it directly.";
	}

}
