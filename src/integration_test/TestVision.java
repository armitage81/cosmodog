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

public class TestVision {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(7, 3, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);

				player.getInventory().put(InventoryItemType.AXE, new AxeInventoryItem());
				player.getInventory().put(InventoryItemType.PICK, new PickInventoryItem());
				player.getInventory().put(InventoryItemType.MACHETE, new MacheteInventoryItem());
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());

				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));

				for (int i = 0; i < 6000; i++) {
					player.getGameProgress().addInfobit();
				}

				List<String> debuggerPositions = new ArrayList<>();

				debuggerPositions.add("45/127"); //BOT
				debuggerPositions.add("40/178"); //TANK
				debuggerPositions.add("365/52"); //DRONE
				debuggerPositions.add("4/130"); //TURRET
				debuggerPositions.add("358/150"); //PIGRAT
				debuggerPositions.add("157/157"); //ARTILLERY
				debuggerPositions.add("63/363"); //SCOUT
				debuggerPositions.add("37/156"); //HERCULES
				debuggerPositions.add("262/255"); //FLOATER
				debuggerPositions.add("263/302"); //CONDUCTOR
				debuggerPositions.add("227/258"); //GUARDIAN

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
