package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestAll {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(5, 3, MapType.MAIN));
				List<String> debuggerPositions = new ArrayList<>();

				/*
				for (int i = 0; i < 20; i++) {
					for (int j = 0; j < 20; j++) {
						int x = j * 20 + 18;
						int y = i * 20 + 18;
						debuggerPositions.add(String.format("%s/%s/%s", x, y, MapType.ALTERNATIVE));
					}
				}
				*/

				debuggerPositions.add(String.format("%s/%s/%s", 200 + 11, 0 + 20, MapType.ALTERNATIVE));

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}

	
}
