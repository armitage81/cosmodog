package antonafanasjew.cosmodog.rules.triggers;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

/**
 * This trigger applies when a game event of typed "changed position" was thrown and the player
 * tile is covering the start point of the given teleport.
 * The teleport is identified as the start point of the teleport connection with the given name on the teleport connection object group.
 */
public class EnteringTeleportTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 9096030493555091756L;

	private MapType mapType;
	private String teleportName;

	/**
	 * Initialized with the teleport name.
	 * @param teleportName The name of the teleport connection as defined on the teleport connection group in the tiled map.
	 */
	public EnteringTeleportTrigger(MapType mapType, String teleportName) {
		this.mapType = mapType;
		this.teleportName = teleportName;
	}
	
	
	@Override
	public boolean accept(GameEvent event) {
		if (event instanceof GameEventChangedPosition == false) {
			return false;
		}
		
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(mapType);
		
		TiledObjectGroup teleportConnectionObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_TELEPORT_CONNECTIONS);
		
		Map<String, TiledObject> teleportConnectionObjects = teleportConnectionObjectGroup.getObjects();

		//Per convention, teleport connection objects are polylines defined by two points. The first is the teleport position, the second is the teleport target.
		TiledPolylineObject teleportConnectionObject = (TiledPolylineObject)teleportConnectionObjects.get(teleportName);
		
		List<Point> teleportConnectionPoints = teleportConnectionObject.getPoints();
		
		Point teleportStartPoint = teleportConnectionPoints.get(0);
		
		Position teleportStartPosition = Position.fromCoordinates(teleportStartPoint.x, teleportStartPoint.y);
		
		boolean inRegion = RegionUtils.playerOnPosition(player, teleportStartPosition, map.getTileWidth(), map.getTileHeight());
		
		return inRegion;
		
	}

}
