package antonafanasjew.cosmodog.ingamemenu.map;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.controller.InputHandler;

public class MapInputHandler implements InputHandler {

	private static final int DELAY_LIMIT = 3;
	
	private MapInputState mapInputState;
	private int delay = 0;
	
	public MapInputHandler(MapInputState mapInputState) {
		this.mapInputState = mapInputState;
	}
	
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {

		//We do not render the map except on the main map
		MapType mapType = ApplicationContextUtils.mapOfPlayerLocation().getMapType();
		if (mapType != MapType.MAIN) {
			return;
		}

		if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
			mapInputState.left();
			delay = 0;
			return;
		} else if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			mapInputState.right();
			delay = 0;
			return;
		} else if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			mapInputState.up();
			delay = 0;
			return;
		} else if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			mapInputState.down();
			delay = 0;
			return;
		}
		
		if (delay < DELAY_LIMIT) {
			delay++;
		}
		
		if (delay != DELAY_LIMIT) {
			return;
		}
		
		if (gc.getInput().isKeyDown(Input.KEY_LEFT)) {
			mapInputState.left();
		} else if (gc.getInput().isKeyDown(Input.KEY_RIGHT)) {
			mapInputState.right();
		} else if (gc.getInput().isKeyDown(Input.KEY_UP)) {
			mapInputState.up();
		} else if (gc.getInput().isKeyDown(Input.KEY_DOWN)) {
			mapInputState.down();
		}
	}
	
}
