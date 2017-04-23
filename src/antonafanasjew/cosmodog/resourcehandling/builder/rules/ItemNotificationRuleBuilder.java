package antonafanasjew.cosmodog.resourcehandling.builder.rules;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.resourcehandling.AbstractResourceWrapperBuilder;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.InteractingWithPieceTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;

public class ItemNotificationRuleBuilder extends AbstractResourceWrapperBuilder<Rule> {

	@Override
	protected Rule build(String line) {
		String[] values = line.split(";");
		
		String ruleName = values[0];
		String pieceName = values[1];
		String ifGameProgressPropertyValueIs = values[2];
		String gameProgressProperty = values[3];
		String description = values[4];
		short priority = Short.valueOf(values[5]);
			
		RuleTrigger trigger = new InteractingWithPieceTrigger(pieceName);
		
		boolean valueIsAny = ifGameProgressPropertyValueIs.equalsIgnoreCase("ANY");
		
		if (!valueIsAny) {
			RuleTrigger specificValueTrigger = new GameProgressPropertyTrigger(gameProgressProperty, ifGameProgressPropertyValueIs, "0");
			trigger = AndTrigger.and(specificValueTrigger, trigger);
		}
		
		
		AsyncAction asyncAction = new PopUpNotificationAction(description);
		RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		
		if (!valueIsAny) {
			int value = Integer.valueOf(ifGameProgressPropertyValueIs);
			value = value + 1;
			RuleAction setPropertyAction = new SetGameProgressPropertyAction(gameProgressProperty, String.valueOf(value));
			action = BlockAction.block(action, setPropertyAction);
		}
				
		Rule rule = new Rule(ruleName, trigger, action, priority);
		return rule;
	}
	
	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/rulebuilder/itemnotificationmapping.txt";
	}

}
