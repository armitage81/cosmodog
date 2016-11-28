package antonafanasjew.cosmodog.collision;

import java.util.concurrent.Callable;

import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;

/**
 * This validator will pass for players without vehicles and will fail if player has a vehicle and it is out of fuel.
 * 
 * Take note: This 'collision' validator ignores the actual collisions and only checks for fuel.
 * It's result needs to be logically conjoined with other real collision validators.
 */
public class FuelCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		Player player = (Player) actor;
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			if (!vehicleInventoryItem.isExiting()) {
				Vehicle vehicle = vehicleInventoryItem.getVehicle();
				if (vehicle.outOfFuel()) {
					retVal = Features.getInstance().featureBoundFunction(Features.FEATURE_FUEL, new Callable<CollisionStatus>() {
						@Override
						public CollisionStatus call() throws Exception {
							return CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.FUEL_EMPTY);
						}
					}, retVal);
				}
			}
		}
		return retVal;
	}
	
}
