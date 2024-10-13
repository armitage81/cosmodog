package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

public class TestEnemyCollisionWithEnergyWalls {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPositionX(34);
				player.setPositionY(107);
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.BOAT, new BoatInventoryItem());

				for (int i = 0; i < 599; i++) {
					player.getGameProgress().addInfobit();
				}

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
