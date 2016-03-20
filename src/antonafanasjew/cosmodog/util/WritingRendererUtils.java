package antonafanasjew.cosmodog.util;

import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.writing.model.TextBlock;

import com.google.common.collect.Maps;

public class WritingRendererUtils {

	public static final String SPEAKER_NARRATOR = "narrator";
	public static final String SPEAKER_CARL = "carl";
	public static final String SPEAKER_ALISA = "alisa";
	public static final String SPEAKER_SYSTEM = "system";
	public static final String SPEAKER_MICK = "mick";
	
	private static Map<String, Integer> ALPHABETH_INDEX_BY_SPEAKER = Maps.newHashMap();
	
	static {
		ALPHABETH_INDEX_BY_SPEAKER.put(SPEAKER_NARRATOR, 0);
		ALPHABETH_INDEX_BY_SPEAKER.put(SPEAKER_CARL, 1);
		ALPHABETH_INDEX_BY_SPEAKER.put(SPEAKER_ALISA, 2);
		ALPHABETH_INDEX_BY_SPEAKER.put(SPEAKER_SYSTEM, 3);
		ALPHABETH_INDEX_BY_SPEAKER.put(SPEAKER_MICK, 4);
	}
	
	public static Image letterImageForCharacterAndTextBlock(char character, TextBlock textBlock) {
		
		int alphabethIndex = 0;
		if (textBlock != null && textBlock.getSpeaker() != null && ALPHABETH_INDEX_BY_SPEAKER.containsKey(textBlock.getSpeaker())) {
			alphabethIndex = ALPHABETH_INDEX_BY_SPEAKER.get(textBlock.getSpeaker());
		}
		
		int imageX = character % SpriteSheets.ALPHABETH_SPRITESHEET_TILES_WIDTH;
		int imageY = character / SpriteSheets.ALPHABETH_SPRITESHEET_TILES_WIDTH + 4 * alphabethIndex;
		
		
		
		SpriteSheet alphabethSpriteSheet = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_ALPHABETH);
		Image characterImage = alphabethSpriteSheet.getSprite(imageX, imageY);
		
		return characterImage;
	}
	
}
