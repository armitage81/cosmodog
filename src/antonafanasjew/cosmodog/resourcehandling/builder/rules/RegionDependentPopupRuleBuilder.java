package antonafanasjew.cosmodog.resourcehandling.builder.rules;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.resourcehandling.AbstractCsvBasedResourceWrapperBuilder;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.triggers.EnteringRegionTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;

public class RegionDependentPopupRuleBuilder extends AbstractCsvBasedResourceWrapperBuilder<Rule> {
	
	@Override
	protected Rule build(String line) {
		String[] values = line.split(";");
		
		String ruleName = values[0];
		String regionName = values[1];
		String text = values[2];
		String onlyOnceFlag = values[3];
		String gameProgressProperty = values[4];
		
		
		RuleTrigger trigger = new EnteringRegionTrigger(ObjectGroups.OBJECT_GROUP_ID_REGIONS, regionName);
		boolean onlyOnce = Boolean.valueOf(onlyOnceFlag);
		
		if (onlyOnce) {
			RuleTrigger onlyOnceTrigger = new GameProgressPropertyTrigger(gameProgressProperty, "false");
			trigger = AndTrigger.and(onlyOnceTrigger, trigger);
		}
		
		AsyncAction asyncAction = new PopUpNotificationAction(text);
		RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction);
		
		if (onlyOnce) {
			RuleAction setPropertyAction = new SetGameProgressPropertyAction(gameProgressProperty, "true");
			action = BlockAction.block(action, setPropertyAction);
		}
				
		Rule rule = new Rule(ruleName, trigger, action, Rule.RULE_PRIORITY_20);

		return rule;
	}

	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/rulebuilder/regionmapping_popups.txt";
	}

}
