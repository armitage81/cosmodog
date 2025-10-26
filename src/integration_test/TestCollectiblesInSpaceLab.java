package integration_test;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PortalGunInventoryItem;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.player.AbstractPlayerBuilder;
import antonafanasjew.cosmodog.player.PlayerBuilder;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.SlickException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestCollectiblesInSpaceLab {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(228, 135, MapType.SPACE));
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
				debuggerPositions.add(String.format("%s/%s/%s", 176, 53, MapType.SPACE));
				debuggerPositions.add(String.format("%s/%s/%s", 193, 167, MapType.SPACE));
				debuggerPositions.add(String.format("%s/%s/%s", 3, 5, MapType.MAIN));
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
