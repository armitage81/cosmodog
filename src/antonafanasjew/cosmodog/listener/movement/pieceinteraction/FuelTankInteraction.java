package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;

import java.util.Collection;

public class FuelTankInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {

		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();

		Collection<Piece> mapPieces = cosmodogMap.getMapPieces().values();

		for (Piece piece2 : mapPieces) {
			if (piece2 instanceof Vehicle vehicle) {
                vehicle.upgradeMaxFuel();
			}
		}

		if (player.getInventory().hasVehicle()) {
			Vehicle vehicle = ((VehicleInventoryItem)(player.getInventory().get(InventoryItemType.VEHICLE))).getVehicle();
			vehicle.upgradeMaxFuel();
		}

		player.getGameProgress().increaseFuelTanks();
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_POWERUP;
	}
	
}
