package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing the binoculars.
 *
 */
public class BinocularsInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the binoculars item.
	 */
	public BinocularsInventoryItem() {
		super(InventoryItemType.BINOCULARS);
	}
	
	@Override
	public String description() {
		return "These military binoculars are very useful as they let you survey the area better than your eyes can do. "
				+ "Press [Z] zo zoom in and out.";
	}
}
