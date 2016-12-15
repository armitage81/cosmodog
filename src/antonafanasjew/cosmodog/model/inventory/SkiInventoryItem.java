package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing the ski.
 *
 */
public class SkiInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the ski item.
	 */
	public SkiInventoryItem() {
		super(InventoryItemType.SKI);
	}
	
	@Override
	public String description() {
		return "Simple ski equipment including goggles and sticks. "
				+ "Snow won't be an obstacle any more and you can cross it as if it were plain terrain.";
	}
}
