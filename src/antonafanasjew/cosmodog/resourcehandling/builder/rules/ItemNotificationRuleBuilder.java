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
		boolean onlyFirstTime = Boolean.valueOf(values[2]);
		String gameProgressProperty = values[3];
		String description = values[4];
			
		RuleTrigger trigger = new InteractingWithPieceTrigger(pieceName);
		
		if (onlyFirstTime) {
			RuleTrigger onlyOnceTrigger = new GameProgressPropertyTrigger(gameProgressProperty, "false");
			trigger = AndTrigger.and(onlyOnceTrigger, trigger);
		}
		
		
		AsyncAction asyncAction = new PopUpNotificationAction(description);
		RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		
		if (onlyFirstTime) {
			RuleAction setPropertyAction = new SetGameProgressPropertyAction(gameProgressProperty, "true");
			action = BlockAction.block(action, setPropertyAction);
		}
				
		Rule rule = new Rule(ruleName, trigger, action, Rule.RULE_PRIORITY_40);
		return rule;
	}
	
	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/rulebuilder/itemnotificationmapping.txt";
	}

}
