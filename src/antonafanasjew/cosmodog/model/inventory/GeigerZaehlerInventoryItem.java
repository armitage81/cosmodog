package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing the Geiger Zähler.
 *
 */
public class GeigerZaehlerInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the Geiger Zähler item.
	 */
	public GeigerZaehlerInventoryItem() {
		super(InventoryItemType.GEIGERZAEHLER);
	}
	
}
