package integration_test;

import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestSpoolReplacements {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(115, 156, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.GEIGERZAEHLER, new GeigerZaehlerInventoryItem());
				player.getInventory().put(InventoryItemType.RADIOACTIVESUIT, new RadioactiveSuitInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());

				List<String> debuggerPositions = new ArrayList<>();

				debuggerPositions.add(String.format("%s/%s/%s", 9, 248, ApplicationContextUtils.mapDescriptorMain()));
				debuggerPositions.add(String.format("%s/%s/%s", 48, 139, ApplicationContextUtils.mapDescriptorMain()));
				debuggerPositions.add(String.format("%s/%s/%s", 160, 260, ApplicationContextUtils.mapDescriptorMain()));
				debuggerPositions.add(String.format("%s/%s/%s", 188, 319, ApplicationContextUtils.mapDescriptorMain()));
				debuggerPositions.add(String.format("%s/%s/%s", 227, 272, ApplicationContextUtils.mapDescriptorMain()));

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
				
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
