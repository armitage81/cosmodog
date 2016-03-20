package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

/**
 * Lets the player control the dialog box by pressing enter.
 */
public class InGameDialogInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		WritingTextBoxState writingTextBoxState = cosmodogGame.getWritingTextBoxState();
		
		Input input = gc.getInput();

		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			writingTextBoxState.displayCompleteBoxOrSwitchToNextBoxOrFinish();
		}	

		input.clearKeyPressedRecord();

	}

}
