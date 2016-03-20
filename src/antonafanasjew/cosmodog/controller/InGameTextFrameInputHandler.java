package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Lets the player control the dialog box by pressing enter.
 */
public class InGameTextFrameInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		Input input = gc.getInput();

		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			cosmodogGame.setTextFrame(null);
		}	

		input.clearKeyPressedRecord();

	}

}
