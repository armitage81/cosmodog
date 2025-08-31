package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.enemyinventory.AmmoInventoryItem;
import antonafanasjew.cosmodog.model.enemyinventory.EnemyInventoryItem;
import antonafanasjew.cosmodog.model.enemyinventory.KeyInventoryItem;
import antonafanasjew.cosmodog.model.inventory.AntidoteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.AxeInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.enemyinventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.JacketInventoryItem;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MacheteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MineDetectorInventoryItem;
import antonafanasjew.cosmodog.model.inventory.PickInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Key;

public class DroppedCollectibleFactory {

	public static Collectible createCollectibleFromDroppedItem(EnemyInventoryItem item) {
		Collectible retVal = null;
		
		if (item instanceof GoodieInventoryItem goodieInventoryItem) {
			retVal = new CollectibleGoodie(goodieInventoryItem.getGoodieType());
		} else if (item instanceof AmmoInventoryItem ammoInventoryItem) {
			retVal = new CollectibleAmmo(ammoInventoryItem.getWeaponType());
		} else if (item instanceof KeyInventoryItem keyInventoryItem) {
			DoorType doorType = keyInventoryItem.getDoorType();
			Key key = new Key();
			key.setDoorType(doorType);
			retVal = new CollectibleKey(key);
		}
		return retVal;
	}
	
}
