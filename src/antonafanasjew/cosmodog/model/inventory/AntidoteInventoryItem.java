package antonafanasjew.cosmodog.model.inventory;

/**
 * Item type representing the Antidote.
 *
 */
public class AntidoteInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -4515905925505349086L;

	/**
	 * Constructor. Initializes the Antidote item.
	 */
	public AntidoteInventoryItem() {
		super(InventoryItemType.ANTIDOTE);
	}
	
	@Override
	public String description() {
		return "The suringe with the antidote. It helps against poison. Possible side effect list includes nose bleeding and good mood. There is enough stuff for a month.";
	}
	
}
