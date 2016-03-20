package antonafanasjew.cosmodog.writing.model;

import java.util.ArrayList;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * Represents a text box full of text. Cannot contain paragraphs.
 */
public class TextBlockBox extends ArrayList<TextBlockLine> {

	private static final long serialVersionUID = -4711390704919503618L;

	public String aggregatedText() {
		return Joiner.on("\n").join(Iterables.transform(this, new Function<TextBlockLine, String>() {
			@Override
			public String apply(TextBlockLine textBlockLine) {
				return textBlockLine.aggregatedText();
			}
		}));
	}
	
	public boolean endsWithNewLine() {
		return size() == 0 ? false : get(size() - 1).endsWithNewLine();
	}
	
	public boolean endsWithParagraph() {
		return size() == 0 ? false : get(size() - 1).endsWithParagraph();
	}
	
	public TextBlockBox subTextBlockBox(int lengthInCharacters) {
		TextBlockBox retVal = new TextBlockBox();
		
		int subBoxLengthInCharacters = 0;
		
		for (TextBlockLine textBlockLine : this) {
			int textBlockLineLength = textBlockLine.aggregatedText().length();

			//Add one for EOL. The last one will be skipped as it is the end of box.
			if (!textBlockLine.endsWithParagraph()) {
				textBlockLineLength++;
			}
			
			//Check the case when the new text block line with the EOL (except EOF) will fit completely.
			if (subBoxLengthInCharacters + textBlockLineLength <= lengthInCharacters) {
				retVal.add(textBlockLine);
				subBoxLengthInCharacters = subBoxLengthInCharacters + textBlockLineLength;
			} else {
				//It can happen that the line does only fit partially, or it should be skipped at all if the text box has
				//exactly the same length as needed
				if (subBoxLengthInCharacters < lengthInCharacters) {
					int difference = lengthInCharacters - subBoxLengthInCharacters;
					TextBlockLine partialTextBlockLine = textBlockLine.subTextBlockLine(difference);
					retVal.add(partialTextBlockLine);
				}
				//In any case, we don't care about the rest of the box as our current line is already too much.
				break;
			}
		}

		
		return retVal;
	}

	public int length() {
		return this.aggregatedText().length();
	}
	
}
