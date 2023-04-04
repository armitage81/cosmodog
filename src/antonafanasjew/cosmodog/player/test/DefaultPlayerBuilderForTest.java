package antonafanasjew.cosmodog.player.test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArcheologistsJournalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DynamiteInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.RadioactiveSuitInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SupplyTrackerInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;

public class DefaultPlayerBuilderForTest extends AbstractPlayerBuilder {
	@Override
	protected void updatePlayer(Player player) {

		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.getLogPlayer().addLogToSeries("hints");
		player.setPositionX(211);
		player.setPositionY(250);
		
		player.getGameProgress().setTurnsTillWormAppears(7);

		ChartInventoryItem chartInventoryItem = new ChartInventoryItem();

		chartInventoryItem.discoverPiece(0, 0);
		chartInventoryItem.discoverPiece(0, 1);
		chartInventoryItem.discoverPiece(0, 2);
		chartInventoryItem.discoverPiece(1, 2);
		chartInventoryItem.discoverPiece(2, 2);
		chartInventoryItem.discoverPiece(2, 1);
		chartInventoryItem.discoverPiece(2, 0);
		chartInventoryItem.discoverPiece(1, 0);
		
		player.getInventory().put(InventoryItemType.CHART, chartInventoryItem);

		player.getInventory().put(InventoryItemType.CHART,
		chartInventoryItem);


		player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem()); 
		player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
		player.getInventory().put(InventoryItemType.SUPPLYTRACKER, new SupplyTrackerInventoryItem());
		player.getInventory().put(InventoryItemType.RADIOACTIVESUIT, new RadioactiveSuitInventoryItem());
		player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
		player.getInventory().put(InventoryItemType.ARCHEOLOGISTS_JOURNAL, new ArcheologistsJournalInventoryItem());
		
//		player.getInventory().put(InventoryItemType.KEY_RING, new KeyRingInventoryItem());
//		
//		
//		Key key;
//		
//		for (DoorType doorType : DoorType.values()) {
//			key = new Key();
//			key.setDoorType(doorType);
//			((KeyRingInventoryItem)player.getInventory().get(InventoryItemType.KEY_RING)).addKey(key);
//		}
//		
//		for (int i = 0; i < 5999; i++) {
//			player.getGameProgress().addInfobit();
//		}
		
		
		/*
		 * if (Features.getInstance().featureOn(Features.FEATURE_DEBUGGER)) {
		 * player.getInventory().put(InventoryItemType.DEBUGGER, new
		 * DebuggerInventoryItem()); }
		 * 
		 * player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		 * player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		 * player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
		 * player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
		 * player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
		 * 
		 * 
		 * player.getInventory().put(InventoryItemType.AXE, new AxeInventoryItem());
		 * player.getInventory().put(InventoryItemType.BINOCULARS, new
		 * BinocularsInventoryItem()); //
		 * player.getInventory().put(InventoryItemType.DEBUGGER, new
		 * DebuggerInventoryItem()); player.getInventory().put(InventoryItemType.BOAT,
		 * new BoatInventoryItem()); player.getInventory().put(InventoryItemType.SKI,
		 * new SkiInventoryItem()); player.getInventory().put(InventoryItemType.PICK,
		 * new PickInventoryItem());
		 * player.getInventory().put(InventoryItemType.DYNAMITE, new
		 * DynamiteInventoryItem());
		 * player.getInventory().put(InventoryItemType.MACHETE, new
		 * MacheteInventoryItem());
		 * player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new
		 * GeigerZaehlerInventoryItem());
		 */
		/*
		 * SoftwareInventoryItem software = new SoftwareInventoryItem();
		 * software.setNumber(16); player.getInventory().put(InventoryItemType.SOFTWARE,
		 * software);
		 * 
		 * ChartInventoryItem chartInventoryItem = new ChartInventoryItem(); for (int i
		 * = 0; i < 8; i++) { for (int j = 0; j < 8; j++) {
		 * chartInventoryItem.discoverPiece(i, j); } }
		 * player.getInventory().put(InventoryItemType.CHART, chartInventoryItem);
		 * 
		 * /*
		 * 
		 * 
		 * 
		 * player.getInventory().put(InventoryItemType.DYNAMITE, new
		 * DynamiteInventoryItem());
		 * 
		 * 
		 * 
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * player.getLogPlayer().addLogToSeries("aliennomads");
		 * 
		 * 
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * player.getLogPlayer().addLogToSeries("amurderingpoet");
		 * 
		 * 
		 * player.getLogPlayer().addLogToSeries("smileofthegoddess");
		 * player.getLogPlayer().addLogToSeries("smileofthegoddess");
		 * player.getLogPlayer().addLogToSeries("smileofthegoddess");
		 * player.getLogPlayer().addLogToSeries("smileofthegoddess");
		 * 
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * player.getLogPlayer().addLogToSeries("privatehiggs");
		 * 
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * player.getLogPlayer().addLogToSeries("pinky");
		 * 
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 * player.getLogPlayer().addLogToSeries("maryharper");
		 */

		// player.setWater(20);
		// player.setFood(20);
		//
		// player.getInventory().put(InventoryItemType.SKI, new
		// SkiInventoryItem());
		// player.getInventory().put(InventoryItemType.BOAT, new
		// BoatInventoryItem());
		// player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new
		// GeigerZaehlerInventoryItem());
		// player.getInventory().put(InventoryItemType.SUPPLYTRACKER, new
		// SupplyTrackerInventoryItem());
		// player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
		// player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
		// player.getArsenal().addWeaponToArsenal(new
		// Weapon(WeaponType.PISTOL));
		// player.getArsenal().addWeaponToArsenal(new
		// Weapon(WeaponType.SHOTGUN));
		// player.getArsenal().addWeaponToArsenal(new
		// Weapon(WeaponType.MACHINEGUN));
		//
		// SoftwareInventoryItem item = new SoftwareInventoryItem();
		// item.setNumber(10);
		// player.getInventory().put(InventoryItemType.SOFTWARE, item);
		// player.getGameProgress().setArmors(4);
		// player.getGameProgress().setSoulEssences(11);
		//
		// ChartInventoryItem chartInventoryItem = new ChartInventoryItem();
		//
		// for (int i = 0; i < 2; i++) {
		// for (int j = 0; j < 2; j++) {
		// chartInventoryItem.discoverPiece(i, j);
		// }
		// }
		//
		// player.getInventory().put(InventoryItemType.CHART,
		// chartInventoryItem);

//		 InsightInventoryItem insightInventoryItem = new InsightInventoryItem();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		 insightInventoryItem.increaseNumber();
//		

	}

}
