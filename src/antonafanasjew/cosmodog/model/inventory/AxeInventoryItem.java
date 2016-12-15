package antonafanasjew.cosmodog.model.inventory;

/**
 * Inventory item for the axe tool
 *
 */
public class AxeInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;
	
	/**
	 * Constructor. Creates a axe item.
	 */
	public AxeInventoryItem() {
		super(InventoryItemType.AXE);
	}
	
	@Override
	public String description() {
		return "An old-fashioned axe that you have found in a military equipement box. "
				+ "Perhaps, it was supposed to be used by fire fighters. "
				+ "Use it to remove obstacles. "
				+ "Works only with dry trees.";
	}
}
