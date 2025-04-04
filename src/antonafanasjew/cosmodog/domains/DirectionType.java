package antonafanasjew.cosmodog.domains;

import antonafanasjew.cosmodog.topology.Position;

import java.util.Random;

/**
 * Domain for directions.
 */
public enum DirectionType {

	/**
	 * Up/North.
	 */
	UP("North"),
	
	/**
	 * Right/East.
	 */
	RIGHT("East"),
	
	/**
	 * Down/South.
	 */
	DOWN("South"),
	
	/**
	 * Left/West.
	 */
	LEFT("West");
	
	private String representation;
	
	
	private DirectionType(String representation) {
		this.representation = representation;
	}
	
	public String getRepresentation() {
		return representation;
	}
	
	public static DirectionType random() {
		Random r = new Random();
		int diceThrow = r.nextInt();
		if (diceThrow < 0) {
			diceThrow = - diceThrow;
		}
		diceThrow %= values().length;
		
		return values()[diceThrow];
	}

	public static DirectionType direction(Position sourcePosition, Position targetPosition) {

		if (sourcePosition.equals(targetPosition)) {
			throw new RuntimeException("Positions must not equal.");
		}

		float x1 = sourcePosition.getX();
		float x2 = targetPosition.getX();

		float y1 = sourcePosition.getY();
		float y2 = targetPosition.getY();

		float distanceX = x1 - x2;
		float distanceY = y1 - y2;

		if (Math.abs(distanceX) > 1) {
			throw new RuntimeException("Positions must be adjacent");
		}
		if (Math.abs(distanceY) > 1) {
			throw new RuntimeException("Positions must be adjacent");
		}
		if (Math.abs(distanceX) == 1 && Math.abs(distanceY) == 1) {
			throw new RuntimeException("Positions must be adjacent");
		}

		if (distanceX != 0) {
			return distanceX < 0 ? DirectionType.RIGHT : DirectionType.LEFT;
		} else {
			return distanceY < 0 ? DirectionType.DOWN : DirectionType.UP;
		}

	}

	public static DirectionType reverse(DirectionType directionType) {
		if (directionType == DirectionType.LEFT) {
			return DirectionType.RIGHT;
		} else if (directionType == DirectionType.RIGHT) {
			return DirectionType.LEFT;
		} else if (directionType == DirectionType.UP) {
			return DirectionType.DOWN;
		} else if (directionType == DirectionType.DOWN) {
			return DirectionType.UP;
		}
		throw new RuntimeException();
	}

	public static Position facedAdjacentPosition(Position position, DirectionType direction) {

		Position facedAdjacentPosition = position;

		if (direction == DirectionType.RIGHT) {
			facedAdjacentPosition = position.shifted(1, 0);
		} else if (direction == DirectionType.LEFT) {
			facedAdjacentPosition = position.shifted(-1, 0);
		} else if (direction == DirectionType.UP) {
			facedAdjacentPosition = position.shifted(0, -1);
		} else {
			facedAdjacentPosition = position.shifted(0, 1);
		}

		return facedAdjacentPosition;
	}
}
