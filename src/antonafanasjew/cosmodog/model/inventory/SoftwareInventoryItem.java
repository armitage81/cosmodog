package antonafanasjew.cosmodog.model.inventory;


public class SoftwareInventoryItem extends CountableInventoryItem {

	private static final long serialVersionUID = -5819631394123309541L;

	/**
	 * Constructor. Initializes the software inventory item.
	 */
	public SoftwareInventoryItem() {
		super(InventoryItemType.SOFTWARE);
	}

	@Override
	public String description() {
		return "This is a chip with the piece of software from the super computer. Apparently, the software was considered as dangerous and deliberately "
				+ "separated into multiple fragments. Find all of them and bring them to the data center to boot the software.";
	}
	
}
