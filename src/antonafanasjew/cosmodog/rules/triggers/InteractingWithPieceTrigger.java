package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.MapDescriptor;
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
	private MapDescriptor mapDescriptor;
	
	//mapType can be null. In this case, the map type is not checked.
	public InteractingWithPieceTrigger(String pieceType, MapDescriptor mapDescriptor) {
		this.pieceType = pieceType;
		this.mapDescriptor = mapDescriptor;
	}

	@Override
	public boolean accept(GameEvent event) {

		if (!(event instanceof GameEventPieceInteraction pieceInteractionGameEvent)) {
			return false;
		}

        Piece piece = pieceInteractionGameEvent.getPiece();
		
		String eventPieceType = PiecesUtils.pieceType(piece);
		MapDescriptor eventMapDescriptor = piece.getPosition().getMapDescriptor();

		return pieceType.equals(eventPieceType) && (mapDescriptor == null || mapDescriptor.equals(eventMapDescriptor));
		
		
		
		
	}

}
