package antonafanasjew.cosmodog.topology;

import java.io.Serializable;

/**
 * 2D Position of a point
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 703260030937304215L;

	private float x;
	private float y;
	
	public static Position fromCoordinates(float x, float y) {
		Position position = new Position();
		position.x = x;
		position.y = y;
		return position;
	}
	
	private Position() {

	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	/**
	 * Moves the position at given offsets.
	 * Offsets can be negative (moving left and up).
	 * THIS IS NOT POSITIONING, RESULT IS RELATIVE TO CURRENT PLACEMENT!!!
	 */
	public void shift(float offsetX, float offsetY) {
		this.x += offsetX;
		this.y += offsetY;
	}
	
	/**
	 * Moves the position to given location.
	 * RESULTING POSITION IS ABSOLUTE!!!
	 */
	public void move(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float distance(float x, float y) {
		float deltaX = this.getX() - x;
		deltaX = deltaX < 0 ? (deltaX * -1) : deltaX;
		float deltaY = this.getY() - y;
		deltaY = deltaY < 0 ? (deltaY * -1) : deltaY;
		
		return deltaX + deltaY;
	}
	
	public float distance(Position pos) {
		return distance(pos.getX(), pos.getY());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return x + "/" + y;
	}
	
}
