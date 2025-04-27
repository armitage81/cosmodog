package antonafanasjew.cosmodog.collision.validators.moveable;

import antonafanasjew.cosmodog.collision.AbstractCollisionValidator;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.AutoBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Bollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.OneWayBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Sensor;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;

public class DynamicPieceCollisionValidatorForMoveable extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
		DynamicPiece dynamicPiece = cosmodogGame.dynamicPieceAtPosition(entrance.getPosition());
		if (dynamicPiece != null) {
			if (dynamicPiece instanceof AutoBollard autoBollard) {
				//Autobollards don't block moveables.
			} else if (dynamicPiece instanceof OneWayBollard oneWayBollard && oneWayBollard.direction == entrance.getEntranceDirection()) {
				//One-Way Bollards don't block moveables as long as they point in the same direction.
			} else if (dynamicPiece instanceof Bollard bollard && bollard.isOpen()) {
				//Bollards don't block moveables as long as they are open.
			} else if (dynamicPiece instanceof Sensor) {
				//Sensors don't block moveables.
			} else {
				retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
			}
		}
		return retVal;
	}

}
