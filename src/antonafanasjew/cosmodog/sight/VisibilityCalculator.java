package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;

/**
 * Takes a sight object and the position and direction of a point and 
 * can be used to calculate whether a given point is within or out of sight.
 */
public class VisibilityCalculator {

	private Sight sight;
	
	//Direction from 0 to almost 360, counter clockwise as counted from the 'top/north point'.
	private float actorDirectionAngle;
	
	private float centerX;
	private float centerY;

	public VisibilityCalculator() {

	}
	
	public static VisibilityCalculator create(Sight sight, Actor actor, int tileWidth, int tileHeight) {
		
		float povX = actor.getPositionX() * tileWidth + tileWidth / 2;
		float povY = actor.getPositionY() * tileHeight + tileHeight / 2;
		float directionAngle = 0;
		if (actor.getDirection() == DirectionType.LEFT) {
			directionAngle = 90;
		} else if (actor.getDirection() == DirectionType.DOWN) {
			directionAngle = 180;
		} else if (actor.getDirection() == DirectionType.RIGHT) {
			directionAngle = 270;
		}
		
		return new VisibilityCalculator(sight, povX, povY, directionAngle);
	}
	
	public VisibilityCalculator(Sight sight, float centerX, float centerY, float actorDirectionAngle) {
		this.sight = sight;
		this.centerX = centerX;
		this.centerY = centerY;
		this.actorDirectionAngle = actorDirectionAngle;
	}

	public float getActorDirectionAngle() {
		return actorDirectionAngle;
	}

	public void setActorDirectionAngle(float actorDirectionAngle) {
		this.actorDirectionAngle = actorDirectionAngle;
	}
	
	public boolean visible(Piece piece, int tileWidth, int tileHeight) {
		int centerX = piece.getPositionX() * tileWidth + tileWidth / 2;
		int centerY = piece.getPositionY() * tileHeight + tileHeight / 2;
		return visible(centerX, centerY);
	}
	
	/**
	 * Returns true if the given point is in the visible area of this sight angle.
	 * @param x X coordinate of the given point.
	 * @param y Y coordinate of the given point.
	 * @return true if the given point is in the sight area, false otherwise.
	 */
	public boolean visible(float x, float y) {
		
		float distance = sight.getDistance();
		float angle = sight.getAngle();
		float directionAngle = getActorDirectionAngle() + sight.getAngleRelativeToDirection();
		
		//There was a bug for rover's sight fields. 
		//When a rover faced east, its direction angle was 270 degree.
		//The cone that pointed back hat a relative degree of 180. Adding 270 and 180 results in 450 which breaks the calculation. 
		//It should be 90 instead. We just "wound up the clock" too far and need to get rid of full circles.
		//That's why we only take the rest of 365 degrees as the final degree.
		directionAngle = directionAngle % 365;
		
		//If the distance from the point of view to the given point is greater than the sight radius, then it will never be visible.
		float distanceFromPointToCenter = distanceFromPointToCenter(x, y);
		
		if (distanceFromPointToCenter == 0) {
			return false;
		}
		
		if (distanceFromPointToCenter > distance) {
			return false;
		}
		
		/* Calculate the minimum and the maximum angles of the sight cone as related to the direction.
		 * Three cases can occur:
		 * 1) direction angle is somewhere in the middle of the range and the sight cone is small => min and max angles are both within the range (0;360]
		 * 2) direction angle is small and the sight cone is big => min angle is negative and max angle is within the range (0;360]
		 * 3) direction angle is almost 360 and the sight cone is big => min angle is within the range (0;360] and max angle is greater or equal 360
		 * In the first case we need to calculate whether the angle to the given point is greater than or equals min sight angle and is less than the max sight angle.
		 * In the second and the third case we need to check whether the point angle is in one of the two cones that have resulted from the split of the sight cone.
		 * There will never be a case when both min and max sight angles are beyond the range [0;360[ as the maximal sight radius is set to 360 
		 */
		float minAngleOfSight = directionAngle - (angle / 2);
		float maxAngleOfSight = directionAngle + (angle / 2);
		float pointAngle = pointAngle(x, y);
		
		boolean minAngleInRange = minAngleOfSight >= 0 && minAngleOfSight < 360;
		boolean maxAngleInRange = maxAngleOfSight >= 0 && maxAngleOfSight < 360;
		
		//Case 1
		if (minAngleInRange && maxAngleInRange) {
			return pointAngle >= minAngleOfSight && pointAngle <= maxAngleOfSight;
		}
		
		//Case 2
		if (!minAngleInRange) {
			
			float minAngle0 = 360 + minAngleOfSight;
			float maxAngle0 = 360;

			float minAngle1 = 0;
			float maxAngle1 = maxAngleOfSight;
			
			return (pointAngle >= minAngle0 && pointAngle <= maxAngle0) || (pointAngle >= minAngle1 && pointAngle <= maxAngle1);
			
		}
		
		//Case 3
		float minAngle0 = 0;
		float maxAngle0 = maxAngleOfSight - 360;

		float minAngle1 = minAngleOfSight;
		float maxAngle1 = 360;
		
		return (pointAngle >= minAngle0 && pointAngle <= maxAngle0) || (pointAngle >= minAngle1 && pointAngle <= maxAngle1);		
		
		
	}

	private float distanceFromPointToCenter(float x, float y) {
		
		//a² + b² = c² => c = sqrt(a² + b²)
		
		float horizontalDistance = Math.abs(x - centerX);
		float verticalDistance = Math.abs(y - centerY);
		
		float retVal = (float)Math.sqrt(horizontalDistance * horizontalDistance + verticalDistance * verticalDistance);
		
		return retVal;
		
	}

	private float pointAngle(float x, float y) {
		
		float horizontalDistance = Math.abs(x - centerX);
		float verticalDistance = Math.abs(y - centerY);
		
		if (horizontalDistance == 0 && verticalDistance == 0) {
			throw new IllegalArgumentException("The given point is equal to the point of view.");
		}
		
		if (horizontalDistance == 0) {
			return (y > centerY) ? 180 : 0;
		}
		
		if (verticalDistance == 0) {
			return (x > centerX) ? 270 : 90;
		}
		
		float tangens = horizontalDistance / verticalDistance;
		
		float angleInRad = (float)Math.atan(tangens);
		
		float angle = (float)(angleInRad * 180 / Math.PI);
		
		if (x < centerX && y > centerY) {
			angle = 180 - angle;
		} else if (x > centerX && y > centerY) {
			angle = 180 + angle;
		} else if (x > centerX && y < centerY) {
			angle = 360 - angle;
		}
		
		
		return angle;
		
	}
	
	
}
