package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestRedCarpetContrast {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(255, 359));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("255/359");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
