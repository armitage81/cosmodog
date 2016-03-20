package antonafanasjew.cosmodog.model.inventory;


/**
 * Wraps a cosmodog collectible as inventory item.
 * A speciality for this item is that it has a number. This number increase with every collected
 * insight/cosmodog figure.
 */
public class InsightInventoryItem extends CountableInventoryItem {

	private static final long serialVersionUID = -5819631394123309541L;

	public InsightInventoryItem() {
		super(InventoryItem.INVENTORY_ITEM_INSIGHT);
	}

}
