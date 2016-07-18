package antonafanasjew.cosmodog.domains;

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
}
