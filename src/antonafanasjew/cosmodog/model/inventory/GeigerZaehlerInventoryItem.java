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
		return "This little device is an indicator of radiation. "
				+ "Once you have stepped close to a radioactive area, it will start to chirp and warn you about the danger. "
				+ "Read the hints on your interface and find a safe way to avoid being contaminated.";
	}
	
}
