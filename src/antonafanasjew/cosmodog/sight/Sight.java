package antonafanasjew.cosmodog.sight;

import java.io.Serializable;

/**
 * Defines a sight "cone" by providing the sight angle, the sight direction as related to the actors direction (both in degree)
 * and the distance.
 *
 */
public class Sight implements Serializable {

	private static final long serialVersionUID = -2174059709810754936L;

	private float distance;
	
	private float angle;

	private float angleRelativeToDirection;
	
	public Sight(float distance, float angle, float angleRelativeToDirection) {
		this.distance = distance;
		this.angle = angle;
		this.setAngleRelativeToDirection(angleRelativeToDirection);
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAngleRelativeToDirection() {
		return angleRelativeToDirection;
	}

	public void setAngleRelativeToDirection(float angleRelativeToDirection) {
		this.angleRelativeToDirection = angleRelativeToDirection;
	}

	public Sight copyWithOtherDistance(float distance) {
		return new Sight(distance, this.angle, this.angleRelativeToDirection);
	}
	
	
	
	
	
	
}
