package antonafanasjew.cosmodog.globals;

public class Constants {

	/**
	 * Relative path to the tiled map document.
	 */
	public static final String PATH_TO_TILED_MAP = "data/FinalMap.tmx";
	
	/**
	 * Milliseconds to wait between turns.
	 */
	public static final int INTERVAL_BETWEEN_TURNS = 50;
	
	/**
	 * Milliseconds between another collision notification.
	 */
	public static final int INTERVAL_BETWEEN_COLLISION_NOTIFICATION = 500;
	
	/**
	 * The factor by which the length of the movement action will be defined.
	 * It will be multiplied with the number of planetary minutes needed for the action
	 * to calculate the action movement.
	 */
	public static final int VISIBLE_MOVEMENT_DURATION_FACTOR = 75;
	
	/**
	 * Minimal unit on the planetary calendar in minutes.
	 */
	public static final int MINIMAL_TIME_UNIT_IN_MINUTES = 1;
	
	/**
	 * Time costs that a vehicle has to pay to move from one default tile to another. 
	 */
	public static final int DEFAULT_TIME_COSTS_WITH_VEHICLE = MINIMAL_TIME_UNIT_IN_MINUTES;
	
	/**
	 * Time costs that a flying device has to pay to move from one default tile to another. 
	 */
	public static final int DEFAULT_TIME_COSTS_FLYING = DEFAULT_TIME_COSTS_WITH_VEHICLE;
	
	/**
	 * Time costs that a unit on foot has to pay to move from one default tile to another. 
	 */
	public static final int DEFAULT_TIME_COSTS_ON_FOOT = DEFAULT_TIME_COSTS_WITH_VEHICLE * 3;

	/**
	 * Number of entries in the score list.
	 */
	public static final int MAX_ELEMENTS_IN_SCORE_LIST = 10;
	
	/**
	 * The initial zoom factor on the camera.
	 */
	public static final float DEFAULT_CAM_ZOOM_FACTOR = 4.0f;
	
	/**
	 * The visible duration of the player attack fight action phase transition.
	 */
	public static final int PLAYER_ATTACK_ACTION_DURATION = 500;
	
	/**
	 * The visible duration of the enemy attack fight action phase transition.
	 */
	public static final int ENEMY_ATTACK_ACTION_DURATION = 500;
	
	/**
	 * The visible duration of the enemy destruction fight action phase transition.
	 */
	public static final int ENEMY_DESTRUCTION_ACTION_DURATION = 500;
	
	/**
	 * The horizontal resolution of the game window.
	 */
	public static final int RESOLUTION_WIDTH = 1024;
	
	/**
	 * The vertical resolution of the game window.
	 */
	public static final int RESOLUTION_HEIGHT = 576;
	
	/**
	 * Full screen flag.
	 */
	public static final boolean FULLSCREEN = false;
	
	/**
	 * Dynamic dialogs use this as basis for the text speed.
	 */
	public static final int DEFAULT_INTERVAL_BETWEEN_DIALOG_LETTERS = 64;

}
