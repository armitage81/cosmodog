package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Iterator;
import java.util.Set;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.CamCenteringDecoratorAction;
import antonafanasjew.cosmodog.actions.cutscenes.PauseDecoratorAction;
import antonafanasjew.cosmodog.domains.MapType;
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
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class InsightInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();
		
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
		Position insightPosition = piece.getPosition();
		Position effectPosition = Position.fromCoordinates(insightPosition.getX(), insightPosition.getY() - 1, insightPosition.getMapType());

		Set<Piece> effectPieces = cosmodogMap.getEffectPieces();
		Iterator<Piece> effectPiecesIt = effectPieces.iterator();
		while (effectPiecesIt.hasNext()) {
			Piece effectPiece = effectPiecesIt.next();
			if (effectPiece.getPosition().equals(effectPosition)) {
				if (effectPiece instanceof Effect effect) {
                    if (effect.getEffectType().equals(Effect.EFFECT_TYPE_ELECTRICITY)) {
						effectPiecesIt.remove();
						break;
					}
				}

			}
		}
		
		Position playerPosition = player.getPosition();
		int l = Layers.LAYER_META_RADIATION;
		
		//Now add map modifications to get rid of the electricity fields that cause damage.
		
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(-2, 0), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(-1, 0), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(1, 0), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(2, 0), l, TileType.NO_RADIATION_MARKUP);
		
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(-2, -1), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(-1, -1), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(1, -1), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(2, -1), l, TileType.NO_RADIATION_MARKUP);
		
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(-2, -2), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(-1, -2), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(1, -2), l, TileType.NO_RADIATION_MARKUP);
		cosmodogMap.getMapModification().modifyTile(playerPosition.shifted(2, -2), l, TileType.NO_RADIATION_MARKUP);
		
		//Now check if the alien base door can be opened and notify the player.
		
		if (item != null && ((InsightInventoryItem)item).getNumber() == Constants.MIN_INSIGHTS_TO_OPEN_ALIEN_BASE) {
			AsyncAction asyncAction2 = new PopUpNotificationAction("You can enter the alien base now.");
			asyncAction2 = new PauseDecoratorAction(3000, 0, asyncAction2);
			asyncAction2 = new CamCenteringDecoratorAction(3000, Position.fromCoordinates(238, 238, MapType.MAIN), asyncAction2, ApplicationContextUtils.getCosmodogGame());
			ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, asyncAction2);
		}
		
	}

}
