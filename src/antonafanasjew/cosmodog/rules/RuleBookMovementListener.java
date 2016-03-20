package antonafanasjew.cosmodog.rules;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.listener.movement.MovementListenerAdapter;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventChangedPosition;
import antonafanasjew.cosmodog.rules.events.GameEventEndedTurn;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class RuleBookMovementListener extends MovementListenerAdapter {

	private static final long serialVersionUID = -8999782338441869506L;

	@Override
	public void onEnteringTile(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		throwEvent(new GameEventChangedPosition());
	}

	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		throwEvent(new GameEventEndedTurn());
	}
	
	private void throwEvent(GameEvent gameEvent) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		RuleBook ruleBook = cosmodogGame.getRuleBook();
		
		List<Rule> rulesSortedByPriority = ruleBook.getRulesSortedByPriority();
		
		for (Rule rule : rulesSortedByPriority) {
			rule.apply(gameEvent);
		}
	}
	
}
