package antonafanasjew.cosmodog.model.inventory;

/**
 * Describes multi-instance items, like insights (there are 20 of them to collect)
 */
public class CountableInventoryItem extends InventoryItem {

	private static final long serialVersionUID = -329618952210291080L;

	private int number = 1;

	public CountableInventoryItem(String inventoryItemType) {
		super(inventoryItemType);
	}
	
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public void increaseNumber() {
		number++;
	}


}
