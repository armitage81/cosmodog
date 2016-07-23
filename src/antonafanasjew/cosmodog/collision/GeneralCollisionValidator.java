package antonafanasjew.cosmodog.collision;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;

/**
 * This collision validator should be used as the central point of validation.
 * It should invoke underlying validators based on actor status.
 */
public class GeneralCollisionValidator extends AbstractCollisionValidator {

	private UnequippedWalkerCollisionValidator unequippedWalkerCollisionValidator = new UnequippedWalkerCollisionValidator();
	private CollisionValidator vehicleCollisionValidator =  new OneBlocksAllCollisionValidator(Lists.newArrayList(new ActorOnWheelsCollisionValidator(), new VehicleObstacleCollisionValidator()));
	private FuelCollisionValidator fuelCollisionValidator = new FuelCollisionValidator();
	private BoatCollisionValidator boatCollisionValidator = new BoatCollisionValidator();
	
	
	/**
	 * the parameter consider vehicle is needed to describe the case when player is exiting the vehicle
	 * As the inventory item vehicle is set based on vehicle and players coordinates, the player is still considered
	 * to be in vehicle when exiting. On the other hand, the collision calculation needs to be made based on the player
	 * on foot. 
	 */
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		
		Player player = (Player)actor;
		VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
		BoatInventoryItem boatInventoryItem = (BoatInventoryItem)player.getInventory().get(InventoryItemType.BOAT);
		boolean vehicleCollision = vehicleInventoryItem != null && !vehicleInventoryItem.isExiting(); 
		boolean boatCollision = !vehicleCollision && boatInventoryItem != null;
		if (vehicleCollision) {
			CollisionStatus fuelCollisionStatus = fuelCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			CollisionStatus vehicleCollisionStatus = vehicleCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			if (fuelCollisionStatus.isPassable() && vehicleCollisionStatus.isPassable()) {
				return CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
			} else {
				if (fuelCollisionStatus.isPassable() == false) {
					return CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.FUEL_EMPTY);
				} else {
					return CollisionStatus.instance(actor, map, tileX, tileY, false, PassageBlockerType.BLOCKED);
				}
			}
		} else {
			if (boatCollision) {
				return boatCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			} else {
				return unequippedWalkerCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			}
		}
	}


}
