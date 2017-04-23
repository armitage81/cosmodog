package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;
import antonafanasjew.cosmodog.util.PiecesUtils;

/**
 * Triggers only for piece events and only if the piece is of given type.
 */
public class InteractingWithPieceTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 8881848782255743719L;
	private String pieceType;
	
	
	public InteractingWithPieceTrigger(String pieceType) {
		this.pieceType = pieceType;
	}

	@Override
	public boolean accept(GameEvent event) {

		if (event instanceof GameEventPieceInteraction == false) {
			return false;
		}
		
		GameEventPieceInteraction pieceInteractionGameEvent = (GameEventPieceInteraction)event;
		Piece piece = pieceInteractionGameEvent.getPiece();
		
		String eventPieceType = PiecesUtils.pieceType(piece);
		
		return pieceType.equals(eventPieceType);
		
		
		
		
	}

}
