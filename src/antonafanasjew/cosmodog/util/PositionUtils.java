package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public class PositionUtils {

	public static Position neighbourPositionInFacingDirection(Actor actor) {
		
		Position position = Position.fromCoordinates(actor.getPositionX(), actor.getPositionY());
		
		if (actor.getDirection() == DirectionType.DOWN) {
			position.shift(0, 1);
		} else if (actor.getDirection() == DirectionType.UP) {
			position.shift(0, -1);
		} else if (actor.getDirection() == DirectionType.LEFT) {
			position.shift(-1, 0);
		} else if (actor.getDirection() == DirectionType.RIGHT) {
			position.shift(1, 0);
		}
		
		return position;
		
	}
	
}
