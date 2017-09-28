package antonafanasjew.cosmodog.player;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GeigerZaehlerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class DefaultPlayerBuilder extends AbstractPlayerBuilder {

	@Override
	protected void updatePlayer(Player player) {

//		player.setPositionX(227);
//		player.setPositionY(256);
		
		
		player.setMaxLife(100);
		player.setLife(100);
		
		
		
		
		player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
		 

		
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		player.getLogPlayer().addLogToSeries("aliennomads");
		
		
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		player.getLogPlayer().addLogToSeries("amurderingpoet");
		
		
		player.getLogPlayer().addLogToSeries("smileofthegoddess");
		player.getLogPlayer().addLogToSeries("smileofthegoddess");
		player.getLogPlayer().addLogToSeries("smileofthegoddess");
		player.getLogPlayer().addLogToSeries("smileofthegoddess");
		
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		player.getLogPlayer().addLogToSeries("privatehiggs");
		
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		player.getLogPlayer().addLogToSeries("pinky");
		
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		player.getLogPlayer().addLogToSeries("maryharper");
		
		
//		player.setWater(20);
//		player.setFood(20);
//		
//		player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
//		player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
//		player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new GeigerZaehlerInventoryItem());
//		player.getInventory().put(InventoryItemType.SUPPLYTRACKER, new SupplyTrackerInventoryItem());
//		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
//		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
//		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
//		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
//		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
//		
//		SoftwareInventoryItem item = new SoftwareInventoryItem();
//		item.setNumber(10);
//		player.getInventory().put(InventoryItemType.SOFTWARE, item);
//		player.getGameProgress().setArmors(4);
//		player.getGameProgress().setSoulEssences(11);
//		
//		ChartInventoryItem chartInventoryItem = new ChartInventoryItem();
//		
//		for (int i = 0; i < 2; i++) {
//			for (int j = 0; j < 2; j++) {
//				chartInventoryItem.discoverPiece(i, j);
//			}
//		}
//		
//		player.getInventory().put(InventoryItemType.CHART, chartInventoryItem);

		
//		InsightInventoryItem insightInventoryItem = new InsightInventoryItem();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		insightInventoryItem.increaseNumber();
//		
//		player.getInventory().put(InventoryItemType.INSIGHT, insightInventoryItem);
//		
//		for (int i = 0; i < 6000; i++) {
//			player.getGameProgress().addInfobit();
//		}
//		
//		for (int i = 0; i < 120; i++) {
//			player.getGameProgress().increaseNumberOfFoundSecrets();
//		}
		
	}

}
