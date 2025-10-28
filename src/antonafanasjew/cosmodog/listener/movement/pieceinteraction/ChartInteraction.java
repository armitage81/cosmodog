package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;

public class ChartInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		ChartInventoryItem item = (ChartInventoryItem)player.getInventory().get(InventoryItemType.CHART);
		if (item != null) {
			item = (ChartInventoryItem) item;
		} else {
			item = new ChartInventoryItem();
			item.setNumber(0); //It is 1 by default for all countable items.
			player.getInventory().put(InventoryItemType.CHART, item);
		}
		
		int playerPositionX = (int)player.getPosition().getX();
		int playerPositionY = (int)player.getPosition().getY();
		
		int chartPieceWidth = cosmodogGame.mapOfPlayerLocation().getMapDescriptor().getWidth() / ChartInventoryItem.ACTUAL_CHART_PIECE_NUMBER_X;
		int chartPieceHeight = cosmodogGame.mapOfPlayerLocation().getMapDescriptor().getHeight() / ChartInventoryItem.ACTUAL_CHART_PIECE_NUMBER_Y;
		
		int chartPiecePositionX = playerPositionX / chartPieceWidth;
		int chartPiecePositionY = playerPositionY / chartPieceHeight;
		
		item.discoverPiece(chartPiecePositionX, chartPiecePositionY);
		
	}

}
