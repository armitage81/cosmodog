package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import java.util.Collection;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.caching.PiecePredicates;
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
		
		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();
		
		Collection<Piece> mapPieces = cosmodogMap.getMapPieces().piecesOverall(PiecePredicates.VEHICLE);
		
		for (Piece piece2 : mapPieces) {
			if (piece2 instanceof Vehicle vehicle) {
                vehicle.increaseMaxLife(Player.ROBUSTNESS_UNITS_IN_ARMORPACK, true);
			}
		}
		
		if (player.getInventory().hasVehicle()) {
			Vehicle vehicle = ((VehicleInventoryItem)(player.getInventory().get(InventoryItemType.VEHICLE))).getVehicle();
			vehicle.increaseMaxLife(5, true);
		}		
		
		player.getGameProgress().increaseArmors();
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
