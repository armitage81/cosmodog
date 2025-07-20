package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.Optional;

public class TestMonolithText {

	/*
	Test cases:
	- AI spell check
	- AI style check
	- Page breaks work?
	- Paragraph breaks work?
	- Direct speech properly tagged?
	 */

	public static void main(String[] args) throws SlickException {

		//The count is 1-based.
		int monolithNumber = Integer.parseInt(Optional.ofNullable(System.getProperty("monolithNumber")).orElse("1"));

		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(53, 30, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);

				InsightInventoryItem insight = (InsightInventoryItem) player.getInventory().get(InventoryItemType.INSIGHT);
				if (insight == null) {
					insight = new InsightInventoryItem();
					insight.setNumber(1);
					player.getInventory().put(InventoryItemType.INSIGHT, insight);
				}

				//This one is for the space lab monolith that we assume collected
				insight.increaseNumber();
				player.getGameProgress().getProgressProperties().put("foundinsightnumber_space", "1");

				//And these are collected monoliths and played cutscenes as per configuration of this class.
				for (int i = 2; i < monolithNumber; i++) {
					insight.increaseNumber();
				}
				player.getGameProgress().getProgressProperties().put("foundinsightnumber", "" + (monolithNumber - 1));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
