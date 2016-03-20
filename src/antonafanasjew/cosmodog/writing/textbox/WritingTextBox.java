package antonafanasjew.cosmodog.writing.textbox;


public class WritingTextBox {

	private float boxWidth;
	private float boxHeight;
	private float letterPaddingHorizontal;
	private float letterPaddingVertical;
	private float letterWidth;
	private float letterHeight;
	
	private float horizontalBorderSize;
	private float verticalBorderSize;
	private int maxLettersInLine;
	private int maxLinesInBox;
	
	public WritingTextBox(float boxWidth, float boxHeight, float letterPaddingHorizontal, float letterPaddingVertical, float letterWidth, float letterHeight) {
		this.boxWidth = boxWidth;
		this.boxHeight = boxHeight;
		this.letterPaddingHorizontal = letterPaddingHorizontal;
		this.letterPaddingVertical = letterPaddingVertical;
		this.letterWidth = letterWidth;
		this.letterHeight = letterHeight;
		calculateHorizontalBorderSize();
		calculateVerticalBorderSize();
		calculateMaxLettersInLine();
		calculateMaxLinesInBox();
	}

	private void calculateHorizontalBorderSize() {
		float multipleLettersWidth = letterWidth;
		while (multipleLettersWidth <= boxWidth)  {
			multipleLettersWidth += (letterPaddingHorizontal + letterWidth);
		}
		multipleLettersWidth -= (letterPaddingHorizontal + letterWidth);
		horizontalBorderSize = (boxWidth - multipleLettersWidth) / 2.0f;
	}
	
	public float getHorizontalBorderSize() {
		return horizontalBorderSize;
	}
	
	private void calculateVerticalBorderSize() {
		float multipleLettersHeight = letterHeight;
		while (multipleLettersHeight <= boxHeight)  {
			multipleLettersHeight += (letterPaddingVertical + letterHeight);
		}
		multipleLettersHeight -= (letterPaddingVertical + letterHeight);
		verticalBorderSize = (boxHeight - multipleLettersHeight) / 2.0f;		
	}
	
	public float getVerticalBorderSize() {
		return verticalBorderSize;
	}
	
	private void calculateMaxLettersInLine() {
		int n = 1;
		float multipleLettersWidth = letterWidth;
		while (multipleLettersWidth <= boxWidth)  {
			n++;
			multipleLettersWidth += (letterPaddingHorizontal + letterWidth);
		}
		n--;
		maxLettersInLine = n;
	}
	
	public int getMaxLettersInLine() {
		return maxLettersInLine;
	}
	
	private void calculateMaxLinesInBox() {
		int n = 1;
		float multipleLettersHeight = letterHeight;
		while (multipleLettersHeight <= boxHeight)  {
			n++;
			multipleLettersHeight += (letterPaddingVertical + letterHeight);
		}
		n--;
		maxLinesInBox = n;		
	}
	
	public int getMaxLinesInBox() {
		return maxLinesInBox;
	}
	
	public float getBoxWidth() {
		return boxWidth;
	}

	public void setBoxWidth(float boxWidth) {
		this.boxWidth = boxWidth;
	}

	public float getBoxHeight() {
		return boxHeight;
	}

	public void setBoxHeight(float boxHeight) {
		this.boxHeight = boxHeight;
	}

	public float getLetterPaddingHorizontal() {
		return letterPaddingHorizontal;
	}

	public void setLetterPaddingHorizontal(float letterPaddingHorizontal) {
		this.letterPaddingHorizontal = letterPaddingHorizontal;
	}

	public float getLetterPaddingVertical() {
		return letterPaddingVertical;
	}

	public void setLetterPaddingVertical(float letterPaddingVertical) {
		this.letterPaddingVertical = letterPaddingVertical;
	}

	public float getLetterWidth() {
		return letterWidth;
	}

	public void setLetterWidth(float letterWidth) {
		this.letterWidth = letterWidth;
	}

	public float getLetterHeight() {
		return letterHeight;
	}

	public void setLetterHeight(float letterHeight) {
		this.letterHeight = letterHeight;
	}

	public float xForLetter(int letterIndex) {
		return horizontalBorderSize + letterIndex * letterWidth + (letterIndex - 1) * letterPaddingHorizontal;
	}

	public float xForLetterCentered(int letterIndex, int letters) {
		float centeredOffsetX = (boxWidth - (2 * horizontalBorderSize) - (letters * letterWidth) - ((letters - 1) * letterPaddingHorizontal)) / 2.0f;
		return xForLetter(letterIndex) + centeredOffsetX;
	}
	
	public float yForLine(int lineIndex) {
		return verticalBorderSize + lineIndex * letterHeight + (lineIndex - 1) * letterPaddingVertical;
	}
	
}
