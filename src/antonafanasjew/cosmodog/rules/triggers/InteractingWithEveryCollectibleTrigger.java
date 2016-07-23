package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;

/**
 * Triggers only for piece events but regardless of the piece type as long as it is a collectible piece.
 */
public class InteractingWithEveryCollectibleTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 8881848782255743719L;
	
	@Override
	public boolean accept(GameEvent event) {

		if (event instanceof GameEventPieceInteraction == false) {
			return false;
		}
		
		GameEventPieceInteraction pieceInteractionGameEvent = (GameEventPieceInteraction)event;
		Piece piece = pieceInteractionGameEvent.getPiece();
		
		if ((piece instanceof Collectible) == false) {
			return false;
		}
		
		return true;
		
	}

}
