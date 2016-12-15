package antonafanasjew.cosmodog.model.inventory;


/**
 * Wraps a cosmodog collectible as inventory item.
 * A speciality for this item is that it has a number. This number increase with every collected
 * insight/cosmodog figure.
 */
public class InsightInventoryItem extends CountableInventoryItem {

	private static final long serialVersionUID = -5819631394123309541L;

	/**
	 * Constructor. Initializes the insight inventory item.
	 */
	public InsightInventoryItem() {
		super(InventoryItemType.INSIGHT);
	}

	@Override
	public String description() {
		return "These mysterious items are immaterial and intangible. They are merely abstract ideas representing your insight in the alien philosophy. "
				+ "Find all of them to achieve your goal.";
	}
	
}
