package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing dynamite.
 *
 */
public class DynamiteInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the dynamite item.
	 */
	public DynamiteInventoryItem() {
		super(InventoryItemType.DYNAMITE);
	}
	
	@Override
	public String description() {
		return "The dynamite can be used to destroy crumbled rocks and break through underground passages";
	}
	
}
