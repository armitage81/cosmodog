package antonafanasjew.cosmodog.controller;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;

/**
 * Implementation of this interface will handle the user input.
 * The input is not given as parameter. Instead, the implementations will check if buttons are pressed.
 */
public interface InputHandler {

	/**
	 * Implementations will contain the input handling.
	 * @param gc Game container.
	 * @param sbg State based game.
	 * @param delta Passed time since the last update loop.
	 * @param applicationContext Application context.
	 */
	void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext);
	
}
