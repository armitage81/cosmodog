package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;

public abstract class AbstractPieceInteraction implements PieceInteraction {

	@Override
	public void beforeInteraction(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {

		RuleBook ruleBook = cosmodogGame.getRuleBook();
	
		List<Rule> rulesSortedByPriority = ruleBook.getRulesSortedByPriority();
		
		for (Rule rule : rulesSortedByPriority) {
			rule.apply(new GameEventPieceInteraction(piece));
		}		
	}
	
	@Override
	public void afterInteraction(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
	}
	
	/*
	 * Override it to have item specific sounds.
	 */
	public String soundResource() {
		return SoundResources.SOUND_COLLECTED;
	}
	
	@Override
	public void interactWithPiece(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		applicationContext.getSoundResources().get(soundResource()).play();
		interact(piece, applicationContext, cosmodogGame, player);
	}

	protected abstract void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player);

}
