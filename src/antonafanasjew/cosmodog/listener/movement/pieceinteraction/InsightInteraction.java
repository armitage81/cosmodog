package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Iterator;
import java.util.Set;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.CamCenteringDecoratorAction;
import antonafanasjew.cosmodog.actions.cutscenes.PauseDecoratorAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Effect;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InsightInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class InsightInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		
		InventoryItem item = player.getInventory().get(InventoryItemType.INSIGHT);

		if (item != null) {
			InsightInventoryItem cosmodogCollectionItem = (InsightInventoryItem) item;
			cosmodogCollectionItem.increaseNumber();
		} else {
			InsightInventoryItem cosmodogCollectionItem = new InsightInventoryItem();
			cosmodogCollectionItem.setNumber(1);
			player.getInventory().put(InventoryItemType.INSIGHT, cosmodogCollectionItem);
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
		
		int x = player.getPositionX();
		int y = player.getPositionY();
		int l = Layers.LAYER_META_RADIATION;
		
		//Now add map modifications to get rid of the electricity fields that cause damage.
		
		cosmodogMap.getMapModification().modifyTile(x - 2, y, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x - 1, y, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x + 1, y, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x + 2, y, l, TileType.NO_RADIATION_MARKUP);
		
		cosmodogMap.getMapModification().modifyTile(x - 2, y - 1, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x - 1, y - 1, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x + 1, y - 1, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x + 2, y - 1, l, TileType.NO_RADIATION_MARKUP);
		
		cosmodogMap.getMapModification().modifyTile(x - 2, y - 2, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x - 1, y - 2, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x + 1, y - 2, l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(x + 2, y - 2, l, TileType.NO_RADIATION_MARKUP);
		
		//Now check if the alien base door can be opened and notify the player.
		
		if (item != null && ((InsightInventoryItem)item).getNumber() == Constants.MIN_INSIGHTS_TO_OPEN_ALIEN_BASE) {
			AsyncAction asyncAction1 = new PopUpNotificationAction("Something has changed. There is a feeling you did not have before. You peek at the large structure in the center of the valley.");
			AsyncAction asyncAction2 = new PopUpNotificationAction("Suddenly, you know. <br> You know about the base. <br> You know how to open it.");
			asyncAction2 = new PauseDecoratorAction(3000, 0, asyncAction2);
			asyncAction2 = new CamCenteringDecoratorAction(3000, 238, 238, asyncAction2, ApplicationContextUtils.getCosmodogGame());
			ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction1);
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, asyncAction2);			
		}
		
	}

}
