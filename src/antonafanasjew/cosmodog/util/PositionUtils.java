package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public class PositionUtils {

	public static Position neighbourPositionInFacingDirection(Actor actor) {
		
		Position position = actor.getPosition().copy();
		
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
	
	public static DirectionType targetDirection(Piece reference, Position position) {
		Piece piece = new Piece();
		piece.setPosition(position);
		return targetDirection(reference, piece);
		
	}
	
	public static DirectionType targetDirection(Piece reference, Piece target) {
		
		int refX = (int)reference.getPosition().getX();
		int refY = (int)reference.getPosition().getY();
		
		int x = (int)target.getPosition().getX();
		int y = (int)target.getPosition().getY();
		
		int diffX = x - refX;
		int diffY = y - refY;
		
		int diffXAbs = diffX < 0 ? -diffX : diffX;
		int diffYAbs = diffY < 0 ? -diffY : diffY;
		
		boolean horizontal = diffXAbs >= diffYAbs;
		
		if (horizontal) {
			if (diffX >= 0) {
				return DirectionType.RIGHT;
			} else {
				return DirectionType.LEFT;
			}
		} else {
			if (diffY >= 0) {
				return DirectionType.DOWN;
			} else {
				return DirectionType.UP;
			}			
		}
	}
	
	public static float exactTargetDirection(Piece reference, Piece target) {

		int refX = (int)reference.getPosition().getX();
		int refY = (int)reference.getPosition().getY();

		int x = (int)target.getPosition().getX();
		int y = (int)target.getPosition().getY();
		
		float diffX = x - refX;
		float diffY = y - refY;
		
		float absDiffX = Math.abs(diffX);
		float absDiffY = Math.abs(diffY);
		
		float retVal;
		
		if (absDiffY == 0) {
			retVal = (float)Math.PI / 2;
		} else {
			float tan = absDiffX / absDiffY;
			retVal = (float)Math.atan(tan);
		}
		
		if (diffX >= 0 && diffY >= 0) {
			retVal = (float)(Math.PI - retVal);
		}
		
		if (diffX < 0 && diffY >= 0) {
			retVal = (float)(Math.PI + retVal);
		}
		
		if (diffX < 0 && diffY < 0) {
			retVal = (float)(2 * Math.PI - retVal);
		}
		
		
		return retVal;
	}
	
	
}
