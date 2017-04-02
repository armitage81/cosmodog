package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
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
			
			if (collectible instanceof CollectibleComposed) {
				collectibleType = CollectibleComposed.class.getSimpleName();
			} else if (collectible instanceof CollectibleTool) {
				collectibleType = ((CollectibleTool)collectible).getToolType().name();
			} else if (collectible instanceof CollectibleWeapon) {
				CollectibleWeapon collectibleWeapon = (CollectibleWeapon)collectible;
				collectibleType = collectibleWeapon.getWeapon().getWeaponType().name().toLowerCase();
			} else if (collectible instanceof CollectibleAmmo) {
				collectibleType = CollectibleAmmo.class.getSimpleName();
			} else if (collectible instanceof CollectibleKey) {
				collectibleType = CollectibleKey.class.getSimpleName() + "_" + ((CollectibleKey)collectible).getKey().getDoorType();
			} else {
				collectibleType = ((CollectibleGoodie)collectible).getGoodieType().name();
			}
			return collectibleType.equals(pieceType);
		} else if (piece instanceof Vehicle) {
			return pieceType.equals("vehicle");
		} else if (piece instanceof Platform){
			return pieceType.equals("platform");
		} else {
			//Handle non collectible pieces here.
			return false;
		}
		
		
	}

}
