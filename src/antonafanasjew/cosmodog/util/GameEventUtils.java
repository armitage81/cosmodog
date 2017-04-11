package antonafanasjew.cosmodog.util;

import java.util.List;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.rules.events.GameEvent;

public class GameEventUtils {

	public static void throwEvent(GameEvent gameEvent) {
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		RuleBook ruleBook = cosmodogGame.getRuleBook();
		
		List<Rule> rulesSortedByPriority = ruleBook.getRulesSortedByPriority();
		
		for (Rule rule : rulesSortedByPriority) {
			rule.apply(gameEvent);
		}
	}
	
}
