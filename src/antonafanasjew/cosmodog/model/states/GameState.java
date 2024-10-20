package antonafanasjew.cosmodog.model.states;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.rendering.renderer.*;
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
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.GameEventUtils;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

import java.util.Map;

/**
 * This class implements the loop where the actual game takes place.
 * 
 * It shows the scene, the player, the enemies and the terrain.
 * It listens to keyboard controls and updates the player positions.
 * 
 * In-game menus, like map, inventory etc. are also handled here.
 * 
 * Don't mix it with the base class GameState from the slick2d framework.
 *
 */
public class GameState extends CosmodogAbstractState {

	private boolean firstUpdate;

	private ApplicationContext applicationContext;

	private Renderer interfaceOnSceneRenderer;

	private Renderer dyingPlayerRenderer;

	private Renderer wrongSequenceRenderer;

	private InGameMenuRenderer inGameMenuRenderer;

	@Override
	public void firstEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {

		applicationContext = ApplicationContext.instance();

		interfaceOnSceneRenderer = new InterfaceOnSceneRenderer();

		dyingPlayerRenderer = new DyingPlayerRenderer();

		wrongSequenceRenderer = new RespawnRenderer();

		inGameMenuRenderer = new InGameMenuRenderer();

	}

	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		if (cosmodog.getGameLifeCycle().isStartNewGame()) {

			Map<MapType, CustomTiledMap> customTiledMaps = applicationContext.getCustomTiledMaps();

			CosmodogGame cosmodogGame;

			try {
				cosmodogGame = InitializationUtils.initializeCosmodogGame(game, customTiledMaps, "Armitage");
				applicationContext.getCosmodog().setCosmodogGame(cosmodogGame);
				cosmodogGame.setGameName(cosmodog.getGameLifeCycle().getGameName());
				CosmodogMap map = cosmodogGame.mapOfPlayerLocation();
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
				cam.focusOnPiece(cosmodogGame, 0, 0, player);

			} catch (TiledMapIoException e1) {
				throw new SlickException("Error while reading the cosmodog map", e1);
			}



			// Check for the rules of the new game event
			GameEventUtils.throwEvent(new GameEventNewGame());

		}
		MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);

		firstUpdate = true;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {

		if (firstUpdate) {
			n = 0; // We ignore the first update as it can be big because of the initialization.
			gc.getInput().clearKeyPressedRecord();
			firstUpdate = false;
		}

		final int nn = n;
		ApplicationContext.instance().getAnimations().values().forEach(e -> e.update(nn));

		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		Input input = gc.getInput();

		// Blocking interface actions, like text frames or dialog boxes, have their own
		// modal input handling, so handle the input only in case they are not registered.
		// Also the asyncronous actions from the action registry should be updated only
		// if no blocking interface action is registered.
		if (cosmodogGame.getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.BLOCKING_INTERFACE) == null) {
			if (Features.getInstance().featureOn(Features.FEATURE_DEBUGGER)) {

				if (input.isKeyPressed(Input.KEY_0)) {
					sbg.enterState(CosmodogStarter.DEBUG_STATE_ID);
				}

			}
			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME).handleInput(gc, sbg, n, applicationContext);

		}

		cosmodogGame.getActionRegistry().update(n, gc, sbg);
		cosmodogGame.getInterfaceActionRegistry().update(n, gc, sbg);

		// After processing a loop, clear the record of pressed buttons.
		input.clearKeyPressedRecord();

	}

	public static int maxTime = 0;

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		interfaceOnSceneRenderer.render(gc, g, null);
		dyingPlayerRenderer.render(gc, g, null);
		wrongSequenceRenderer.render(gc, g, null);
		inGameMenuRenderer.render(gc, g, null);

	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_STATE_ID;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		// Stop ambient sounds

		ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
	}
}
