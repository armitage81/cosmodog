package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing the Supply Tracker.
 *
 */
public class JacketInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the Jacket item.
	 */
	public JacketInventoryItem() {
		super(InventoryItemType.JACKET);
	}
	
	@Override
	public String description() {
		return "A warm red jacket, worn but tidy. It will be invaluable in snow areas as wearing it is the only way to not freeze in the cold.";
	}
	
}
