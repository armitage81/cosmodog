package antonafanasjew.cosmodog;

import java.util.HashMap;

import org.newdawn.slick.SpriteSheet;

/**
 * Map structure containing the sprite sheets by their ID's
 */
public class SpriteSheets extends HashMap<String, SpriteSheet> {

	private static final long serialVersionUID = 7451716816594141392L;
	
	/**
	 * Sprite sheet ID for: clouds.
	 */
	public static final String SPRITESHEET_CLOUDS = "spritesheet.clouds";
	
	/**
	 * Sprite sheet ID for: tile set.
	 */
	public static final String SPRITESHEET_TILES1 = "spritesheet.tiles1";
	public static final String SPRITESHEET_TILES2 = "spritesheet.tiles2";
	
	public static final String SPRITESHEET_SYMBOLS = "spritesheet.symbols";

}
