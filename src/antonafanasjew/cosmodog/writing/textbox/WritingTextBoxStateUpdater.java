package antonafanasjew.cosmodog.writing.textbox;

import java.io.Serializable;

import antonafanasjew.cosmodog.writing.model.NarrativeSequence;

/**
 * This is a wrapper around a text box state that updates it automatically.
 */
public class WritingTextBoxStateUpdater implements Serializable {

	private static final long serialVersionUID = -5731824239719935998L;

	//Holds the time passed since the last box has been fully shown or 0 if no box is fully shown.
	private long timePassedAfterFullBoxSown = 0;
	
	//Holds the desired interval of time between a box has been displayed fully and before it will be switched.
	private int millisecondsToWaitAfterFullBoxShown;
	
	private WritingTextBox textBox;
	private WritingTextBoxState state;

	public WritingTextBoxStateUpdater(int millisecondsToWaitAfterFullBoxShown, WritingTextBox textBox) {
		this.millisecondsToWaitAfterFullBoxShown = millisecondsToWaitAfterFullBoxShown;
		this.textBox = textBox;
	}
	
	/**
	 * 
	 * @param s Narrative sequence to add.
	 * @param onlyIfStateEmpty indicates whether the narrative sequence should be added only if there is no other sequence displaying.
	 * @param replaceStateIfNotEmpty Only considered if onlyIfStateEmpty == false. Indicates whether to replace the existing state (true) or ignore the new message (false)
	 */
	public void addNarrativeSequence(NarrativeSequence s, boolean onlyIfStateEmpty, boolean replaceStateIfNotEmpty) {
		if (getState() == null) {
			WritingTextBoxContent content = new WritingTextBoxContent(textBox, s.getTextBlocks());
			state = new WritingTextBoxState(content);
		} else {
			if (onlyIfStateEmpty == false) {
				
				if (replaceStateIfNotEmpty) {
					getState().getWritingTextBoxContent().getText().clear();
					getState().resetPassedTime();
				}
				
    			getState().getWritingTextBoxContent().getText().addAll(s.getTextBlocks());
    			getState().getWritingTextBoxContent().recalculateCaches();
			}
		}
	}
	
	public void addNarrativeSequence(NarrativeSequence s) {
		addNarrativeSequence(s, false, false);
	}
	
	public void update(int delta) {
		if (state != null) {
    		state.update(delta);
    		if (getState().completeBoxDisplayed()) {
    			timePassedAfterFullBoxSown += delta;
    			if (timePassedAfterFullBoxSown >= millisecondsToWaitAfterFullBoxShown) {
    				getState().switchToNextBoxOrFinish();
    				timePassedAfterFullBoxSown = 0;
    				if (getState().isFinish()) {
    					state = null;
    				}
    			}
    		}
		}
	}

	public WritingTextBoxState getState() {
		return state;
	}
}
