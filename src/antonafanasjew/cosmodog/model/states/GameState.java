package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.DyingPlayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.InGameMenuRenderer;
import antonafanasjew.cosmodog.rendering.renderer.InterfaceOnSceneRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.GameEventUtils;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public class GameState extends CosmodogAbstractState {

	private boolean firstUpdate;
	
	private ApplicationContext applicationContext = null;
	
	private Renderer interfaceOnSceneRenderer;
	
	private Renderer dyingPlayerRenderer;
	
	
	private InGameMenuRenderer inGameMenuRenderer;
	
	@Override
	public void firstEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {

		applicationContext = ApplicationContext.instance();
		
		interfaceOnSceneRenderer = new InterfaceOnSceneRenderer();
		
		dyingPlayerRenderer = new DyingPlayerRenderer();
		
		inGameMenuRenderer = new InGameMenuRenderer();
		
	}

	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		if (cosmodog.getGameLifeCycle().isStartNewGame()) {

			CustomTiledMap customTiledMap = applicationContext.getCustomTiledMap();
			
    		CosmodogGame cosmodogGame;
    		
    		try {
				cosmodogGame = InitializationUtils.initializeCosmodogGame(game, customTiledMap, "Armitage");
				cosmodogGame.setGameName(cosmodog.getGameLifeCycle().getGameName());
				CosmodogMap map = cosmodogGame.getMap();		
				Rectangle scene = Rectangle.fromSize((float) (map.getWidth() * map.getTileWidth()), (float) (map.getHeight() * map.getTileHeight()));

				DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
				
				try {
					cosmodogGame.setCam(new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, scene, sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h()));
				} catch (CamPositioningException e) {
					Log.error("Camera positioning could not be established", e);
				}
				Player player = cosmodogGame.getPlayer();
				Cam cam = cosmodogGame.getCam();
				cam.zoom(Constants.DEFAULT_CAM_ZOOM_FACTOR);
				cam.focusOnPiece(map, 0, 0, player);
				
				
			} catch (TiledMapIoException e1) {
				throw new SlickException("Error while reading the cosmodog map", e1);
			}
    		
    		applicationContext.getCosmodog().setCosmodogGame(cosmodogGame);

    		//Check for the rules of the new game event
    		GameEventUtils.throwEvent(new GameEventNewGame());
    		
		}
		
		MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);
		
		firstUpdate = true;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		
		if (firstUpdate) {
			n = 0; //We ignore the first update as it can be big because of the initialization.
			gc.getInput().clearKeyPressedRecord();
			firstUpdate = false;
		}
		
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		Input input = gc.getInput();

		//Blocking interface actions, like text frames or dialog boxes, have their own modal input handling, so handle the input only in case they are not registered.
		//Also the asyncronous actions from the action registry should be updated only if no blocking interface action is registered.
		if (cosmodogGame.getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.BLOCKING_INTERFACE) == null) {

			if (input.isKeyPressed(Input.KEY_0)) {
    			sbg.enterState(CosmodogStarter.DEBUG_STATE_ID);
    		}
    
    		if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
    			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_CONTROL).handleInput(gc, sbg, n, applicationContext);
    		} else {
    			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME).handleInput(gc, sbg, n, applicationContext);
    		}

    		cosmodogGame.getChronometer().update(n);
    		
		}
		
		cosmodogGame.getActionRegistry().update(n, gc, sbg);
		cosmodogGame.getInterfaceActionRegistry().update(n, gc, sbg);
		
		//After processing a loop, clear the record of pressed buttons.
		input.clearKeyPressedRecord();
		
	}

	public static int maxTime = 0;

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		interfaceOnSceneRenderer.render(gc, g, null);
		dyingPlayerRenderer.render(gc, g, null);
		inGameMenuRenderer.render(gc, g, null);
	}


	@Override
	public int getID() {
		return CosmodogStarter.GAME_STATE_ID;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		//Stop ambient sounds
		
		ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
	}
}
