package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.CountableInventoryItem;
import antonafanasjew.cosmodog.model.inventory.Inventory;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Triggers in case a given item exists in players inventory with as many exemplars as given.
 * (for not countable items, only 0 and 1 are relevant)
 * 
 */
public class InventoryBasedTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = -6139617119100916731L;

	private int numberOfCollectibleInstances;
	private InventoryItemType inventoryItemType;
	
	public InventoryBasedTrigger(InventoryItemType inventoryItemType, int numberOfCollectibleInstances) {
		super();
		this.inventoryItemType = inventoryItemType;
		this.numberOfCollectibleInstances = numberOfCollectibleInstances;
	}

	@Override
	public boolean accept(GameEvent event) {
		Player player = ApplicationContextUtils.getPlayer();
		Inventory inventory = player.getInventory();
		InventoryItem inventoryItem = inventory.get(inventoryItemType);
		
		int noOfInstances;
		
		if (inventoryItem instanceof CountableInventoryItem) {
			noOfInstances = inventoryItem == null ? 0 : ((CountableInventoryItem)inventoryItem).getNumber();
		} else {
			noOfInstances = inventoryItem == null ? 0 : 1;
		}
		
		return noOfInstances == numberOfCollectibleInstances;
		
	}

}
