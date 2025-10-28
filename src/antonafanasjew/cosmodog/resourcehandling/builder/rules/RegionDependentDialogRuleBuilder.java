package antonafanasjew.cosmodog.resourcehandling.builder.rules;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.narration.EndingNarrationAction;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.resourcehandling.AbstractCsvBasedResourceWrapperBuilder;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.actions.narration.DialogWithAlisaNarrationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.triggers.EnteringRegionTrigger;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.Map;

public class RegionDependentDialogRuleBuilder extends AbstractCsvBasedResourceWrapperBuilder<Rule> {
	
	@Override
	protected Rule build(String line) {

		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();

		String[] values = line.split(";");
		
		String ruleName = values[0];
		String regionName = values[1];
		String gameLogsSeriesNameAndId = values[2];
		String onlyOnceFlag = values[3];
		String gameProgressProperty = values[4];
		String mapName = values[5];
		MapDescriptor mapDescriptor = mapDescriptors.get(mapName);
		boolean endingCutscene = values[6].equalsIgnoreCase("ending");
		
		
		RuleTrigger trigger = new EnteringRegionTrigger(mapDescriptor, ObjectGroups.OBJECT_GROUP_ID_REGIONS, regionName);
		boolean onlyOnce = Boolean.valueOf(onlyOnceFlag);
		
		if (onlyOnce) {
			RuleTrigger onlyOnceTrigger = new GameProgressPropertyTrigger(gameProgressProperty, "false");
			trigger = AndTrigger.and(onlyOnceTrigger, trigger);
		}
		
		String gameLogSeries = gameLogsSeriesNameAndId.split("/")[0];
		String gameLogId = gameLogsSeriesNameAndId.split("/")[1];
		
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		GameLog gameLog = gameLogs.getGameLogBySeriesAndId(gameLogSeries, gameLogId);

		AsyncAction asyncAction;
		if (endingCutscene) {
			asyncAction = new EndingNarrationAction(gameLog);
		} else {
			asyncAction = new DialogWithAlisaNarrationAction(gameLog);
		}
		RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
		
		if (onlyOnce) {
			RuleAction setPropertyAction = new SetGameProgressPropertyAction(gameProgressProperty, "true");
			action = BlockAction.block(action, setPropertyAction);
		}
				
		Rule rule = new Rule(ruleName, trigger, action, Rule.RULE_PRIORITY_20);

		return rule;
	}

	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/rulebuilder/regionmapping_dialogs.txt";
	}

}
