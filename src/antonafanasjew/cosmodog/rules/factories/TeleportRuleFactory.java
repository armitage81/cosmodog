package antonafanasjew.cosmodog.rules.factories;

import java.util.Map;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.teleportation.TeleportationAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.triggers.EnteringTeleportTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;

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
	public Map<String, Rule> buildRules(CosmodogGame cosmodogGame) {

		//Do not take the map from the application context at this point as it is not defined there yet.
		CosmodogMap map = cosmodogGame.getMap();
		
		Map<String, Rule> retVal = Maps.newHashMap();
		
		TiledObjectGroup teleportConnectionObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_TELEPORT_CONNECTIONS);
		
		
		Map<String, TiledObject> teleportConnectionObjects = teleportConnectionObjectGroup.getObjects();
		
		for (String teleportConnectionName : teleportConnectionObjects.keySet()) {
			
			TiledPolylineObject teleportConnection = (TiledPolylineObject)teleportConnectionObjects.get(teleportConnectionName);
			
			RuleTrigger trigger = new EnteringTeleportTrigger(teleportConnectionName);

			//Some teleports can be activated. This is the way to ignore them until the activation property is set.
			String conditionPropertyName = teleportConnection.getProperties().get("conditionPropertyName");
			String conditionPropertyValue = teleportConnection.getProperties().get("conditionPropertyValue");
			
			if (conditionPropertyName != null && conditionPropertyValue != null && conditionPropertyName.isEmpty() == false) {
				GameProgressPropertyTrigger gameProgressPropertyTrigger = new GameProgressPropertyTrigger(conditionPropertyName, conditionPropertyValue);
				trigger = AndTrigger.and(trigger, gameProgressPropertyTrigger);
			}
			
			
			RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, new TeleportationAction(teleportConnection), false);
			
			Rule rule = new Rule("teleport." + teleportConnectionName, trigger, action);
			
			retVal.put(rule.getId(), rule);
		}
		
		return retVal;
	}

}
