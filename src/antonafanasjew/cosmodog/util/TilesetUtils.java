package antonafanasjew.cosmodog.util;


import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SpriteSheets;

public class TilesetUtils {

	public static final Image tileByIndex(int x, int y) {
		SpriteSheet tilesetSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_TILES1);
		
		if (y * 16 >= tilesetSpriteSheet.getHeight()) {
			y = y - (tilesetSpriteSheet.getHeight() / 16);
			tilesetSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_TILES2);
		}
		
		Image tileImage = tilesetSpriteSheet.getSprite(x, y);
		return tileImage;
	}
	
}
