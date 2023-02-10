package antonafanasjew.cosmodog.collision;

import java.util.Collection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.PiecesUtils;
import antonafanasjew.cosmodog.util.PositionUtils;

/**
 * This validator is checking only the not-occupied platform as obstacle for the player (He may enter some areas of it) 
 */
public class PlatformAsObstacleForPlayerCollisionValidator extends AbstractCollisionValidator {

	private static class BlockInfo {
		public DirectionType directionType;
		public int actorType; //0=any, 1=OnFoot, 2=onVehicle, 3=Enemy
		
		public static BlockInfo fromDir(DirectionType dt) {
			BlockInfo retVal = new BlockInfo();
			retVal.directionType = dt;
			retVal.actorType = 0;
			return retVal;
		}
		
		public static BlockInfo fromDirCarOnly(DirectionType dt) {
			BlockInfo retVal = new BlockInfo();
			retVal.directionType = dt;
			retVal.actorType = 2;
			return retVal;
		}
	
	}
	
	
	private static Multimap<Position, BlockInfo> COLLISIONDATA = HashMultimap.create();
	
	static {
		COLLISIONDATA.put(Position.fromCoordinates(-1, -4), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 3, -4), BlockInfo.fromDir(DirectionType.DOWN));
		                                                 
		COLLISIONDATA.put(Position.fromCoordinates(-2, -3), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates(-2, -3), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 0, -3), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 0, -3), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 1, -3), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 2, -3), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 2, -3), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 4, -3), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 4, -3), BlockInfo.fromDir(DirectionType.LEFT));
		                                                 
		COLLISIONDATA.put(Position.fromCoordinates(-3, -2), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates(-3, -2), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates(-1, -2), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates(-1, -2), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 0, -2), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 1, -2), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 2, -2), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 3, -2), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 3, -2), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 5, -2), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 5, -2), BlockInfo.fromDir(DirectionType.LEFT));
		                                                 
		COLLISIONDATA.put(Position.fromCoordinates(-4, -1), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates(-2, -1), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates(-2, -1), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates(-2, -1), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 0, -1), BlockInfo.fromDirCarOnly(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 4, -1), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 4, -1), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 4, -1), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 6, -1), BlockInfo.fromDir(DirectionType.LEFT));
		                                                 
		COLLISIONDATA.put(Position.fromCoordinates(-3,  0), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates(-3,  0), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates(-1,  0), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates(-1,  0), BlockInfo.fromDirCarOnly(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates(1,  0), BlockInfo.fromDirCarOnly(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 3,  0), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 5,  0), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 5,  0), BlockInfo.fromDir(DirectionType.LEFT));
		                                                 
		COLLISIONDATA.put(Position.fromCoordinates(-3,  1), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates(-1,  1), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates(-1,  1), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 0,  1), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 0,  1), BlockInfo.fromDirCarOnly(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 1,  1), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 1,  1), BlockInfo.fromDirCarOnly(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 2,  1), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 2,  1), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 3,  1), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 3,  1), BlockInfo.fromDir(DirectionType.DOWN));
		COLLISIONDATA.put(Position.fromCoordinates( 3,  1), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 5,  1), BlockInfo.fromDir(DirectionType.LEFT));
		                                                 
		COLLISIONDATA.put(Position.fromCoordinates(-3,  2), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 1,  2), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 1,  2), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 2,  2), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 2,  2), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 5,  2), BlockInfo.fromDir(DirectionType.LEFT));
		
		COLLISIONDATA.put(Position.fromCoordinates(-2,  3), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates(-2,  3), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 0,  3), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 0,  3), BlockInfo.fromDirCarOnly(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 0,  3), BlockInfo.fromDir(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 2,  3), BlockInfo.fromDir(DirectionType.RIGHT));
		COLLISIONDATA.put(Position.fromCoordinates( 2,  3), BlockInfo.fromDirCarOnly(DirectionType.LEFT));
		COLLISIONDATA.put(Position.fromCoordinates( 4,  3), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 4,  3), BlockInfo.fromDir(DirectionType.LEFT));
		
		COLLISIONDATA.put(Position.fromCoordinates(-1,  4), BlockInfo.fromDir(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 1,  4), BlockInfo.fromDirCarOnly(DirectionType.UP));
		COLLISIONDATA.put(Position.fromCoordinates( 3,  4), BlockInfo.fromDir(DirectionType.UP));

	}
	
	/**
	 * This collision validator is for the platform on rails. It checks a separate collision layer.
	 * 
	 * Take care: It is using cache to avoid bad design of piece storage on the map (simple set). That means, it assumes an non-destroyable and unique Platform object in the game.
	 * 
	 */
	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		
		boolean blocked = false;
		
		Platform platform = cosmodogGame.getMap().getCachedPlatform(cosmodogGame);

//		//Take care: If exiting, platforms position is not updated. At this point of time, it is still an inventory item.
//		//So its position is still the one that was when the player entered it.
//		//Always return unblocked passage in such cases.
//		if (actor instanceof Player) {
//			Player player = (Player)actor;
//			PlatformInventoryItem platformInventoryItem = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);
//			if (platformInventoryItem != null && platformInventoryItem.isExiting()) {
//				
//			}
//		}
		
		if (platform != null) { //It is null, if collected, that is if the player is sitting inside of it. 
		
			if (PiecesUtils.distanceBetweenPieces(actor, platform) <= 10) {
				int actorOffsetX = actor.getPositionX() - platform.getPositionX();
				int actorOffsetY = actor.getPositionY() - platform.getPositionY();
				Position offsetPosition = Position.fromCoordinates(actorOffsetX, actorOffsetY);
				
				Collection<BlockInfo> blockInfosForActorsPosition = COLLISIONDATA.get(offsetPosition);
				
				if (blockInfosForActorsPosition != null) { //It will be null, if no collisions are defined, so it would be fine to pass.
					DirectionType movementAttemptDirection = PositionUtils.targetDirection(actor, tileX, tileY);
					for (BlockInfo blockInfo : blockInfosForActorsPosition) {
						if (blockInfo.directionType.equals(movementAttemptDirection)) {
							
							boolean inCar = false;
							
							if (actor instanceof Player) {
								VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)(((Player)actor).getInventory().get(InventoryItemType.VEHICLE));
								if (vehicleInventoryItem != null && vehicleInventoryItem.isExiting() == false) {
									inCar = true;
								}
							}
							
							if (blockInfo.actorType == 0) {
								blocked = true;
								break;
							}
							
							if (blockInfo.actorType == 1 && !inCar) { //Block only on foot
								blocked = true;
								break;
							}
							
							if (blockInfo.actorType == 2 && inCar) { //Block only car
								blocked = true;
								break;
							}
	
						}
					}
				}
			}
		}
		return CollisionStatus.instance(actor, map, tileX, tileY, !blocked, PassageBlockerDescriptor.fromPassageBlockerType(blocked ? PassageBlockerType.BLOCKED : PassageBlockerType.PASSABLE));
		
	}

}
