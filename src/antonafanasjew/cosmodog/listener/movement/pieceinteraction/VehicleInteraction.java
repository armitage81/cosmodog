package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.NarrativeSequenceUtils;
import antonafanasjew.cosmodog.util.NotificationUtils;

public class VehicleInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		VehicleInventoryItem vehicleInventoryItem = new VehicleInventoryItem((Vehicle)piece);
		
		if (player.getInventory().get(InventoryItemType.FUEL_TANK) != null) {
			vehicleInventoryItem.getVehicle().setFuel(Vehicle.MAX_FUEL);
			player.getInventory().remove(InventoryItemType.FUEL_TANK);
		}

		if (vehicleInventoryItem.getVehicle().getFuel() > 0) {
			cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundVehicle()), true, false);
		} else {
			cosmodogGame.getCommentsStateUpdater().addNarrativeSequence(NarrativeSequenceUtils.commentNarrativeSequenceFromText(NotificationUtils.foundVehicleWithoutFuel()), true, false);
		}
		
		player.getInventory().put(InventoryItemType.VEHICLE, vehicleInventoryItem);
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_CAR_DOOR;
	}
	
}
