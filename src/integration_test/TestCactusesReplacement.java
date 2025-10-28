package integration_test;

import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.JacketInventoryItem;
import antonafanasjew.cosmodog.model.inventory.SkiInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestCactusesReplacement {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(5, 3, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);
				player.getGameProgress().setWormActive(false);
				player.getInventory().put(InventoryItemType.SKI, new SkiInventoryItem());
				player.getInventory().put(InventoryItemType.JACKET, new JacketInventoryItem());

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("107/16");
				debuggerPositions.add("157/29");
				debuggerPositions.add("201/34");
				debuggerPositions.add("14/35");
				debuggerPositions.add("63/41");
				debuggerPositions.add("110/50");
				debuggerPositions.add("250/73");
				debuggerPositions.add("378/78");
				debuggerPositions.add("76/81");
				debuggerPositions.add("219/94");
				debuggerPositions.add("347/97");
				debuggerPositions.add("160/98");
				debuggerPositions.add("303/124");
				debuggerPositions.add("351/126");
				debuggerPositions.add("155/135");
				debuggerPositions.add("202/135");
				debuggerPositions.add("65/137");
				debuggerPositions.add("250/146");
				debuggerPositions.add("106/148");
				debuggerPositions.add("240/152");
				debuggerPositions.add("101/157");
				debuggerPositions.add("159/170");
				debuggerPositions.add("52/175");
				debuggerPositions.add("336/178");
				debuggerPositions.add("19/180");
				debuggerPositions.add("375/185");
				debuggerPositions.add("253/197");
				debuggerPositions.add("25/205");
				debuggerPositions.add("256/206");
				debuggerPositions.add("364/210");
				debuggerPositions.add("200/221");
				debuggerPositions.add("69/239");
				debuggerPositions.add("172/244");
				debuggerPositions.add("315/249");
				debuggerPositions.add("102/262");
				debuggerPositions.add("161/282");
				debuggerPositions.add("308/287");
				debuggerPositions.add("285/292");
				debuggerPositions.add("16/298");
				debuggerPositions.add("55/298");
				debuggerPositions.add("357/298");
				debuggerPositions.add("5/308");
				debuggerPositions.add("75/310");
				debuggerPositions.add("354/311");
				debuggerPositions.add("307/316");
				debuggerPositions.add("200/321");
				debuggerPositions.add("199/322");
				debuggerPositions.add("103/338");
				debuggerPositions.add("257/338");
				debuggerPositions.add("99/350");
				debuggerPositions.add("101/354");
				debuggerPositions.add("203/359");
				debuggerPositions.add("152/361");
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));


			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
