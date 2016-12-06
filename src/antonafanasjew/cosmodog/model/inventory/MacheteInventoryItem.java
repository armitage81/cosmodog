package antonafanasjew.cosmodog.model.inventory;

/**
 * Inventory item for the machete tool
 *
 */
public class MacheteInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;
	
	/**
	 * Constructor. Creates a machete item.
	 */
	public MacheteInventoryItem() {
		super(InventoryItemType.MACHETE);
	}
	
}
