package integration_test;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PortalGunInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;

public class TestCollectiblesInSpaceLab {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(228, 135, ApplicationContextUtils.mapDescriptorSpace()));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.PORTAL_GUN, new PortalGunInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.PISTOL));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.SHOTGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RIFLE));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.MACHINEGUN));
				player.getArsenal().addWeaponToArsenal(new Weapon(WeaponType.RPG));
				player.getArsenal().selectNextWeaponType();

				List<String> debuggerPositions = new ArrayList<>();
				debuggerPositions.add(String.format("%s/%s/%s", 176, 53, ApplicationContextUtils.mapDescriptorSpace()));
				debuggerPositions.add(String.format("%s/%s/%s", 193, 167, ApplicationContextUtils.mapDescriptorSpace()));
				debuggerPositions.add(String.format("%s/%s/%s", 3, 5, ApplicationContextUtils.mapDescriptorMain()));
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
