package antonafanasjew.cosmodog.text;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.topology.Rectangle;

/**
 * Contains methods to calculate letter positioning.
 * 
 */
public class LetterUtils {

	/**
	 * Returns the letter related to the given character.
	 * Uses the letter map to assign a letter to the character.
	 * 
	 * @param c - Character for which the letter should be returned.
	 * @param letters - Map of the letters by character keys to find the letter assigned to the character.
	 * @param defaultLetter - The letter that will be used if the character key is not found in the letter map.
	 * @return Letter related to the character or the default letter if not found.
	 */
	public static final Letter letterForCharacter(char c, Map<Character, Letter> letters, Letter defaultLetter) {
		Letter letter = letters.get(c);
		
		if (letter == null) {
			letter = defaultLetter;
		}
		
		return letter;
	}
	
	/**
	 * Returns the letter list for the given text. Take note: All non-printable characters, like \n will be translated to the default letter
	 * so try to avoid them. Use this method for one line texts.
	 * 
	 * @param text - Text for which the letter list should be returned. 
	 * @param letters - Map of the letters by character keys to find the letter assigned to the character.
	 * @param defaultLetter - The letter that will be used if the character key is not found in the letter map.
	 * @return List of letters related to the text (with default letters for not found letters).
	 */
	public static final List<Letter> lettersForText(String text, Map<Character, Letter> letters, Letter defaultLetter) {
		List<Letter> retVal = Lists.newArrayList();
		
		char[] chars = text.toCharArray();
		
		for (char c : chars) {
			Letter l = letterForCharacter(c, letters, defaultLetter);
			retVal.add(l);
		}
		
		return retVal;
	}
	
	/**
	 * Returns the bounds of a letter.
	 * 
	 * @param letter - The letter whose bounds should be returned.
	 * @return A Rectangle containing the bounds of the letter.
	 */
	public static final Rectangle letterBounds(Letter letter) {
		return Rectangle.fromSize(letter.getW(), Letter.LETTER_HEIGHT);
	}


	/**
	 * Returns the bounds of a letter list. It will always represent only one line of letters.
	 * 
	 * @param letters - Letter list for which the bounds should be calculated.
	 * @param letterPadding - (Horizontal) padding between the letters.
	 * @return Rectangle representing the bounds of the letter list.
	 */
	public static final Rectangle letterListBounds(List<Letter> letters, float letterPadding) {
		float aggregatedPadding = letterPadding * (letters.size() - 1);
		float aggregatedLetterWidth = 0;
		float maximalLetterHeight = 0;
		for (Letter letter : letters) {
			aggregatedLetterWidth += letter.getW();
			float letterHeight = Letter.LETTER_HEIGHT;
			if (letterHeight > maximalLetterHeight) {
				maximalLetterHeight = letterHeight;
			}
		}
		
		float aggregatedWidth = aggregatedLetterWidth + aggregatedPadding;
		
		return Rectangle.fromSize(aggregatedWidth, maximalLetterHeight);
		
	}
	
	/**
	 * Returns the list of drawing contexts for all letters in the list as related to the parent drawing context (which can be null).
	 * Assumed, the parent drawing context is null or has the coordinates 0/0, the first letter will have the context (0, 0, letter0.width, letter0.height), 
	 * second letter will have the context (letter0.width + padding, 0, letter1.width, letter1.height) etc.
	 *  
	 * @param letters - List of letters for which the contexts should be calculated.
	 * @param letterPadding - The interval between letters.
	 * @param parentDrawingContext - The drawing context which will be used as offset for all resulting drawing contexts.
	 * @return List of drawing contexts for each letter in the letter list.
	 */
	public static final List<DrawingContext> letterLineDrawingContexts(List<Letter> letters, float letterPadding, DrawingContext parentDrawingContext) {
		return letterLineDrawingContexts(letters, letterPadding, 1.0f, parentDrawingContext);
	}
	
	public static final List<DrawingContext> letterLineDrawingContexts(List<Letter> letters, float letterPadding, float letterScaleFactor, DrawingContext parentDrawingContext) {
		
		List<DrawingContext> retVal = Lists.newArrayList();
		
		float accumulatedOffsetX = 0;
		
		for (int i = 0; i < letters.size(); i++) {
			Letter letter = letters.get(i);
			
			float dcX = accumulatedOffsetX;
			float dcY = 0;
			float dcW = letter.getW() * letterScaleFactor;
			float dcH = Letter.LETTER_HEIGHT * letterScaleFactor;
			
			DrawingContext dc = new SimpleDrawingContext(parentDrawingContext, dcX, dcY, dcW, dcH);
			retVal.add(dc);
			
			accumulatedOffsetX += (letter.getW() * letterScaleFactor + letterPadding);
			
		}
		
		return retVal;
	}
	
	public static final List<String> splitTextForTextBoxWidth(String text, float scaleFactor, float textBoxWidth) {
		
		Map<Character, Letter> characterLetters = ApplicationContext.instance().getCharacterLetters();
		Letter defaultLetter = characterLetters.get('?');
		float spaceWidth = letterBounds(characterLetters.get(' ')).getWidth();
		
		String[] wordsArray = text.split(" ");
		List<String> words = Lists.newArrayList(wordsArray);
		
		
		List<Float> wordWidths = Lists.newArrayList();
		
		for (String word : words) {
			List<Letter> wordLetters = lettersForText(word, characterLetters, defaultLetter);
			Rectangle wordBounds = letterListBounds(wordLetters, 0);
			float unscaledWidth = wordBounds.getWidth() + spaceWidth;
			float realWidth = unscaledWidth * scaleFactor;
			wordWidths.add(realWidth);
		}

		List<String> retVal = Lists.newArrayList();
		
		StringBuffer textLine = new StringBuffer();
		float accumulatedLineWidth = 0f;
		
		for (int i = 0; i < wordWidths.size(); i++) {
			float wordWidth = wordWidths.get(i);
			if (accumulatedLineWidth + wordWidth <= textBoxWidth) {
				textLine.append(words.get(i)).append(" ");
				accumulatedLineWidth += wordWidth;
			} else {
				retVal.add(textLine.toString());
				textLine = new StringBuffer(words.get(i)).append(" ");
				accumulatedLineWidth = wordWidth;
			}
		}
		
		retVal.add(textLine.toString());
		
		return retVal;
		
	}
	
}








