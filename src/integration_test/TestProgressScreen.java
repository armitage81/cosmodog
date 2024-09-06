package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestProgressScreen {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPositionX(293);
				player.setPositionY(101);
				player.setMaxLife(100);
				player.setLife(100);
				player.getGameProgress().setSoulEssences(1);
				player.getGameProgress().setBottles(2);
				player.getGameProgress().setFoodCompartments(3);
				player.getGameProgress().setArmors(4);
				player.getGameProgress().setFuelTanks(5);

				ChartInventoryItem chart = new ChartInventoryItem();
				chart.setNumber(6);
				player.getInventory().put(InventoryItemType.CHART, chart);

				SoftwareInventoryItem software = new SoftwareInventoryItem();
				software.setNumber(7);
				player.getInventory().put(InventoryItemType.SOFTWARE, software);

				InsightInventoryItem insight = new InsightInventoryItem();
				insight.setNumber(8);
				player.getInventory().put(InventoryItemType.INSIGHT, insight);

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
