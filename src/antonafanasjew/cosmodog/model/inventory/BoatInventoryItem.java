package antonafanasjew.cosmodog.model.inventory;

/**
 * Inventory item for the boat tool
 *
 */
public class BoatInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;
	
	/**
	 * Constructor. Creates a boat item.
	 */
	public BoatInventoryItem() {
		super(InventoryItemType.BOAT);
	}
	
	@Override
	public String description() {
		return "A simple inflatable boat that fits in your back pack. Allows you to cross water.";
	}
	
}
