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
	public static final int VISIBLE_MOVEMENT_DURATION_FACTOR = 50;
	
	/**
	 * Minimal unit on the planetary calendar in minutes.
	 */
	public static final int MINIMAL_TIME_UNIT_IN_MINUTES = 1;
	
	/**
	 * Time costs that a vehicle has to pay to move from one default tile to another. 
	 */
	public static final int DEFAULT_TIME_COSTS_WITH_VEHICLE = MINIMAL_TIME_UNIT_IN_MINUTES;
	
	/**
	 * Time costs that the platform has to pay to move on rails.
	 */
	public static final int DEFAULT_TIME_COSTS_WITH_PLATFORM = DEFAULT_TIME_COSTS_WITH_VEHICLE;
	
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
	 * The visible duration of the ranged enemy attack fight action phase transition.
	 */
	public static final int RANGED_ENEMY_ATTACK_ACTION_DURATION = 2000;
	
	/**
	 * The visible duration of the enemy destruction fight action phase transition.
	 */
	public static final int ENEMY_DESTRUCTION_ACTION_DURATION = 750;
	
	/**
	 * The horizontal resolution of the game window.
	 */
	public static final int RESOLUTION_WIDTH = 1280;
	
	/**
	 * The vertical resolution of the game window.
	 */
	public static final int RESOLUTION_HEIGHT = 720;
	
//	/**
//	 * The horizontal resolution of the game window.
//	 */
//	public static final int RESOLUTION_WIDTH = 1280;
//	
//	/**
//	 * The vertical resolution of the game window.
//	 */
//	public static final int RESOLUTION_HEIGHT = 720;
	
	/**
	 * Full screen flag.
	 */
	public static final boolean FULLSCREEN = true;
	
	/**
	 * Dynamic dialogs use this as basis for the text speed.
	 */
	public static final int DEFAULT_INTERVAL_BETWEEN_DIALOG_LETTERS = 64;

	/**
	 * Only enemies that are that close to the player will move and be active.
	 */
	public static final int ENEMY_ACTIVATION_DISTANCE = 32;
	
	/**
	 * The main objective of the game is collecting the insight objects to open the entrance into the alien base.
	 * There are 32 insight objects. This constant defines, how many are enough to open the base.
	 */
	public static final int MIN_INSIGHTS_TO_OPEN_ALIEN_BASE = 20;

	public static final int NUMBER_OF_SOFTWARE_PIECES_IN_GAME = 16;

}
