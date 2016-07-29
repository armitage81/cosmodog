package antonafanasjew.cosmodog.domains;

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
}
