package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestPlatformVsTurrets {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(94, 195, ApplicationContextUtils.mapDescriptorMain()));
				player.setMaxLife(100);
				player.setLife(100);
				PlatformInventoryItem platform = new PlatformInventoryItem(new Platform());
				player.getInventory().put(InventoryItemType.PLATFORM, platform);
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add(String.format("%s/%s/%s", 355, 389, ApplicationContextUtils.mapDescriptorMain()));

				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));
			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
