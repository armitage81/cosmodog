package antonafanasjew.cosmodog.model;

public class CollectibleItem extends Collectible {

	private static final long serialVersionUID = 5790400319374924936L;

	public static final String ITEM_TYPE_BOAT = "item.boat";
	public static final String ITEM_TYPE_DYNAMITE = "item.dynamite";
	
	private String itemType;
	
	public CollectibleItem(String itemType) {
		super(Collectible.COLLECTIBLE_TYPE_ITEM);
		this.itemType = itemType;
		
	}

	public String getItemType() {
		return itemType;
	}

}
