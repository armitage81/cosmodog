package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestGameHints {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(7, 3, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);
				player.setWater(10);
				player.setFood(5);
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("367/323");
				debuggerPositions.add("268/352");
				debuggerPositions.add("248/104");
				debuggerPositions.add("232/111");
				debuggerPositions.add("350/325");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
