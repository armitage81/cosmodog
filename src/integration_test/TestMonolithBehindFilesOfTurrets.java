package integration_test;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.RadioactiveSuitInventoryItem;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestMonolithBehindFilesOfTurrets {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(95, 196, MapType.MAIN));
				player.setMaxLife(100);
				player.setLife(100);
				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add("172/317");
				debuggerPositions.add("95/196");
				DebuggerInventoryItem debugger = new DebuggerInventoryItem(String.join(";", debuggerPositions));
				player.getInventory().put(InventoryItemType.DEBUGGER, debugger);
				player.getInventory().put(InventoryItemType.PLATFORM, new PlatformInventoryItem(new Platform()));
				player.getInventory().put(InventoryItemType.RADIOACTIVESUIT, new RadioactiveSuitInventoryItem());
				
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
