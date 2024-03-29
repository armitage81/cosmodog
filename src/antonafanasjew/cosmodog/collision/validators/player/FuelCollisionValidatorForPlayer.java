package antonafanasjew.cosmodog.collision.validators.player;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
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
public class FuelCollisionValidatorForPlayer extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		Player player = (Player) actor;
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			if (!vehicleInventoryItem.isExiting()) {
				Vehicle vehicle = vehicleInventoryItem.getVehicle();
				if (vehicle.outOfFuel()) {
					return CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.FUEL_EMPTY);
				}
			}
		}
		return retVal;
	}
	
}
