package antonafanasjew.cosmodog.collision.validators.player;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.topology.Position;

/**
 * This collision validator will always pass if the player is not in a vehicle.
 * 
 * If the player is in a vehicle (and not exiting it), this collision validator will block in case the target tile is another vehicle
 * and it will pass in all the other cases.
 */
public class VehicleAsObstacleCollisionValidatorForPlayer extends AbstractCollisionValidator {

	public static int N = 0;
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, position, true, PassageBlockerType.PASSABLE);
		Player player = (Player) actor;
		if (player.getInventory().hasVehicle()) {
			VehicleInventoryItem vehicleInventoryItem = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE);
			if (!vehicleInventoryItem.isExiting()) {
				Piece piece = map.pieceAtTile(position);
				boolean vehicleOnTile = piece instanceof Vehicle;
				boolean resultingPassageFlag = !vehicleOnTile;
				PassageBlockerType passageBlocker = resultingPassageFlag ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_BY_VEHICLE_COLLECTIBLE;
				retVal = CollisionStatus.instance(actor, map, position, resultingPassageFlag, passageBlocker);
			}
		}
		return retVal;
	}

}
