package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;

/**
 * Handles debugging commands. Collects text till enter is pressed, then executes the command 
 */
public class DebugConsoleInputHandler extends AbstractInputHandler {

	private StringBuffer sb = new StringBuffer();
	
	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		
		Input input = gc.getInput();
		
		if (input.isKeyPressed(Input.KEY_RETURN)) {
			executeCommand(sb.toString());
			sb = new StringBuffer();
		} else {
		}
		
		
		input.clearKeyPressedRecord();
		
	}

	private void executeCommand(String string) {
		Log.info(string);
	}

}
