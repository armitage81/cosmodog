package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestAmmunitionInterface {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setMaxLife(100);
				player.setLife(100);

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("34/58");
				debuggerPositions.add("79/209");
				debuggerPositions.add("340/81");
				debuggerPositions.add("81/2");
				debuggerPositions.add("136/120");
				debuggerPositions.add("192/194");
				debuggerPositions.add("256/173");
				debuggerPositions.add("285/382");
				debuggerPositions.add("371/23");
				debuggerPositions.add("181/114");
				debuggerPositions.add("243/199");
				debuggerPositions.add("350/262");
				debuggerPositions.add("7/105");
				debuggerPositions.add("217/144");
				debuggerPositions.add("335/245");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));




			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
