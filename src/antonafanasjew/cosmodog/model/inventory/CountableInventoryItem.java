package antonafanasjew.cosmodog.model.inventory;

/**
 * Describes multi-instance items, like insights (there are 33 of them to collect)
 */
public abstract class CountableInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -329618952210291080L;

	private int number = 1;

	/**
	 * Constructor. Creates the instance by initializing the item type.
	 */
	public CountableInventoryItem(InventoryItemType inventoryItemType) {
		super(inventoryItemType);
	}
	
	/**
	 * Returns the number of instances for the item.
	 * @return Number of collected instances.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the number of collected instances.
	 * @param number Number of collected instances.
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Increases the number of collected instances by one.
	 */
	public void increaseNumber() {
		number++;
	}

}
