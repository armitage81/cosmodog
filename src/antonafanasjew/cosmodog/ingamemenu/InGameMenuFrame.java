package antonafanasjew.cosmodog.ingamemenu;

import antonafanasjew.cosmodog.controller.InputHandler;
import antonafanasjew.cosmodog.ingamemenu.inventory.InventoryInputHandler;
import antonafanasjew.cosmodog.ingamemenu.inventory.InventoryInputState;
import antonafanasjew.cosmodog.ingamemenu.inventory.InventoryRenderer;
import antonafanasjew.cosmodog.ingamemenu.logplayer.LogPlayerInputHandler;
import antonafanasjew.cosmodog.ingamemenu.logplayer.LogPlayerInputState;
import antonafanasjew.cosmodog.ingamemenu.logplayer.LogPlayerRenderer;
import antonafanasjew.cosmodog.ingamemenu.map.MapInputHandler;
import antonafanasjew.cosmodog.ingamemenu.map.MapInputState;
import antonafanasjew.cosmodog.ingamemenu.map.MapRenderer;
import antonafanasjew.cosmodog.ingamemenu.options.OptionsInputHandler;
import antonafanasjew.cosmodog.ingamemenu.options.OptionsInputState;
import antonafanasjew.cosmodog.ingamemenu.options.OptionsRenderer;
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
	private static MapInputState mapInputState = new MapInputState();
	private static LogPlayerInputState logPlayerInputState = new LogPlayerInputState();
	private static OptionsInputState optionsInputState = new OptionsInputState();
	
	public static final InGameMenuFrame INVENTORY_INGAME_MENU_FRAME = new InGameMenuFrame("Inventory", new InventoryInputHandler(inventoryInputState), new InventoryRenderer(), inventoryInputState);
	public static final InGameMenuFrame PROGRESS_INGAME_MENU_FRAME = new InGameMenuFrame("Game progress", new ProgressInputHandler(), new ProgressRenderer(), new ProgressInputState());
	public static final InGameMenuFrame MAP_INGAME_MENU_FRAME = new InGameMenuFrame("Map", new MapInputHandler(mapInputState), new MapRenderer(), mapInputState);
	public static final InGameMenuFrame LOG_PLAYER_INGAME_MENU_FRAME = new InGameMenuFrame("Log Player", new LogPlayerInputHandler(logPlayerInputState), new LogPlayerRenderer(), logPlayerInputState);
	public static final InGameMenuFrame OPTIONS_INGAME_MENU_FRAME = new InGameMenuFrame("Save and Quit", new OptionsInputHandler(), new OptionsRenderer(), optionsInputState);
	
	
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
