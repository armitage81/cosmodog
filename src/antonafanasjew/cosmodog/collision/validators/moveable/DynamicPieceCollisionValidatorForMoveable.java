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

import java.util.Optional;

public class DynamicPieceCollisionValidatorForMoveable extends AbstractCollisionValidator {

	@Override
	public CollisionStatus calculateStatusWithinMap(CosmodogGame cosmodogGame, Actor actor, CosmodogMap map, Entrance entrance) {
		CollisionStatus retVal = CollisionStatus.instance(actor, map, entrance, true, PassageBlockerType.PASSABLE);
		Optional<DynamicPiece> optDynamicPiece = map
				.getMapPieces()
				.piecesAtPosition(e -> e instanceof DynamicPiece, entrance.getPosition().getX(), entrance.getPosition().getY())
				.stream()
				.map(e -> (DynamicPiece)e)
				.findFirst();

		if (optDynamicPiece.isPresent()) {
			DynamicPiece dynamicPiece = optDynamicPiece.get();
            switch (dynamicPiece) {
                case AutoBollard autoBollard -> {
                    //Autobollards don't block moveables.
                }
                case OneWayBollard oneWayBollard when oneWayBollard.direction == entrance.getEntranceDirection() -> {
                    //One-Way Bollards don't block moveables as long as they point in the same direction.
                }
                case Bollard bollard when bollard.isOpen() -> {
                    //Bollards don't block moveables as long as they are open.
                }
                case Sensor sensor -> {
                    //Sensors don't block moveables.
                }
                default ->
                        retVal = CollisionStatus.instance(actor, map, entrance, false, PassageBlockerType.BLOCKED_DYNAMIC_PIECE, "");
            }
		}
		return retVal;
	}

}
