package antonafanasjew.cosmodog.rules;

import java.util.List;

import antonafanasjew.cosmodog.listener.movement.PieceInteractionListenerAdapter;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class RuleBookPieceInteractionListener extends PieceInteractionListenerAdapter {

	private static final long serialVersionUID = 493016378399062531L;

	@Override
	public void beforeInteraction(Piece piece) {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		RuleBook ruleBook = cosmodogGame.getRuleBook();
	
		List<Rule> rulesSortedByPriority = ruleBook.getRulesSortedByPriority();
		
		for (Rule rule : rulesSortedByPriority) {
			rule.apply(new GameEventPieceInteraction(piece));
		}
	}
	
	
}
