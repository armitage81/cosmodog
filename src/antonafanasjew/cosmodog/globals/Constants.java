package antonafanasjew.cosmodog.globals;

public class Constants {

	public static final String PATH_TO_TILED_MAP = "data/FinalMap.tmx";
	
	public static final int INTERVAL_BETWEEN_TURNS = 50;
	public static final int INTERVAL_BETWEEN_COLLISION_NOTIFICATION = 1000;
	
	public static final int VISIBLE_MOVEMENT_DURATION_FACTOR = 75;
	public static final int MINIMAL_TIME_UNIT_IN_MINUTES = 1;
	
	public static final int DEFAULT_TIME_COSTS_WITH_VEHICLE = MINIMAL_TIME_UNIT_IN_MINUTES;
	public static final int DEFAULT_TIME_COSTS_FLYING = DEFAULT_TIME_COSTS_WITH_VEHICLE;
	public static final int DEFAULT_TIME_COSTS_ON_FOOT = DEFAULT_TIME_COSTS_WITH_VEHICLE * 3;

	public static final int MAX_ELEMENTS_IN_SCORE_LIST = 10;
	public static final float DEFAULT_CAM_ZOOM_FACTOR = 4.0f;
	
	public static final int PLAYER_ATTACK_ACTION_DURATION = 500;
	
	
	public static final int ENEMY_ATTACK_ACTION_DURATION = 500;
	
	public static final int ENEMY_DESTRUCTION_ACTION_DURATION = 500;
	
	public static final int RESOLUTION_WIDTH = 1024;
	public static final int RESOLUTION_HEIGHT = 768;
	public static final boolean FULLSCREEN = false;
	
	public static final int CAM_PADDING = 256;

	/**
	 * Dynamic dialogs use this as basis for the text speed.
	 */
	public static final int DEFAULT_INTERVAL_BETWEEN_DIALOG_LETTERS = 64;

	
}
