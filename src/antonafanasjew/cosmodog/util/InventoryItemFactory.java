package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Key;

public class InventoryItemFactory {

	public static InventoryItem createInventoryItem(Collectible collectible) {
		if (collectible instanceof CollectibleGoodie) {
			return createInventoryItemFromGoodie((CollectibleGoodie)collectible);
		} else if (collectible instanceof CollectibleTool) {
			return createInventoryItemFromTool((CollectibleTool)collectible);
		} else if (collectible instanceof CollectibleAmmo) {
			return createInventoryItemFromAmmo((CollectibleAmmo)collectible);
		} else if (collectible instanceof CollectibleKey) {
			return createInventoryItemFromKey((CollectibleKey)collectible);
		} else {
			return null;
		}
	}
		
	private static InventoryItem createInventoryItemFromGoodie(CollectibleGoodie collectibleGoodie) {
		GoodieInventoryItem item = new GoodieInventoryItem(collectibleGoodie.getGoodieType());
		return item;
	}

	private static InventoryItem createInventoryItemFromTool(CollectibleTool tool) {
		ToolType toolType = tool.getToolType();
		if (toolType == ToolType.antidote) {
			return new AntidoteInventoryItem();
		} else if (toolType == ToolType.axe) {
			return new AxeInventoryItem();
		} else if (toolType == ToolType.binoculars) {
			return new BinocularsInventoryItem();
		} else if (toolType == ToolType.boat) {
			return new BoatInventoryItem();
		} else if (toolType == ToolType.dynamite) {
			return new DynamiteInventoryItem();
		} else if (toolType == ToolType.geigerzaehler) {
			return new GeigerZaehlerInventoryItem();
		} else if (toolType == ToolType.jacket) {
			return new JacketInventoryItem();
		} else if (toolType == ToolType.machete) {
			return new MacheteInventoryItem();
		} else if (toolType == ToolType.minedetector) {
			return new MineDetectorInventoryItem();
		} else if (toolType == ToolType.pick) {
			return new PickInventoryItem();
		} else if (toolType == ToolType.ski) {
			return new SkiInventoryItem();
		} else if (toolType == ToolType.supplytracker) {
			return new SupplyTrackerInventoryItem();
		} else if (toolType == ToolType.archeologistsJournal) {
			return new ArcheologistsJournalInventoryItem();
		} else if (toolType == ToolType.portalgun) {
			return new PortalGunInventoryItem();
		} else {
			return null;
		}
	}
	
	private static InventoryItem createInventoryItemFromAmmo(CollectibleAmmo collectibleAmmo) {
		AmmoInventoryItem item = new AmmoInventoryItem(collectibleAmmo.getWeaponType());
		return item;
	}
	
	private static InventoryItem createInventoryItemFromKey(CollectibleKey collectibleKey) {
		Key key = collectibleKey.getKey();
		KeyRingInventoryItem keyRing = new KeyRingInventoryItem();
		keyRing.addKey(key);
		return keyRing;
	}
	
}
