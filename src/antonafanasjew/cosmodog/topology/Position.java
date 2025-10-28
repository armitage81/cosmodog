package antonafanasjew.cosmodog.topology;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Position of a point
 * Represents a point in 2D space and the name of the map it is on.
 */
public class Position implements Serializable {

	@Serial
	private static final long serialVersionUID = 703260030937304215L;

	private float x;
	private float y;
	private MapDescriptor mapDescriptor;

	public static Position fromCoordinatesOnPlayerLocationMap(float x, float y) {
		MapDescriptor playersMapDescriptor = ApplicationContextUtils.getPlayer().getPosition().mapDescriptor;
		return fromCoordinates(x, y, playersMapDescriptor);
	}

	public static Position fromCoordinates(float x, float y, MapDescriptor mapDescriptor) {
		Position position = new Position();
		position.x = x;
		position.y = y;
		position.mapDescriptor = mapDescriptor;
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

	public void switchPlane(MapDescriptor mapType) {
		this.mapDescriptor = mapType;
	}

	public Position copy() {
        return Position.fromCoordinates(this.x, this.y, this.mapDescriptor);
	}

	public Position shifted(float offsetX, float offsetY) {
		Position shiftedPosition = Position.fromCoordinates(this.x, this.y, this.mapDescriptor);
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

	/*
	Useful to create a pixel position from a tile position.
	 */
	public Position scale(float factor) {
		return Position.fromCoordinates(this.x * factor, this.y * factor, this.mapDescriptor);
	}

	public Position translate(float x, float y) {
		return Position.fromCoordinates(this.x + x, this.y + y, this.mapDescriptor);
	}

	public float distance(Position pos) {
		return distance(pos.getX(), pos.getY());
	}

	public boolean inMapBounds(CosmodogMap map) {
		return x >= 0 && y >= 0 && x < map.getMapDescriptor().getWidth() && y < map.getMapDescriptor().getHeight();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Position position = (Position) o;
		boolean xEqual = Float.compare(x, position.x) == 0;
		boolean yEqual = Float.compare(y, position.y) == 0;
		boolean mapDescriptorEqual = Objects.equals(mapDescriptor, position.mapDescriptor);
		return xEqual && yEqual && mapDescriptorEqual;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, mapDescriptor);
	}

	@Override
	public String toString() {
		return x + "/" + y;
	}

	public Position nextPosition(DirectionType directionType) {
		Position position = this.copy();

		if (directionType == DirectionType.DOWN) {
			position.shift(0, 1);
		} else if (directionType == DirectionType.UP) {
			position.shift(0, -1);
		} else if (directionType == DirectionType.LEFT) {
			position.shift(-1, 0);
		} else if (directionType == DirectionType.RIGHT) {
			position.shift(1, 0);
		}

		return position;
	}

	public MapDescriptor getMapDescriptor() {
		return mapDescriptor;
	}
}
