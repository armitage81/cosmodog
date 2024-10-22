package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestEnemySightWhenUnderRoofRemovalBlocker {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(238, 237, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);

				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());

				for (int i = 0; i < 6000; i++) {
					player.getGameProgress().addInfobit();
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
