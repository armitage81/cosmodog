package antonafanasjew.cosmodog.collision;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Vehicle;

/**
 * Blocks passage for tiles which are occupied with an empty vehicle collectible.
 * Do not use it for player character on foot as he can enter such tiles (by collecting the vehicle)
 */
public class VehicleObstacleCollisionValidator extends AbstractCollisionValidator {

	@Override
	protected CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CustomTiledMap map, int tileX, int tileY) {
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		Piece piece = cosmodogMap.pieceAtTile(tileX, tileY);
		boolean vehicleOnTile = piece instanceof Vehicle;
		boolean resultingPassageFlag = !vehicleOnTile;
		int noPassageReason = resultingPassageFlag ? CollisionStatus.NO_PASSAGE_REASON_NO_REASON : CollisionStatus.NO_PASSAGE_REASON_BLOCKED_BY_VEHICLE_COLLECTIBLE;
		return CollisionStatus.instance(actor, map, tileX, tileY, resultingPassageFlag, noPassageReason);
	}

}
