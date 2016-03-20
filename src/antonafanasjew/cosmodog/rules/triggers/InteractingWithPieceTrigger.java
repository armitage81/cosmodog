package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleItem;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;

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
		if (piece instanceof Collectible) {
			
			Collectible collectible = (Collectible)piece;
			
			String collectibleType;
			
			if (collectible instanceof CollectibleItem) {
				collectibleType = ((CollectibleItem)collectible).getItemType();
			} else {
				collectibleType = collectible.getCollectibleType();
			}
			
			return collectibleType.equals(pieceType);
		} else if (piece instanceof Vehicle) {
			return pieceType.equals("vehicle");
		} else {
			//Handle non collectible pieces here.
			return false;
		}
		
		
	}

}
