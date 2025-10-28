package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

public class TestAllInventoryItems {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(5, 5, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);


				player.getInventory().put(InventoryItemType.VEHICLE, new VehicleInventoryItem(new Vehicle()));
				player.getInventory().put(InventoryItemType.PLATFORM, new PlatformInventoryItem(new Platform()));
				player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
				player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
				player.getInventory().put(InventoryItemType.FUEL_TANK, new FuelTankInventoryItem());
				player.getInventory().put(InventoryItemType.INSIGHT, new InsightInventoryItem());
				player.getInventory().put(InventoryItemType.SOFTWARE, new SoftwareInventoryItem());
				player.getInventory().put(InventoryItemType.CHART, new ChartInventoryItem());
				player.getInventory().put(InventoryItemType.KEY_RING, new KeyRingInventoryItem());
				player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new GeigerZaehlerInventoryItem());
				player.getInventory().put(InventoryItemType.SUPPLYTRACKER, new SupplyTrackerInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
				player.getInventory().put(InventoryItemType.ANTIDOTE, new AntidoteInventoryItem());
				player.getInventory().put(InventoryItemType.MINEDETECTOR, new MineDetectorInventoryItem());
				player.getInventory().put(InventoryItemType.MINEDEACTIVATIONCODES, new MineDeactivationCodesInventoryItem());
				player.getInventory().put(InventoryItemType.PICK, new PickInventoryItem());
				player.getInventory().put(InventoryItemType.MACHETE, new MacheteInventoryItem());
				player.getInventory().put(InventoryItemType.AXE, new AxeInventoryItem());
				player.getInventory().put(InventoryItemType.RADIOACTIVESUIT, new RadioactiveSuitInventoryItem());
				player.getInventory().put(InventoryItemType.ARCHEOLOGISTS_JOURNAL, new ArcheologistsJournalInventoryItem());
				player.getInventory().put(InventoryItemType.WEAPON_FIRMWARE_UPGRADE, new WeaponFirmwareUpgradeInventoryItem());
				player.getInventory().put(InventoryItemType.NUTRIENTS, new NutrientsInventoryItem());
				player.getInventory().put(InventoryItemType.PORTAL_GUN, new PortalGunInventoryItem());
				player.getInventory().put(InventoryItemType.FLASHLIGHT, new FlashlightInventoryItem());
				player.getInventory().put(InventoryItemType.NIGHT_VISION_GOGGLES, new NightVisionGogglesInventoryItem());
				player.getInventory().put(InventoryItemType.MOTION_TRACKER, new MotionTrackerInventoryItem());
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem());


				
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
