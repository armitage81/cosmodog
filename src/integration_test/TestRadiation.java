package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestRadiation {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPositionX(39);
				player.setPositionY(212);
				player.setMaxLife(100);
				player.setLife(100);

				List<String> debuggerPositions = new ArrayList<>();

				debuggerPositions.add("223/24");
				debuggerPositions.add("326/85");
				debuggerPositions.add("340/87");
				debuggerPositions.add("120/147");
				debuggerPositions.add("101/154");
				debuggerPositions.add("257/213");
				debuggerPositions.add("260/213");
				debuggerPositions.add("38/215");
				debuggerPositions.add("41/218");
				debuggerPositions.add("35/222");
				debuggerPositions.add("40/223");
				debuggerPositions.add("300/258");
				debuggerPositions.add("298/259");
				debuggerPositions.add("294/261");
				debuggerPositions.add("300/263");
				debuggerPositions.add("181/321");

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));


			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
