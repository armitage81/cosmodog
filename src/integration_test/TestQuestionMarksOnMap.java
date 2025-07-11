package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestQuestionMarksOnMap {

	/**
	 * - Test that question marks appear on the charted map for the following items:
	 * 	- Insights,
	 * 	- Tools,
	 * 	- Weapons,
	 * 	- Food compartments and water bottles.
	 * - Test that the question marks do not cross the borders of the map.
	 * - Test that uncharted map pieces do not contain question marks.
	 * - Test that a collected item makes the question mark disappear.
	 *
	 */

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem());
				player.getInventory().put(InventoryItemType.ARCHEOLOGISTS_JOURNAL, new ArcheologistsJournalInventoryItem());

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
