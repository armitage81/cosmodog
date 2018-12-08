package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rules.actions.async.PopUpNotificationAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class VehicleInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		VehicleInventoryItem vehicleInventoryItem = new VehicleInventoryItem((Vehicle)piece);
		
		if (player.getInventory().get(InventoryItemType.FUEL_TANK) != null) {
			
			ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
			actionRegistry.registerAction(AsyncActionType.BLOCKING_INTERFACE, new PopUpNotificationAction("You used the fuel canister to refuel the car."));
			
			vehicleInventoryItem.getVehicle().setFuel(Vehicle.MAX_FUEL);
			player.getInventory().remove(InventoryItemType.FUEL_TANK);
		}
		
		player.getInventory().put(InventoryItemType.VEHICLE, vehicleInventoryItem);
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_CAR_DOOR;
	}
	
}
