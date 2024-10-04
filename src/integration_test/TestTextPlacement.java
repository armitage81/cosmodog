package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

/**
 * This test is used to test the text placement in the game.
 * Pressing '1' will bring the player to all relevant log files.
 */
public class TestTextPlacement {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {

				player.setMaxLife(100);
				player.setLife(100);

				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");
				player.getLogPlayer().addLogToSeries("hints");

				List<String> debuggerPositions = new ArrayList<>();

				debuggerPositions.add("204/279");
				debuggerPositions.add("207/274");
				debuggerPositions.add("223/288");
				debuggerPositions.add("225/251");
				debuggerPositions.add("231/260");
				debuggerPositions.add("232/248");
				debuggerPositions.add("252/254");
				debuggerPositions.add("268/284");
				debuggerPositions.add("273/239");
				debuggerPositions.add("261/135");
				debuggerPositions.add("274/135");
				debuggerPositions.add("291/122");
				debuggerPositions.add("314/118");
				debuggerPositions.add("330/126");
				debuggerPositions.add("332/135");
				debuggerPositions.add("346/119");
				debuggerPositions.add("354/131");
				debuggerPositions.add("362/117");
				debuggerPositions.add("364/127");
				debuggerPositions.add("292/242");
				debuggerPositions.add("307/198");
				debuggerPositions.add("308/146");
				debuggerPositions.add("312/161");
				debuggerPositions.add("313/223");
				debuggerPositions.add("316/249");
				debuggerPositions.add("325/174");
				debuggerPositions.add("339/210");
				debuggerPositions.add("365/235");
				debuggerPositions.add("124/210");
				debuggerPositions.add("129/220");
				debuggerPositions.add("143/214");
				debuggerPositions.add("146/205");
				debuggerPositions.add("3/130");
				debuggerPositions.add("5/157");
				debuggerPositions.add("8/168");
				debuggerPositions.add("8/194");
				debuggerPositions.add("9/205");
				debuggerPositions.add("10/221");
				debuggerPositions.add("15/139");
				debuggerPositions.add("23/147");
				debuggerPositions.add("42/195");
				debuggerPositions.add("105/118");
				debuggerPositions.add("119/38");
				debuggerPositions.add("137/142");
				debuggerPositions.add("168/326");
				debuggerPositions.add("180/68");
				debuggerPositions.add("220/220");
				debuggerPositions.add("122/42");
				debuggerPositions.add("126/37");
				debuggerPositions.add("126/47");
				debuggerPositions.add("129/50");
				debuggerPositions.add("134/38");
				debuggerPositions.add("134/47");
				debuggerPositions.add("204/192");
				debuggerPositions.add("206/192");
				debuggerPositions.add("208/192");
				debuggerPositions.add("210/192");
				debuggerPositions.add("223/192");
				debuggerPositions.add("231/191");
				debuggerPositions.add("204/181");
				debuggerPositions.add("208/184");
				debuggerPositions.add("216/185");
				debuggerPositions.add("218/182");
				debuggerPositions.add("219/186");
				debuggerPositions.add("225/182");
				debuggerPositions.add("226/186");
				debuggerPositions.add("54/239");
				debuggerPositions.add("322/334");
				debuggerPositions.add("247/105");
				debuggerPositions.add("131/111");
				debuggerPositions.add("38/154");
				debuggerPositions.add("51/147");
				debuggerPositions.add("43/55");
				debuggerPositions.add("238/366");
				debuggerPositions.add("8/254");
				debuggerPositions.add("202/264");
				debuggerPositions.add("218/260");
				debuggerPositions.add("8/112");
				debuggerPositions.add("11/33");
				debuggerPositions.add("19/75");
				debuggerPositions.add("78/7");
				debuggerPositions.add("170/11");
				debuggerPositions.add("231/257");
				debuggerPositions.add("21/289");
				debuggerPositions.add("297/179");
				debuggerPositions.add("83/148");


				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
