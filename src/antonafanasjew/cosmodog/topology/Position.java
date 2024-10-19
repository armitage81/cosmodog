package antonafanasjew.cosmodog.topology;

import java.io.Serializable;
import java.util.Objects;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;

/**
 * Position of a point
 * Represents a point in 2D space and the name of the map it is on.
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 703260030937304215L;

	private float x;
	private float y;
	private MapType mapType;

	public static Position fromCoordinates(float x, float y) {
		return fromCoordinates(x, y, MapType.MAIN);
	}

	public static Position fromCoordinates(float x, float y, MapType mapType) {
		Position position = new Position();
		position.x = x;
		position.y = y;
		position.mapType = mapType;
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

	public MapType getMapType() {
		return mapType;
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

	public Position copy() {
        return Position.fromCoordinates(this.x, this.y, this.mapType);
	}

	public Position shifted(float offsetX, float offsetY) {
		Position shiftedPosition = Position.fromCoordinates(this.x, this.y, this.mapType);
		shiftedPosition.shift(offsetX, offsetY);
		return shiftedPosition;
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

	public boolean inMapBounds(CosmodogMap map) {
		return x >= 0 && y >= 0 && x < map.getWidth() && y < map.getHeight();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		return Float.compare(x, position.x) == 0 && Float.compare(y, position.y) == 0 && mapType == position.mapType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, mapType);
	}

	@Override
	public String toString() {
		return x + "/" + y;
	}
	
}
