package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.*;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestMonolythBehindPortal {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(118, 2, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);

				player.getInventory().put(InventoryItemType.INSIGHT, new InsightInventoryItem());
				player.getInventory().put(InventoryItemType.PORTAL_GUN, new PortalGunInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				List<String> debuggerPositions = new ArrayList<>();

				debuggerPositions.add("153/13");
				debuggerPositions.add("140/16");
				debuggerPositions.add("118/2");

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
