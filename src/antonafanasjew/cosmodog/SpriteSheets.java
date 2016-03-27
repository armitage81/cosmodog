package antonafanasjew.cosmodog;

import java.util.HashMap;

import org.newdawn.slick.SpriteSheet;

/**
 * Map structure containing the sprite sheets by their ID's
 */
public class SpriteSheets extends HashMap<String, SpriteSheet> {

	private static final long serialVersionUID = 7451716816594141392L;
	
	/**
	 * Alphabeth tile width for monospace alphabeths.
	 */
	public static final int ALPHABETH_SPRITESHEET_TILES_WIDTH = 40;
	
	/**
	 * Sprite sheet ID for: player.
	 */
	public static final String SPRITESHEET_PLAYER = "spritesheet.player";
	
	/**
	 * Sprite sheet ID for: infobits.
	 */
	public static final String SPRITESHEET_INFOBITS = "spritesheet.infobits";
	
	/**
	 * Sprite sheet ID for: insight collectible.
	 */
	public static final String SPRITESHEET_INSIGHT = "spritesheet.insight";
	
	/**
	 * Sprite sheet ID for: supplies.
	 */
	public static final String SPRITESHEET_SUPPLIES = "spritesheet.supplies";
	
	/**
	 * Sprite sheet ID for: clouds.
	 */
	public static final String SPRITESHEET_CLOUDS = "spritesheet.clouds";
	
	/**
	 * Sprite sheet ID for: a box with the tool collectible.
	 */
	public static final String SPRITESHEET_COLLECTIBLE_ITEM_TOOL = "spritesheet.collectible_tool";
	
	/**
	 * Sprite sheet ID for: alphabeth.
	 */
	public static final String SPRITESHEET_ALPHABETH = "spritesheet.alphabeth";
	
	/**
	 * Sprite sheet ID for: system alphabeth
	 */
	public static final String SPRITESHEET_ALPHABETH2 = "spritesheet.alphabeth2";
	
	/**
	 * Sprite sheet ID for: interface.
	 */
	public static final String SPRITESHEET_INTERFACE = "spritesheet.interface";

	/**
	 * Sprite sheet ID for: tile set.
	 */
	public static final String SPRITESHEET_TILES = "spritesheet.tiles";

}
