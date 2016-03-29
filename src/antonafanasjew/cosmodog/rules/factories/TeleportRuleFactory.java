package antonafanasjew.cosmodog.rules.factories;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.triggers.EnteringTeleportTrigger;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import com.google.common.collect.Maps;

/**
 * This factory (which is singleton) walks through the teleport connection object group of the custom tiled map
 * and builds the teleport jumping rules for each teleport.
 * The trigger is the player tile covering the start point of the teleport connection polyline object.
 * The action is the registration of the actual asynchronous transportation in the action registry. 
 */
public class TeleportRuleFactory implements RuleFactory {

	private static TeleportRuleFactory instance = new TeleportRuleFactory();
	
	public static TeleportRuleFactory getInstance() {
		return instance;
	}
	
	@Override
	public Map<String, Rule> buildRules() {

		Map<String, Rule> retVal = Maps.newHashMap();
		
		final CustomTiledMap tiledMap = ApplicationContextUtils.getCustomTiledMap();
		TiledObjectGroup teleportConnectionObjectGroup = tiledMap.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_TELEPORT_CONNECTIONS);
		
		
		Map<String, TiledObject> teleportConnectionObjects = teleportConnectionObjectGroup.getObjects();
		
		for (String teleportConnectionName : teleportConnectionObjects.keySet()) {
			
			TiledPolylineObject teleportConnection = (TiledPolylineObject)teleportConnectionObjects.get(teleportConnectionName);
			
			List<Point> teleportConnectionPoints = teleportConnection.getPoints();
			
			Point endPoint = teleportConnectionPoints.get(1);
			
			EnteringTeleportTrigger trigger = new EnteringTeleportTrigger(teleportConnectionName);
			
			RuleAction action = new AbstractRuleAction() {
				
				private static final long serialVersionUID = -4287587770311355080L;

				@Override
				public void execute(GameEvent event) {
					
					Player player = ApplicationContextUtils.getPlayer();
					Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
					
					int targetPosX = (int)endPoint.x / tiledMap.getTileWidth();
					int targetPosY = (int)endPoint.y / tiledMap.getTileHeight();
					
					player.setPositionX(targetPosX);
					player.setPositionY(targetPosY);
					
					cam.focusOnPiece(tiledMap, 0, 0, player);
					
				}
			};
			
			Rule rule = new Rule("teleport." + teleportConnectionName, trigger, action);
			
			retVal.put(rule.getId(), rule);
		}
		
		return retVal;
	}

}
