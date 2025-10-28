package antonafanasjew.cosmodog.rules.triggers;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
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
import antonafanasjew.cosmodog.util.TileUtils;

/**
 * This trigger applies when a game event of typed "changed position" was thrown and the player
 * tile is covering the start point of the given switch-poison connector.
 * The switch is identified as the start point of the connection with the given name on the poison-switch connection object group.
 */
public class EnteringPoisonDeactivationSwitchTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 9096030493555091756L;

	private MapDescriptor mapDescriptor;
	private String connectorName;

	public EnteringPoisonDeactivationSwitchTrigger(MapDescriptor mapDescriptor, String connectorName) {
		this.mapDescriptor = mapDescriptor;
		this.connectorName = connectorName;
	}
	
	
	@Override
	public boolean accept(GameEvent event) {

		if (!(event instanceof GameEventChangedPosition)) {
			return false;
		}

		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(mapDescriptor);
		
		TiledObjectGroup poisonSwitchConnectionObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_POISON_SWITCH_CONNECTORS);
		
		Map<String, TiledObject> poisonSwitchConnectionObjects = poisonSwitchConnectionObjectGroup.getObjects();

		//Per convention, poison switch connection objects are polylines defined by two points. The first is the switch position, the second is the region with relevant poison tiles.
		TiledPolylineObject poisonSwitchConnectionObject = (TiledPolylineObject)poisonSwitchConnectionObjects.get(connectorName);
		
		List<Point> connectionPoints = poisonSwitchConnectionObject.getPoints();
		
		Point startPoint = connectionPoints.getFirst();
		
		Position switchPosition = Position.fromCoordinates(startPoint.x, startPoint.y, mapDescriptor);
		
		return RegionUtils.playerOnPosition(player, switchPosition);
		
	}

}
