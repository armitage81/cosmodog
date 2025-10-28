package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestAlienNomadsLogs {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setMaxLife(100);
				player.setLife(100);

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("252/239");
				debuggerPositions.add("261/239");
				debuggerPositions.add("238/245");
				debuggerPositions.add("227/246");
				debuggerPositions.add("227/250");
				debuggerPositions.add("278/255");
				debuggerPositions.add("202/260");
				debuggerPositions.add("231/260");
				debuggerPositions.add("246/260");
				debuggerPositions.add("215/272");
				debuggerPositions.add("223/273");
				debuggerPositions.add("203/275");
				debuggerPositions.add("249/277");
				debuggerPositions.add("204/279");
				debuggerPositions.add("235/280");
				debuggerPositions.add("221/284");
				debuggerPositions.add("276/292");
				debuggerPositions.add("276/302");
				debuggerPositions.add("201/304");
				debuggerPositions.add("243/314");
				debuggerPositions.add("216/318");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));


			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
