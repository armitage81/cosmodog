package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Set;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;

public class ArmorInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		
		Set<Piece> mapPieces = cosmodogMap.getMapPieces();
		
		for (Piece piece2 : mapPieces) {
			if (piece2 instanceof Vehicle) {
				Vehicle vehicle = ((Vehicle)piece2);
				vehicle.increaseMaxLife(Player.LIFE_UNITS_IN_LIFEPACK, true);
			}
		}
		
		if (player.getInventory().hasVehicle()) {
			Vehicle vehicle = ((VehicleInventoryItem)(player.getInventory().get(InventoryItemType.VEHICLE))).getVehicle();
			vehicle.increaseMaxLife(5, true);
		}		
	}

}
