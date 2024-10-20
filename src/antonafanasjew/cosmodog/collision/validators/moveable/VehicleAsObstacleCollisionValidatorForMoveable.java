package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.topology.Position;

public class VehicleAsObstacleCollisionValidatorForMoveable extends AbstractCollisionValidator {

	public static int N = 0;
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Position position) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, position, true, PassageBlockerType.PASSABLE);
		CosmodogMap cosmodogMap = cosmodogGame.mapOfPlayerLocation();
		Piece piece = cosmodogMap.pieceAtTile(position);
		boolean vehicleOnTile = piece instanceof Vehicle;
		boolean resultingPassageFlag = !vehicleOnTile;
		PassageBlockerType passageBlocker = resultingPassageFlag ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_BY_VEHICLE_COLLECTIBLE;
		retVal = CollisionStatus.instance(actor, map, position, resultingPassageFlag, passageBlocker);
		return retVal;
	}

}
