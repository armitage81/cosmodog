package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestRoofsOnMap {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPositionX(238);
				player.setPositionY(237);
				player.setMaxLife(100);
				player.setLife(100);


				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());

				for (int i = 0; i < 6000; i++) {
					player.getGameProgress().addInfobit();
				}

				player.getInventory().put(InventoryItemType.CHART, new ChartInventoryItem());
				ChartInventoryItem chart = ((ChartInventoryItem)player.getInventory().get(InventoryItemType.CHART));
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						chart.discoverPiece(i,j);
					}
				}

				InsightInventoryItem insight = new InsightInventoryItem();
				insight.setNumber(32);

				player.getInventory().put(InventoryItemType.INSIGHT, insight);

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("15/275");
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
