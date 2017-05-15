package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.AntidoteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SoftwareInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

/***
 * 
 * This is not finished yet
 *
 */
public class TestFullyEquippedPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {
		player.setMaxLife(100);
		player.setLife(100);
		player.setPositionX(7);
		player.setPositionY(22);
		
		player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
		player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new GeigerZaehlerInventoryItem());
		player.getInventory().put(InventoryItemType.SUPPLYTRACKER, new SupplyTrackerInventoryItem());
		player.getInventory().put(InventoryItemType.ANTIDOTE, new AntidoteInventoryItem());
		
		
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		
		SoftwareInventoryItem item = new SoftwareInventoryItem();
		item.setNumber(10);
		player.getInventory().put(InventoryItemType.SOFTWARE, item);
		player.getGameProgress().setArmors(4);
		player.getGameProgress().setSoulEssences(11);
		
		ChartInventoryItem chartInventoryItem = new ChartInventoryItem();
		chartInventoryItem.discoverPiece(0, 0);
		chartInventoryItem.discoverPiece(0, 1);
		chartInventoryItem.discoverPiece(1, 0);
		
		player.getInventory().put(InventoryItemType.CHART, chartInventoryItem);

		InsightInventoryItem insightInventoryItem = new InsightInventoryItem();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		insightInventoryItem.increaseNumber();
		
		player.getInventory().put(InventoryItemType.INSIGHT, insightInventoryItem);
		
		for (int i = 0; i < 6000; i++) {
			player.getGameProgress().addInfobit();
		}
		
		for (int i = 0; i < 120; i++) {
			player.getGameProgress().increaseNumberOfFoundSecrets();
		}		
	}

}
