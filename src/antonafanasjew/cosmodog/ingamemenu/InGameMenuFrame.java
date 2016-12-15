package antonafanasjew.cosmodog.ingamemenu;

import antonafanasjew.cosmodog.controller.InputHandler;
import antonafanasjew.cosmodog.ingamemenu.inventory.InventoryInputHandler;
import antonafanasjew.cosmodog.ingamemenu.inventory.InventoryInputState;
import antonafanasjew.cosmodog.ingamemenu.inventory.InventoryRenderer;
import antonafanasjew.cosmodog.ingamemenu.progress.ProgressInputHandler;
import antonafanasjew.cosmodog.ingamemenu.progress.ProgressInputState;
import antonafanasjew.cosmodog.ingamemenu.progress.ProgressRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;

public class InGameMenuFrame {

	private String title;
	private InputHandler inputHandler;
	private Renderer contentRenderer;
	private InGameMenuInputState inputState;
	
	private static InventoryInputState inventoryInputState = new InventoryInputState();
	
	public static final InGameMenuFrame INVENTORY_INGAME_MENU_FRAME = new InGameMenuFrame("Inventory", new InventoryInputHandler(inventoryInputState), new InventoryRenderer(), inventoryInputState);
	public static final InGameMenuFrame PROGRESS_INGAME_MENU_FRAME = new InGameMenuFrame("Game progress", new ProgressInputHandler(), new ProgressRenderer(), new ProgressInputState());
	
	private InGameMenuFrame(String title, InputHandler inputHandler, Renderer contentRenderer, InGameMenuInputState inputState) {
		this.title = title;
		this.inputHandler = inputHandler;
		this.contentRenderer = contentRenderer;
		this.inputState = inputState;
	}

	public InputHandler getInputHandler() {
		return inputHandler;
	}
	
	public Renderer getContentRenderer() {
		return contentRenderer;
	}
	
	public InGameMenuInputState getInputState() {
		return inputState;
	}
	
	public String getTitle() {
		return title;
	}

}
