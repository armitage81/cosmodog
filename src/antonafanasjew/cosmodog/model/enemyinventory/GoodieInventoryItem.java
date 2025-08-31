package antonafanasjew.cosmodog.model.enemyinventory;

import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

/**
 * This inventory item type is designed for enemies only. Description is irrelevant.
 */
public class GoodieInventoryItem extends EnemyInventoryItem {
	
	private GoodieType goodieType;

	public GoodieInventoryItem(GoodieType goodieType) {
		super(EnemyInventoryItemType.GOODIE);
		this.goodieType = goodieType;
	}
	
	public GoodieType getGoodieType() {
		return goodieType;
	}

}