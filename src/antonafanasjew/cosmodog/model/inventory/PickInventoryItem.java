package antonafanasjew.cosmodog.model.inventory;

/**
 * Inventory item for the pick tool
 *
 */
public class PickInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;
	
	/**
	 * Constructor. Creates a pick item.
	 */
	public PickInventoryItem() {
		super(InventoryItemType.PICK);
	}
	
	@Override
	public String description() {
		return "A pick axe. Quite heavy and a bit rusty but still effective for destroying stones in your way.";
	}
	
}
