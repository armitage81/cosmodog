package integration_test;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.CosmodogModelHolder;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BinocularsInventoryItem;
import antonafanasjew.cosmodog.model.inventory.DebuggerInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PortalGunInventoryItem;
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

public class TestPortals {

	public static void main(String[] args) throws SlickException {
		
		PlayerBuilder playerBuilder = new AbstractPlayerBuilder() {
			
			@Override
			protected void updatePlayer(Player player) {
				player.setPosition(Position.fromCoordinates(161, 122, MapType.SPACE));
				player.setMaxLife(100);
				player.setLife(100);
				player.getInventory().put(InventoryItemType.PORTAL_GUN, new PortalGunInventoryItem());
				player.getInventory().put(InventoryItemType.BINOCULARS, new BinocularsInventoryItem());

				CustomTiledMap map = ApplicationContextUtils.getCustomTiledMaps().get(MapType.SPACE);
				TiledObjectGroup teleportConnectionObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_TELEPORT_CONNECTIONS);

				Map<String, TiledObject> teleportConnectionObjects = teleportConnectionObjectGroup.getObjects();

				List<String> teleporterCollectionNames = teleportConnectionObjects.keySet().stream().sorted((a, b) -> {
					String number1AsString = a.replaceAll("spaceLab_toChamber", "");
					String number2AsString = b.replaceAll("spaceLab_toChamber", "");
					int number1 = Integer.parseInt(number1AsString);
					int number2 = Integer.parseInt(number2AsString);
					return number1 - number2;
				}).toList();

				int tileLength = TileUtils.tileLengthSupplier.get();
				List<String> debuggerPositions = new ArrayList<>();
				for (String teleportConnectionName : teleporterCollectionNames) {

					TiledPolylineObject teleportConnection = (TiledPolylineObject) teleportConnectionObjects.get(teleportConnectionName);
					TiledLineObject.Point endPoint = teleportConnection.getPoints().get(1);
					int targetPosX = (int)endPoint.x / tileLength;
					int targetPosY = (int)endPoint.y / tileLength;
					debuggerPositions.add(String.format("%s/%s/%s", targetPosX, targetPosY, MapType.SPACE));
				}
				player.getInventory().put(InventoryItemType.DEBUGGER, new DebuggerInventoryItem(String.join(";", debuggerPositions)));

			}
		};

		CosmodogModelHolder.replacePlayerBuilder(playerBuilder);

		TestStarter.main(args);
	}
	
}
