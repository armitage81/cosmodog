package antonafanasjew.cosmodog.ingamemenu.map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.controller.InputHandler;

public class MapInputHandler implements InputHandler {

	private MapInputState mapInputState;
	
	public MapInputHandler(MapInputState mapInputState) {
		this.mapInputState = mapInputState;
	}
	
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
			mapInputState.left();
		} else if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			mapInputState.right();
		} else if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			mapInputState.up();
		} else if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			mapInputState.down();
		}
	}
	
}
