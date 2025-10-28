package antonafanasjew.cosmodog.resourcehandling.builder.rules;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.resourcehandling.AbstractCsvBasedResourceWrapperBuilder;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.actions.SetGameProgressPropertyAction;
import antonafanasjew.cosmodog.actions.narration.MonolithNarrationAction;
import antonafanasjew.cosmodog.rules.actions.composed.BlockAction;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.InteractingWithPieceTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.Map;

/**
 * Builds rules for dialogs, which are based on the number of previous collection of the piece type.
 * Example: For each collection of the insight piece, there will be a new dialog option with always the same order.
 */
public class MultiInstancePieceRuleBuilder extends AbstractCsvBasedResourceWrapperBuilder<Rule> {

	@Override
	protected Rule build(String line) {

		Map<String, MapDescriptor> mapDescriptors = ApplicationContextUtils.mapDescriptors();

		String[] values = line.split(";");
		
		String ruleName = values[0];
		String pieceName = values[1];
		String mapName = values[2];
		MapDescriptor mapDescriptor = mapDescriptors.get(mapName);
		String gameLogsSeriesNameAndId = values[3];
		String gameProgressProperty = values[4];
		String gameProgressPropertyValue = values[5];
		int gameProgressPropertyCount = Integer.valueOf(gameProgressPropertyValue);
		short priority = Short.valueOf(values[6]);
		
		
		RuleTrigger pieceInteractionTrigger = new InteractingWithPieceTrigger(pieceName, mapDescriptor);
		RuleTrigger gameProgressPropertyValueTrigger = new GameProgressPropertyTrigger(gameProgressProperty, gameProgressPropertyValue, "0");
		
		RuleTrigger trigger = AndTrigger.and(pieceInteractionTrigger, gameProgressPropertyValueTrigger);
		
		String gameLogSeries = gameLogsSeriesNameAndId.split("/")[0];
		String gameLogId = gameLogsSeriesNameAndId.split("/")[1];
		
		GameLogs gameLogs = ApplicationContext.instance().getGameLogs();
		GameLog gameLog = gameLogs.getGameLogBySeriesAndId(gameLogSeries, gameLogId);
		
		AsyncAction asyncAction = new MonolithNarrationAction(gameLog);
		RuleAction action = new AsyncActionRegistrationRuleAction(AsyncActionType.MODAL_WINDOW, asyncAction);
		
		RuleAction setPropertyAction = new SetGameProgressPropertyAction(gameProgressProperty, String.valueOf(gameProgressPropertyCount + 1));
		action = BlockAction.block(action, setPropertyAction);
				
		Rule rule = new Rule(ruleName, trigger, action, priority);
		return rule;
	}
	
	@Override
	protected String resourcePath() {
		return "antonafanasjew/cosmodog/rulebuilder/multiinstancepiecemapping.txt";
	}

}
