package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.enemyinventory.AmmoInventoryItem;
import antonafanasjew.cosmodog.model.enemyinventory.EnemyInventoryItem;
import antonafanasjew.cosmodog.model.enemyinventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.model.enemyinventory.KeyInventoryItem;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Key;

public class InventoryItemFactory {

	public static InventoryItem createInventoryItem(Collectible collectible) {
		if (collectible instanceof CollectibleTool) {
			return createInventoryItemFromTool((CollectibleTool)collectible);
		} else {
			return null;
		}
	}

	public static EnemyInventoryItem createEnemyInventoryItem(Collectible collectible) {
        return switch (collectible) {
            case CollectibleGoodie collectibleGoodie -> createInventoryItemFromGoodie(collectibleGoodie);
            case CollectibleAmmo collectibleAmmo -> createInventoryItemFromAmmo(collectibleAmmo);
            case CollectibleKey collectibleKey -> createInventoryItemFromKey(collectibleKey);
            case null, default -> null;
        };
	}

	private static EnemyInventoryItem createInventoryItemFromGoodie(CollectibleGoodie collectibleGoodie) {
        return new GoodieInventoryItem(collectibleGoodie.getGoodieType());
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
	
	private static EnemyInventoryItem createInventoryItemFromAmmo(CollectibleAmmo collectibleAmmo) {
        return new AmmoInventoryItem(collectibleAmmo.getWeaponType());
	}
	
	private static EnemyInventoryItem createInventoryItemFromKey(CollectibleKey collectibleKey) {
		Key key = collectibleKey.getKey();
		return new KeyInventoryItem(key.getDoorType());
	}
	
}
