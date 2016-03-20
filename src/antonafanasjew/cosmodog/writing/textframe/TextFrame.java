package antonafanasjew.cosmodog.writing.textframe;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.text.Letter;
import antonafanasjew.cosmodog.text.LetterUtils;
import antonafanasjew.cosmodog.topology.Rectangle;

import com.google.common.collect.Lists;

public class TextFrame {

	public static final float LETTER_VERTICAL_PADDING = 0;
	public static final float LETTER_HORIZONTAL_PADDING = 0;
	
	private DrawingContext cachedDrawingContext = null;
	
	private String text;
	
	private List<Letter> letters = Lists.newArrayList();
	private List<DrawingContext> letterDrawingContexts = Lists.newArrayList(); 
	
	public TextFrame(String text) {
		this.text = text;
	}

	private void initDrawingContexts(DrawingContext parentDrawingContext) {
		
		Map<Character, Letter> characterLetters = ApplicationContext.instance().getCharacterLetters();
		Letter defaultLetter = characterLetters.get('?');
		
		String[] textLines = text.split("<br>");
		
		List<List<Letter>> letters = Lists.newArrayList();
		
		Rectangle[] letterLineRectangles = new Rectangle[textLines.length];
		
		float allLettersWidth = 0;
		float allLettersHeight = Letter.LETTER_HEIGHT * textLines.length + (LETTER_VERTICAL_PADDING * (textLines.length - 1));
		
		for (int i = 0; i < textLines.length; i++) {
			String textLine = textLines[i];
			List<Letter> lettersForTextLine = LetterUtils.lettersForText(textLine, characterLetters, defaultLetter);
			letters.add(lettersForTextLine);
			this.letters.addAll(lettersForTextLine);
			Rectangle letterLineBounds = LetterUtils.letterListBounds(lettersForTextLine, LETTER_HORIZONTAL_PADDING);
			letterLineRectangles[i] = letterLineBounds;
			
			if (letterLineBounds.getWidth() > allLettersWidth) {
				allLettersWidth = letterLineBounds.getWidth();
			}
			
		}

		DrawingContext completeTextDrawingContext = new CenteredDrawingContext(parentDrawingContext, allLettersWidth, allLettersHeight);
		
		for (int i = 0; i < letters.size(); i++) {
			DrawingContext textLineDrawingContext = new TileDrawingContext(completeTextDrawingContext, 1, textLines.length, 0, i);
			DrawingContext centeredLetterLineDrawingContext = new CenteredDrawingContext(textLineDrawingContext, letterLineRectangles[i].getWidth(), textLineDrawingContext.h());
			List<DrawingContext> lettersDrawingContexts = LetterUtils.letterLineDrawingContexts(letters.get(i), LETTER_VERTICAL_PADDING, centeredLetterLineDrawingContext);
			this.letterDrawingContexts.addAll(lettersDrawingContexts);
		}
		
	}
	
	public List<DrawingContext> getLetterDrawingContexts(DrawingContext parentDrawingContext) {
		
		if (cachedDrawingContext == null || equalDrawingContexts(cachedDrawingContext, parentDrawingContext) == false) {
			initDrawingContexts(parentDrawingContext);
		}
		
		cachedDrawingContext = parentDrawingContext;
		
		return letterDrawingContexts;
	}

	public List<Letter> getLetters(DrawingContext parentDrawingContext) {
		
		if (cachedDrawingContext == null || equalDrawingContexts(cachedDrawingContext, parentDrawingContext) == false) {
			initDrawingContexts(parentDrawingContext);
		}

		cachedDrawingContext = parentDrawingContext;
		
		return letters;
	}
	
	private boolean equalDrawingContexts(DrawingContext dc1, DrawingContext dc2) {
		return dc1.x() == dc2.x() && dc1.y() == dc2.y() && dc1.w() == dc2.w() && dc1.h() == dc2.h();
	}

}













