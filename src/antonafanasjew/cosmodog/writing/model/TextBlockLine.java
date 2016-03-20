package antonafanasjew.cosmodog.writing.model;

import java.util.ArrayList;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * Represents a chunk of text blocks that form a text line.
 * It cannot contain new lines or paragraphs
 */
public class TextBlockLine extends ArrayList<TextBlock> {

	private static final long serialVersionUID = -2947077624027480642L;

	public String aggregatedText() {
		return Joiner.on(" ").join(Iterables.transform(this, new Function<TextBlock, String>() {
			@Override
			public String apply(TextBlock textBlock) {
				return textBlock.getText();
			}
		}));
	}
	
	public boolean endsWithNewLine() {
		return size() == 0 ? false : get(size() - 1).isEndsWithLineBreak();
	}
	
	public boolean endsWithParagraph() {
		return size() == 0 ? false : get(size() - 1).isEndsWithParagraph();
	}

	
	/**
	 * Returns a text block for a character index considering the blocks joined by a white space character.
	 * Will return null in case the index is exactly at one of the white space characters
	 */
	public TextBlock textBlockForCharacterIndex(int index) {
		int begin = -1;
		int end = -1;
		for (TextBlock textBlock : this) {
			begin = (end == -1 ? 0 : end + 1);
			end = begin + textBlock.length();
			if (begin <= index && end > index) {
				return textBlock;
			}
		}
		return null;
	}
	
	public TextBlockLine subTextBlockLine(int lengthInCharacters) {
		TextBlockLine retVal = new TextBlockLine();
		
		int subLineLengthInCharacters = 0;
		
		for (TextBlock textBlock : this) {
			int textBlockLength = textBlock.getText().length();

			//Add one for whitespace. The last one will be skipped as it is the end of line.
			if (!textBlock.isEndsWithLineBreak() && !textBlock.isEndsWithParagraph()) {
				textBlockLength++;
			}
			
			//Check the case when the new text block with the whitespace (except EOL) will fit completely.
			if (subLineLengthInCharacters + textBlockLength <= lengthInCharacters) {
				retVal.add(textBlock);
				subLineLengthInCharacters = subLineLengthInCharacters + textBlockLength;
			} else {
				//It can happen that the block does only fit partially, or it should be skipped at all if the text box line has
				//exactly the same length as needed
				if (subLineLengthInCharacters < lengthInCharacters) {
					int difference = lengthInCharacters - subLineLengthInCharacters;
					TextBlock partialTextBlock = textBlock.subTextBlock(difference);
					retVal.add(partialTextBlock);
				}
				//In any case, we don't care about the rest of the line as our current block is already too much.
				break;
			}
		}

		
		return retVal;
	}

	public int length() {
		return this.aggregatedText().length();
	}
	
}
