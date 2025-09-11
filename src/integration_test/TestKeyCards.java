package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestKeyCards {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(238, 237, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);

				for (int i = 0; i < 3; i++ ) {
					player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
					player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
					player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
					player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
					player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				}

				player.getInventory().put(InventoryItemType.DYNAMITE, new DynamiteInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());

				for (int i = 0; i < 6000; i++) {
					player.getGameProgress().addInfobit();
				}

				InsightInventoryItem insight = new InsightInventoryItem();
				insight.setNumber(33);
				player.getInventory().put(InventoryItemType.INSIGHT, insight);

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("206/269");
				debuggerPositions.add("217/269");
				debuggerPositions.add("230/269");
				debuggerPositions.add("242/254");
				debuggerPositions.add("261/245");
				debuggerPositions.add("224/281");
				DebuggerInventoryItem debugger = new DebuggerInventoryItem(String.join(";", debuggerPositions));
				player.getInventory().put(InventoryItemType.DEBUGGER, debugger);
				player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());
				player.getInventory().put(InventoryItemType.PICK, new PickInventoryItem());
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
