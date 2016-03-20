package antonafanasjew.cosmodog.text;

import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import com.google.common.collect.Maps;

public class DefaultLetterBuilder implements LetterBuilder {

	private SpriteSheet spriteSheet;
	
	private static final float SPRITE_SHEET_LETTER_PADDING = 3;
	private static final Map<Character, Float> LETTER_WIDTHS = Maps.newHashMap();

	
	static {
		
		char index = 32;
		
		LETTER_WIDTHS.put(index++, 1.0f); //SPACE
		LETTER_WIDTHS.put(index++, 1.0f); //EXCLAMATION_MARK
		LETTER_WIDTHS.put(index++, 5.0f); //QUOTATION_MARK
		LETTER_WIDTHS.put(index++, 8.0f); //HASH
		LETTER_WIDTHS.put(index++, 5.0f); //DOLLAR
		LETTER_WIDTHS.put(index++, 7.0f); //PERCENT
		LETTER_WIDTHS.put(index++, 8.0f); //AMPERSAND
		LETTER_WIDTHS.put(index++, 2.0f); //QUOTE
		LETTER_WIDTHS.put(index++, 4.0f); //LEFT_BRACKET
		LETTER_WIDTHS.put(index++, 4.0f); //RIGHT_BRACKET
		LETTER_WIDTHS.put(index++, 7.0f); //MULTIPLICATION
		LETTER_WIDTHS.put(index++, 7.0f); //PLUS
		LETTER_WIDTHS.put(index++, 3.0f); //COMMA
		LETTER_WIDTHS.put(index++, 7.0f); //MINUS
		LETTER_WIDTHS.put(index++, 1.0f); //FULL_STOP
		LETTER_WIDTHS.put(index++, 7.0f); //DIVISION
		
		
		LETTER_WIDTHS.put(index++, 7.0f); //0
		LETTER_WIDTHS.put(index++, 4.0f); //1
		LETTER_WIDTHS.put(index++, 6.0f); //2
		LETTER_WIDTHS.put(index++, 6.0f); //3
		LETTER_WIDTHS.put(index++, 7.0f); //4
		LETTER_WIDTHS.put(index++, 6.0f); //5
		LETTER_WIDTHS.put(index++, 7.0f); //6
		LETTER_WIDTHS.put(index++, 6.0f); //7
		LETTER_WIDTHS.put(index++, 7.0f); //8
		LETTER_WIDTHS.put(index++, 7.0f); //9
		
		
		LETTER_WIDTHS.put(index++, 1.0f); //COLON
		LETTER_WIDTHS.put(index++, 3.0f); //SEMICOLON
		LETTER_WIDTHS.put(index++, 4.0f); //LESS_THAN
		LETTER_WIDTHS.put(index++, 6.0f); //EQUALS
		LETTER_WIDTHS.put(index++, 4.0f); //GREATER_THAN
		LETTER_WIDTHS.put(index++, 7.0f); //QUESTION_MARK
		LETTER_WIDTHS.put(index++, 10.0f); //AT
		
		LETTER_WIDTHS.put(index++, 13.0f); //A
		LETTER_WIDTHS.put(index++, 8.0f); //B
		LETTER_WIDTHS.put(index++, 8.0f); //C
		LETTER_WIDTHS.put(index++, 9.0f); //D
		LETTER_WIDTHS.put(index++, 8.0f); //E
		LETTER_WIDTHS.put(index++, 8.0f); //F
		LETTER_WIDTHS.put(index++, 8.0f); //G
		LETTER_WIDTHS.put(index++, 9.0f); //H
		LETTER_WIDTHS.put(index++, 3.0f); //I
		LETTER_WIDTHS.put(index++, 3.0f); //J
		LETTER_WIDTHS.put(index++, 8.0f); //K
		LETTER_WIDTHS.put(index++, 8.0f); //L
		LETTER_WIDTHS.put(index++, 11.0f); //M
		LETTER_WIDTHS.put(index++, 10.0f); //N
		LETTER_WIDTHS.put(index++, 11.0f); //O
		LETTER_WIDTHS.put(index++, 8.0f); //P
		LETTER_WIDTHS.put(index++, 11.0f); //Q
		LETTER_WIDTHS.put(index++, 8.0f); //R
		LETTER_WIDTHS.put(index++, 8.0f); //S
		LETTER_WIDTHS.put(index++, 9.0f); //T
		LETTER_WIDTHS.put(index++, 11.0f); //U
		LETTER_WIDTHS.put(index++, 13.0f); //V
		LETTER_WIDTHS.put(index++, 19.0f); //W
		LETTER_WIDTHS.put(index++, 11.0f); //X
		LETTER_WIDTHS.put(index++, 11.0f); //Y
		LETTER_WIDTHS.put(index++, 10.0f); //Z
		
		LETTER_WIDTHS.put(index++, 3.0f); //LEFT_RECT_BRACKET
		LETTER_WIDTHS.put(index++, 7.0f); //BACK_SLASH
		LETTER_WIDTHS.put(index++, 3.0f); //RIGHT_RECT_BRACKET
		LETTER_WIDTHS.put(index++, 5.0f); //LINE_BEGIN (^)
		LETTER_WIDTHS.put(index++, 6.0f); //UNDEERSCORE
		LETTER_WIDTHS.put(index++, 2.0f); //ACCENT

		
		LETTER_WIDTHS.put(index++, 5.0f); //a
		LETTER_WIDTHS.put(index++, 5.0f); //b
		LETTER_WIDTHS.put(index++, 5.0f); //c
		LETTER_WIDTHS.put(index++, 5.0f); //d
		LETTER_WIDTHS.put(index++, 5.0f); //e
		LETTER_WIDTHS.put(index++, 3.0f); //f
		LETTER_WIDTHS.put(index++, 5.0f); //g
		LETTER_WIDTHS.put(index++, 5.0f); //h
		LETTER_WIDTHS.put(index++, 1.0f); //i
		LETTER_WIDTHS.put(index++, 3.0f); //j
		LETTER_WIDTHS.put(index++, 4.0f); //k
		LETTER_WIDTHS.put(index++, 1.0f); //l
		LETTER_WIDTHS.put(index++, 7.0f); //m
		LETTER_WIDTHS.put(index++, 5.0f); //n
		LETTER_WIDTHS.put(index++, 5.0f); //o
		LETTER_WIDTHS.put(index++, 5.0f); //p
		LETTER_WIDTHS.put(index++, 5.0f); //q
		LETTER_WIDTHS.put(index++, 4.0f); //r
		LETTER_WIDTHS.put(index++, 5.0f); //s
		LETTER_WIDTHS.put(index++, 3.0f); //t
		LETTER_WIDTHS.put(index++, 5.0f); //u
		LETTER_WIDTHS.put(index++, 5.0f); //v
		LETTER_WIDTHS.put(index++, 7.0f); //w
		LETTER_WIDTHS.put(index++, 5.0f); //x
		LETTER_WIDTHS.put(index++, 5.0f); //y
		LETTER_WIDTHS.put(index++, 5.0f); //z
		
		//Add shadow thickness
		for (Character key : LETTER_WIDTHS.keySet()) {
			LETTER_WIDTHS.put(key, LETTER_WIDTHS.get(key) + 2.0f);
		}
		
	}
	
	
	public DefaultLetterBuilder(SpriteSheet spriteSheet) {
		this.spriteSheet = spriteSheet;
	}
	
	@Override
	public Map<Character, Letter> buildLetters() {
		
		int accumulatedOffsetX = 0;
		
		Map<Character, Letter> retVal = Maps.newHashMap();
		for (char c = ' '; c <= 'z'; c++) {
			float width = LETTER_WIDTHS.get(c);
			Image image = spriteSheet.getSubImage(accumulatedOffsetX, 0, (int)width, (int)Letter.LETTER_HEIGHT);
			Letter letter = Letter.create(c, width, image);
			retVal.put(c, letter);
			accumulatedOffsetX += width + SPRITE_SHEET_LETTER_PADDING;
		}
		
		return retVal;
	}
	
}
