package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Vehicle;

public class VehicleAsObstacleCollisionValidatorForMoveable extends AbstractCollisionValidator {

	public static int N = 0;
	
	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, int tileX, int tileY) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, tileX, tileY, true, PassageBlockerType.PASSABLE);
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		Piece piece = cosmodogMap.pieceAtTile(tileX, tileY);
		boolean vehicleOnTile = piece instanceof Vehicle;
		boolean resultingPassageFlag = !vehicleOnTile;
		PassageBlockerType passageBlocker = resultingPassageFlag ? PassageBlockerType.PASSABLE : PassageBlockerType.BLOCKED_BY_VEHICLE_COLLECTIBLE;
		retVal = CollisionStatus.instance(actor, map, tileX, tileY, resultingPassageFlag, passageBlocker);
		return retVal;
	}

}
