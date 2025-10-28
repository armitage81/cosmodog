package integration_test;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.JacketInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestSnowUnderRoof {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(255, 354, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);

				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());


				for (int i = 0; i < 6000; i++) {
					player.getGameProgress().addInfobit();
				}

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("255/357");
				debuggerPositions.add("255/358");
				debuggerPositions.add("255/359");
				debuggerPositions.add("255/348");
				debuggerPositions.add("130/365");
				debuggerPositions.add("31/391");
				debuggerPositions.add("255/354");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));


			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
