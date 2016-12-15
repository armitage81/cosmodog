package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.ingamemenu.InGameMenuFrame;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class InGameMenuInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		InGameMenu inGameMenu = cosmodogGame.getInGameMenu();
		Input input = gc.getInput();

		//First handle the general menu navigation input.
		
		if (gc.getInput().isKeyPressed(Input.KEY_TAB)) {
			if (gc.getInput().isKeyPressed(Input.KEY_LSHIFT) || gc.getInput().isKeyPressed(Input.KEY_RSHIFT)) {
				cosmodogGame.getInGameMenu().switchToPreviousMenuFrame();	
			} else {
				inGameMenu.switchToNextMenuFrame();
			}
		}
		
		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			cosmodogGame.setInGameMenu(null);
		}	

		//Now handle the specific menu frame input.
		
		InGameMenuFrame currentMenuFrame = inGameMenu.currentMenuFrame();
		InputHandler currentMenuFrameInputHandler = currentMenuFrame.getInputHandler();
		
		currentMenuFrameInputHandler.handleInput(gc, sbg, delta, applicationContext);
		
		input.clearKeyPressedRecord();

	}

}
