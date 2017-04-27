package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.BoatInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;

import com.google.common.collect.Lists;

/**
 * This collision validator should be used as the central point of validation.
 * It should invoke underlying validators based on actor status.
 */
public class GeneralCollisionValidatorForPlayer extends AbstractCollisionValidator {

	private CollisionValidator unequippedWalkerCollisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(new UnequippedWalkerCollisionValidator()));
	private CollisionValidator vehicleCollisionValidator =  new OneBlocksAllCollisionValidator(Lists.newArrayList(new ActorOnWheelsCollisionValidator(), new VehicleObstacleCollisionValidator(), new NonInteractivePieceCollisionValidator()));
	private FuelCollisionValidator fuelCollisionValidator = new FuelCollisionValidator();
	private BoatCollisionValidator boatCollisionValidator = new BoatCollisionValidator();
	private PlatformAsVehicleCollisionValidator platformAsVehicleCollisionValidator = new PlatformAsVehicleCollisionValidator();
	private CollisionValidator platformAsObstacleOnFootCollisionValidator = new PlatformAsObstacleForPlayerCollisionValidator();
	private CollisionValidator platformAsObstacleForCarCollisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(platformAsObstacleOnFootCollisionValidator, new VehicleObstacleCollisionValidator()));
	
	/**
	 * the parameter consider vehicle is needed to describe the case when player is exiting the vehicle
	 * As the inventory item vehicle is set based on vehicle and players coordinates, the player is still considered
	 * to be in vehicle when exiting. On the other hand, the collision calculation needs to be made based on the player
	 * on foot. 
	 */
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		Player player = (Player)actor;
		VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
		BoatInventoryItem boatInventoryItem = (BoatInventoryItem)player.getInventory().get(InventoryItemType.BOAT);
		PlatformInventoryItem platformInventoryItem = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);
		
		boolean platformAsVehicleCollision = platformInventoryItem != null && !platformInventoryItem.isExiting();
		
		//To check whether the player can move outside the platform, he or the target need to be on the platform.
		//If we'd check only for target, the player could jump from the border of the platform as the target is not part of it
		boolean platformAsObstacleCollision = !platformAsVehicleCollision && (CosmodogMapUtils.isTileOnPlatform(tileX, tileY) || CosmodogMapUtils.isTileOnPlatform(actor.getPositionX(), actor.getPositionY()));
		boolean vehicleCollision = vehicleInventoryItem != null && !vehicleInventoryItem.isExiting(); 

		boolean boatCollision = !vehicleCollision && boatInventoryItem != null;

		//Player is on the platform but not using it.
		if (platformAsObstacleCollision) {
			if (vehicleCollision) {
				return platformAsObstacleForCarCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			} else {
				return platformAsObstacleOnFootCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			}
		} else if (vehicleCollision) {
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
		} else if (platformAsVehicleCollision) {
			return platformAsVehicleCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
		} else {
			if (boatCollision) {
				return boatCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			} else {
				return unequippedWalkerCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			}
		}
	}


}
