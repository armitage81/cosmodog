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

/**
 * This test is used to test the text placement in the game.
 * Pressing '1' will bring the player to all relevant log files.
 */
public class TestTextPlacement {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {

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

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("3/3");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
