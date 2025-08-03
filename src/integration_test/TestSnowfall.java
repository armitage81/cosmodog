package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

public class TestSnowfall {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(111, 349, MapType.MAIN));
				player.setLife(1);
				for (int i = 0; i < 64; i++) {
					player.getGameProgress().addInfobank();
				}
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem("111/355;111/357;111/349"));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
