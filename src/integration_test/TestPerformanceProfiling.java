package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

public class TestPerformanceProfiling {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(69, 352, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.ANTIDOTE, new AntidoteInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem());

				ChartInventoryItem chart = new ChartInventoryItem();

				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						chart.discoverPiece(i,j);
					}
				}


				player.getInventory().put(InventoryItemType.CHART, chart);

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
