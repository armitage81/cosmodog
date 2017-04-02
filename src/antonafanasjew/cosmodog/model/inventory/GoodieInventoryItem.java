package antonafanasjew.cosmodog.model.inventory;

import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;

/**
 * This inventory item type is designed for enemies only. Description is irrelevant.
 */
public class GoodieInventoryItem extends InventoryItem {
	
	private static final long serialVersionUID = -4515905925505349086L;
	private GoodieType goodieType;

	public GoodieInventoryItem(GoodieType goodieType) {
		super(InventoryItemType.GOODIE);
		this.goodieType = goodieType;
	}
	
	@Override
	public String description() {
		return goodieType.toString();
	}

	public GoodieType getGoodieType() {
		return goodieType;
	}

}