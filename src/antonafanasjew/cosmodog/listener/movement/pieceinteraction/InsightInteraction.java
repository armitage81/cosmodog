package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Iterator;
import java.util.Set;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Effect;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;

public class InsightInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		
		InventoryItem item = player.getInventory().get(InventoryItem.INVENTORY_ITEM_INSIGHT);

		if (item != null) {
			InsightInventoryItem cosmodogCollectionItem = (InsightInventoryItem) item;
			cosmodogCollectionItem.increaseNumber();
		} else {
			InsightInventoryItem cosmodogCollectionItem = new InsightInventoryItem();
			cosmodogCollectionItem.setNumber(1);
			player.getInventory().put(InventoryItem.INVENTORY_ITEM_INSIGHT, cosmodogCollectionItem);
		}

		// Now remove the electricity effect from the monolyth.
		int insightPosX = piece.getPositionX();
		int insightPosY = piece.getPositionY();

		int effectPosX = insightPosX;
		int effectPosY = insightPosY - 1;

		Set<Piece> effectPieces = cosmodogMap.getEffectPieces();
		Iterator<Piece> effectPiecesIt = effectPieces.iterator();
		while (effectPiecesIt.hasNext()) {
			Piece effectPiece = effectPiecesIt.next();
			if (effectPiece.getPositionX() == effectPosX && effectPiece.getPositionY() == effectPosY) {
				if (effectPiece instanceof Effect) {
					Effect effect = (Effect) effectPiece;
					if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ELECTRICITY)) {
						effectPiecesIt.remove();
						break;
					}
				}

			}
		}
	}

}
