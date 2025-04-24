package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Sensor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

public class DynamicPieceCollisionValidatorForMoveable extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
		DynamicPiece dynamicPiece = cosmodogGame.dynamicPieceAtPosition(entrance.getPosition());
		if (dynamicPiece != null) {

			if (dynamicPiece instanceof Sensor) {
				//Sensors don't block moveables.
			} else {
				retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			}
		}
		return retVal;
	}

}
