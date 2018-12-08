package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
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

	private CollisionValidator otherVehicleCollisionValidator = new VehicleObstacleCollisionValidator();
	private CollisionValidator defaultCollisionValidator = new OneBlocksAllCollisionValidator(Lists.newArrayList(new UnpassableCollisionValidator(), new WaterCollisionValidator(), new SnowCollisionValidator()));
	private CollisionValidator vehicleCollisionValidator =  new OneBlocksAllCollisionValidator(Lists.newArrayList(new ActorOnWheelsCollisionValidator(), new NonInteractivePieceCollisionValidator()));
	private FuelCollisionValidator fuelCollisionValidator = new FuelCollisionValidator();
	private PlatformAsVehicleCollisionValidator platformAsVehicleCollisionValidator = new PlatformAsVehicleCollisionValidator();
	private CollisionValidator platformAsObstacleCollisionValidator = new PlatformAsObstacleForPlayerCollisionValidator();

	
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
		PlatformInventoryItem platformInventoryItem = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);
		
		boolean exitingPlatformCollision = platformInventoryItem != null && platformInventoryItem.isExiting();
		boolean platformAsVehicleCollision = platformInventoryItem != null && !platformInventoryItem.isExiting();
		
		//To check whether the player can move outside the platform, he or the target need to be on the platform.
		//If we'd check only for target, the player could jump from the border of the platform as the target is not part of it
		boolean platformAsObstacleCollision = !platformAsVehicleCollision && (CosmodogMapUtils.isTileOnPlatform(tileX, tileY) || CosmodogMapUtils.isTileOnPlatform(actor.getPositionX(), actor.getPositionY()));
		boolean vehicleCollision = vehicleInventoryItem != null && !vehicleInventoryItem.isExiting(); 

		
		if (vehicleCollision) {
			
			//Check fuel collision
			CollisionStatus fuelCollisionStatus = fuelCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			if (fuelCollisionStatus.isPassable() == false) {
				return fuelCollisionStatus;
			}
			
			//Check collision against other vehicle
			CollisionStatus otherVehicleCollisionStatus = otherVehicleCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			if (otherVehicleCollisionStatus.isPassable() == false) {
				return otherVehicleCollisionStatus;
			}
		}
		
		//Check exiting platform collision. It is always PASSABLE
		if (exitingPlatformCollision) {
			return CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		}
			
		//All cases when player or the target tile is on platform (and not driving)
		if (platformAsObstacleCollision) {
			
			//Only check platform obstacles
			CollisionStatus platformCollisionStatus = platformAsObstacleCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
			
			//If platform blocks passage return IMPASSABLE
			if (!platformCollisionStatus.isPassable()) {
				return platformCollisionStatus;
			}
			
			//If no platform obstacles and target tile on platform, there is no need to check for the terrain type collisions, so return PASSABLE
			if (CosmodogMapUtils.isTileOnPlatform(tileX, tileY)) {
				return platformCollisionStatus;
			}
		} 
		
		if (vehicleCollision) {
			return vehicleCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
		} 
		
		if (platformAsVehicleCollision) {
			return platformAsVehicleCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
		} 
		
		return defaultCollisionValidator.collisionStatus(cosmodogGame, actor, map, tileX, tileY);
	}


}
