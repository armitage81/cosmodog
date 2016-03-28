package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;

/**
 * Abstract implementation of the input handler to hold the common logic.
 */
public abstract class AbstractInputHandler implements InputHandler {
	
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		handleInputInternal(gc, sbg, delta, applicationContext);
	}

	protected abstract void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext);

}
