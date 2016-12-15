package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing the Supply Tracker.
 *
 */
public class SupplyTrackerInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the Supply Tracker item.
	 */
	public SupplyTrackerInventoryItem() {
		super(InventoryItemType.SUPPLYTRACKER);
	}
	
	@Override
	public String description() {
		return "You did not expect to see it again. You found this tracker after it survived the crash of Cosmodog. "
				+ "It looks like an electronic compass except it will show in the direction of the next supply box.";
	}
	
}
