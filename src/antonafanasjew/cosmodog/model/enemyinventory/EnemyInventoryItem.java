package antonafanasjew.cosmodog.model.enemyinventory;

import java.io.Serializable;

public abstract class EnemyInventoryItem implements Serializable {

	private EnemyInventoryItemType inventoryItemType;

	public EnemyInventoryItem(EnemyInventoryItemType inventoryItemType) {
		this.inventoryItemType = inventoryItemType;
	}
	
	public EnemyInventoryItemType getInventoryItemType() {
		return inventoryItemType;
	}

}
