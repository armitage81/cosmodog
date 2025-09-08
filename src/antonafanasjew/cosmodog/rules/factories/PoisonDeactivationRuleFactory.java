package antonafanasjew.cosmodog.rules.factories;

import java.util.Map;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.teleportation.PoisonDeactivationAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.triggers.EnteringPoisonDeactivationSwitchTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;

import com.google.common.collect.Maps;

/**
 * This factory (which is singleton) walks through the poison deactivation switch connection object group of the custom tiled map
 * and builds the deactivation rules for each switch.
 * The trigger is the player tile covering the start point of the poison-switch connector polyline object.
 * The action is the registration of the actual asynchronous deactivation in the action registry. 
 */
public class PoisonDeactivationRuleFactory implements RuleFactory {

	private static PoisonDeactivationRuleFactory instance = new PoisonDeactivationRuleFactory();
	
	public static PoisonDeactivationRuleFactory getInstance() {
		return instance;
	}
	
	
	@Override
	public Map<String, Rule> buildRules(CosmodogGame cosmodogGame) {

		Map<String, Rule> retVal = Maps.newHashMap();

		for (MapType mapType : MapType.values()) {

			//Do not take the map from the application context at this point as it is not defined there yet.
			CosmodogMap map = cosmodogGame.getMaps().get(mapType);

			TiledObjectGroup poisonConnectionObjectGroup = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_POISON_SWITCH_CONNECTORS);


			Map<String, TiledObject> poisonConnectionObjects = poisonConnectionObjectGroup.getObjects();

			for (String poisonConnectionName : poisonConnectionObjects.keySet()) {

				TiledPolylineObject poisonConnection = (TiledPolylineObject)poisonConnectionObjects.get(poisonConnectionName);
				String gameProgressProperty = "PoisonSwitchTriggered." + poisonConnection.getName();

				RuleTrigger trigger = new EnteringPoisonDeactivationSwitchTrigger(mapType, poisonConnectionName);
				RuleTrigger notTriggeredYet = new GameProgressPropertyTrigger(gameProgressProperty, "false");
				trigger = AndTrigger.and(trigger, notTriggeredYet);

				RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.CUTSCENE, new PoisonDeactivationAction(poisonConnection), false);
				action = BlockAction.block(action, new SetGameProgressPropertyAction(gameProgressProperty, "true"));

				Rule rule = new Rule("poisonDeactivation." + poisonConnection.getName(), trigger, action);

				retVal.put(rule.getId(), rule);
			}
		}
		return retVal;
	}

}
