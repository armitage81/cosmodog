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
	
	@Override
	public String description() {
		return "This little device detects radiation. "
				+ "The \"RAD\" window of the interface shows the eight adjacent tiles around you. "
				+ "Fields polluted by radiation are red, safe fields are blue. "
				+ "Use the detector to find a safe way and avoid radioactive damage.";
	}
	
}
