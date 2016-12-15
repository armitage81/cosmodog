package antonafanasjew.cosmodog.ingamemenu.progress;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.controller.InputHandler;

public class ProgressInputHandler implements InputHandler {
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		if (gc.getInput().isKeyPressed(Input.KEY_P)) {
			System.out.println("Handling progress input...");
		}
	}
}
