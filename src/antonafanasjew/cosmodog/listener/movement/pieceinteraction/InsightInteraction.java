package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.camera.CamCenteringDecoratorAction;
import antonafanasjew.cosmodog.actions.popup.PauseDecoratorAction;
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
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
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

		Predicate<Piece> beingElectricityEffect = e -> {
			return e instanceof Effect && ((Effect)e).getEffectType().equals(Effect.EFFECT_TYPE_ELECTRICITY);
		};

		List<Piece> electricityEffectPieces = cosmodogMap.getMapPieces().piecesAtPosition(beingElectricityEffect, effectPosition.getX(), effectPosition.getY());
		for (Piece electricityEffectPiece : electricityEffectPieces ) {
			cosmodogMap.getMapPieces().removePiece(electricityEffectPiece);
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
		
	}

}
