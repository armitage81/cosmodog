package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

public class TestPortals {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(165, 76, MapType.SPACE));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
