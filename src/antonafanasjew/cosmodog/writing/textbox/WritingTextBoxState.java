package antonafanasjew.cosmodog.writing.textbox;

import java.util.List;

import antonafanasjew.cosmodog.writing.dynamics.TextBlockDynamicsCalculatorUtils;
import antonafanasjew.cosmodog.writing.model.TextBlockBox;

public class WritingTextBoxState {

	//Content of the text box with the text that is split in lines and pages/boxes
	private WritingTextBoxContent writingTextBoxContent;

	//Current page number. This gives the renderer the information which text part from the text box content should be rendered.
	private int boxIndex = 0;
	
	//Holds the current text index of the current text box that depends on the passed time. The text index is the index in the text of all text blocks concatenated by spaces and new lines.
	private int currentTextIndex;
	
	//This indicates the time that has passed since the last page/box has been rendered for the first time.
	//This time is important to calculate the line and the text block indexes in the current box
	//but it has no meaning in case displayCompleteBox is true.
	private int timePassedSinceBoxShown;
	
	//This flag indicates that the complete box under boxIndex should be shown regardless of passed time.
	private boolean displayCompleteBox;
	
	//This flag indicates that the player finished reading the text. usually, it will happen if all lines of 
	//the last box/page are displayed completely and the player is pressing enter to close the dialog box.
	private boolean finish = false;
	
	public WritingTextBoxState(WritingTextBoxContent writingTextBoxContent) {
		this.writingTextBoxContent = writingTextBoxContent;
	}
	
	
	/**
	 * Indicates whether there are more boxes/pages to be shown after the current one.
	 */
	public boolean hasMoreBoxes() {
		List<?> boxes = getWritingTextBoxContent().getTextChunksForBoxes();
		return boxIndex < boxes.size() - 1;
	}
	
	
	/**
	 * Switches to the next box if any. Resets passed time since the box is shown and the displayCompleteBox flag.
	 * If no more boxes are there - sets the finish flag.
	 */
	public void switchToNextBoxOrFinish() {
		if (hasMoreBoxes()) {
			boxIndex = boxIndex + 1;
			timePassedSinceBoxShown = 0;
			displayCompleteBox = false;
			currentTextIndex = 0;
		} else {
			finish = true;
		}
	}
	
	/**
	 * This is necessary in case the text needs to be replaced on the fly (f.i. in the comments, when an important comment replaces another one.)
	 */
	public void resetPassedTime() {
		this.timePassedSinceBoxShown = 0;
	}
	
	public TextBlockBox textOfCurrentBox() {
		TextBlockBox textBlockBox = getWritingTextBoxContent().getTextChunksForBoxes().get(boxIndex);
		return textBlockBox;
	}
	
	public TextBlockBox dynamicTextOfCurrentBox() {
		TextBlockBox textBlockBox = textOfCurrentBox();
		return displayCompleteBox ? textBlockBox : textBlockBox.subTextBlockBox(currentTextIndex);
	}
	
	/**
	 * Sets the flag that the complete box should be rendered (f.i. if the player did not want to wait for the running text and pressed enter)
	 */
	public void displayCompleteBox() {
		this.displayCompleteBox = true;
	}
	
	/**
	 * Returns true if the complete box is displayed, that is, the displayCompleteBox flag is set or
	 * the index refers to the last character of the box' aggregated text.
	 */
	public boolean completeBoxDisplayed() {
		if (displayCompleteBox) {
			return true;
		}
		
		return currentTextIndex >= textOfCurrentBox().aggregatedText().length() - 1;
		
	}
	
	public void displayCompleteBoxOrSwitchToNextBoxOrFinish() {
		if (completeBoxDisplayed()) {
			switchToNextBoxOrFinish();
		} else {
			displayCompleteBox();
		}
	}
	
	/**
	 * This method updates the state of the text box content depending on the passed time.
	 * The time is not updated in case the complete box is displayed already or the finish flag is set.
	 */
	public void update(int delta) {
		if (!displayCompleteBox && !finish) {
			timePassedSinceBoxShown += delta;
			currentTextIndex = textIndexFromPassedTime();
		}
	}


	/*
	 * The index returned here should be used as second parameter in the substring method.
	 */
	private int textIndexFromPassedTime() {
		List<Integer> intervals = TextBlockDynamicsCalculatorUtils.letterIntervalDurations(getWritingTextBoxContent().getTextChunksForBoxes().get(boxIndex));
		
		int intervIndex = 0;
		int intervalsSum = 0;
		for (int i = 0; i < intervals.size(); i++) {
			if (intervalsSum + intervals.get(i) > timePassedSinceBoxShown) {
				break;
			}
			intervalsSum += intervals.get(i);
			intervIndex = i;
		}
		return intervIndex == intervals.size() - 1 ? intervIndex : intervIndex + 1;
	}


	public WritingTextBoxContent getWritingTextBoxContent() {
		return writingTextBoxContent;
	}

	public boolean isFinish() {
		return finish;
	}

	public int getCurrentBoxIndex() {
		return boxIndex;
	}

}
