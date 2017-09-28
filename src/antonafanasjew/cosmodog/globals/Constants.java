package antonafanasjew.cosmodog.globals;

public class Constants {

	public static String[] PREFERRED_RESOLUTIONS = new String[] {
		"1280x720",
		"1920x1080",
		"1152x648",
		"1280x1024",
		"1024x800",
		"1366x768",
		"1024x576",
	};
	
	public static final float GAME_LOG_FRAME_WIDTH = 864;
	public static final float GAME_LOG_FRAME_HEIGHT = 572;
	
	public static final float LOG_PLAYER_TEXT_WIDTH = 400;
	public static final float LOG_PLAYER_TEXT_HEIGHT = 200;
	
	public static final float GAME_LOG_TEXT_WIDTH = 800;
	public static final float GAME_LOG_TEXT_HEIGHT = 440;
	
	public static final int FLICKING_RATE_IN_MILLIS = 500;
	public static final float FLICKING_THRESHOLD = 0.2f;
	
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
	//public static final int VISIBLE_MOVEMENT_DURATION_FACTOR = 50;
	public static final int VISIBLE_MOVEMENT_DURATION_FACTOR = 50;
	
	/**
	 * That many minutes will pass in the planetary calendar with each turn. 
	 */
	public static final int MINUTES_PER_TURN = 3;

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
	
	public static final int REFERENCE_RESOLUTION_WIDTH = 1280;
	
	public static final int REFERENCE_RESOLUTION_HEIGHT = 720;
	
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
	
	public static final int NUMBER_OF_ARMOR_PIECES_IN_GAME = 9;
	
	public static final int NUMBER_OF_SOULESSENSE_PIECES_IN_GAME = 18;
	
	public static final int NUMBER_OF_INFOBITS_IN_GAME = 6000;
	
	public static final int NUMBER_OF_SECRETS_IN_GAME = 120;
	
	public static final int NUMBER_OF_INSIGHTS_IN_GAME = 32;
	
	public static final int NUMBER_OF_CHARTS_IN_GAME = 64;

}
