package antonafanasjew.cosmodog.ingamemenu.inventory;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.controller.InputHandler;

public class InventoryInputHandler implements InputHandler {

	private InventoryInputState inventoryInputState;
	
	public InventoryInputHandler(InventoryInputState inventoryInputState) {
		this.inventoryInputState = inventoryInputState;
	}
	
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		if (gc.getInput().isKeyPressed(Input.KEY_LEFT)) {
			inventoryInputState.left();
		} else if (gc.getInput().isKeyPressed(Input.KEY_RIGHT)) {
			inventoryInputState.right();
		} else if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			inventoryInputState.up();
		} else if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			inventoryInputState.down();
		}
	}

}
