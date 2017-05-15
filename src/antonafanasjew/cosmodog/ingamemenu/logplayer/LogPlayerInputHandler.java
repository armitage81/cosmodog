package antonafanasjew.cosmodog.ingamemenu.logplayer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.controller.InputHandler;

public class LogPlayerInputHandler implements InputHandler {
	
	private LogPlayerInputState state;
	
	public LogPlayerInputHandler(LogPlayerInputState state) {
		this.state = state;
	}
	
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
			state.left();
		} else if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			state.right();
		} else if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			state.up();
		} else if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			state.down();
		} else if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			state.rotatePage();
		}
	}
}
