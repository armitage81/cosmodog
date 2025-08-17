package antonafanasjew.cosmodog.listener.movement.pieceinteraction;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class VehicleInteraction extends AbstractPieceInteraction {

	@Override
	protected void interact(Piece piece, ApplicationContext applicationContext, CosmodogGame cosmodogGame, Player player) {
		VehicleInventoryItem vehicleInventoryItem = new VehicleInventoryItem((Vehicle)piece);
		
		if (player.getInventory().get(InventoryItemType.FUEL_TANK) != null) {
			
			ActionRegistry actionRegistry = ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry();
			actionRegistry.registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction("You used the fuel canister to refuel the car."));

			Vehicle vehicle = vehicleInventoryItem.getVehicle();

			vehicle.setFuel(vehicle.getMaxFuel());
			player.getInventory().remove(InventoryItemType.FUEL_TANK);
		}
		
		//Entering a vehicle must reset thirst and hunger. This makes sense since the metaphor is that the car has infinite supplies.
		//But: the player has the access to the car compartment only if the corresponding flag is set in the game progress. (It is set when the car race is won in the North-West of the map.)
		if (player.getGameProgress().getProgressProperties().get(GameProgress.GAME_PROGRESS_PROPERTY_ACCESSTOCARCOMPARTMENT) != null) {
			player.setFood(player.getCurrentMaxFood());
			player.setLifeLentForHunger(0);
			player.setWater(player.getCurrentMaxWater());
			player.setLifeLentForThirst(0);
		}

		player.getInventory().put(InventoryItemType.VEHICLE, vehicleInventoryItem);
	}

	@Override
	public String soundResource() {
		return SoundResources.SOUND_CAR_DOOR;
	}
	
}
