package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.inventory.AmmoInventoryItem;
import antonafanasjew.cosmodog.model.inventory.AntidoteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.AxeInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.JacketInventoryItem;
import antonafanasjew.cosmodog.model.inventory.KeyRingInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MacheteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.MineDetectorInventoryItem;
import antonafanasjew.cosmodog.model.inventory.PickInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Key;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class DroppedCollectibleFactory {

	public static Collectible createCollectibleFromDroppedItem(InventoryItem item) {
		Collectible retVal = null;
		
		if (item instanceof GoodieInventoryItem) {
			retVal = new CollectibleGoodie(((GoodieInventoryItem)item).getGoodieType());
		} else if (item instanceof ArsenalInventoryItem) {
			ArsenalInventoryItem arsenal = (ArsenalInventoryItem)item;
			WeaponType weaponType = arsenal.getWeaponsCopy().keySet().iterator().next();
			Weapon weapon = arsenal.getWeaponsCopy().get(weaponType);
			retVal = new CollectibleWeapon(weapon);
		} else if (item instanceof AmmoInventoryItem) {
			retVal = new CollectibleAmmo(((AmmoInventoryItem)item).getWeaponType());
		} else if (item instanceof KeyRingInventoryItem) {
			KeyRingInventoryItem keyring = (KeyRingInventoryItem)item;
			DoorType keyType = keyring.getKeysCopy().keySet().iterator().next();
			Key key = keyring.getKeysCopy().get(keyType);
			retVal = new CollectibleKey(key);
		} else if(item instanceof BoatInventoryItem) {
			retVal = new CollectibleTool(ToolType.boat);
		} else if(item instanceof DynamiteInventoryItem) {
			retVal = new CollectibleTool(ToolType.dynamite);
		} else if(item instanceof GeigerZaehlerInventoryItem) {
			retVal = new CollectibleTool(ToolType.geigerzaehler);
		} else if(item instanceof SupplyTrackerInventoryItem) {
			retVal = new CollectibleTool(ToolType.supplytracker);
		} else if(item instanceof BinocularsInventoryItem) {
			retVal = new CollectibleTool(ToolType.binoculars);
		} else if(item instanceof JacketInventoryItem) {
			retVal = new CollectibleTool(ToolType.jacket);
		} else if(item instanceof AntidoteInventoryItem) {
			retVal = new CollectibleTool(ToolType.antidote);
		} else if(item instanceof MineDetectorInventoryItem) {
			retVal = new CollectibleTool(ToolType.minedetector);
		} else if(item instanceof SkiInventoryItem) {
			retVal = new CollectibleTool(ToolType.ski);
		} else if(item instanceof PickInventoryItem) {
			retVal = new CollectibleTool(ToolType.pick);
		} else if(item instanceof AxeInventoryItem) {
			retVal = new CollectibleTool(ToolType.axe);
		} else if(item instanceof MacheteInventoryItem) {
			retVal = new CollectibleTool(ToolType.machete);
		}
		return retVal;
	}
	
}
