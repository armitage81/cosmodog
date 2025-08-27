package antonafanasjew.cosmodog.model.states;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.ingamemenu.InGameMenu;
import antonafanasjew.cosmodog.rendering.renderer.*;
import antonafanasjew.cosmodog.util.*;
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
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.Rectangle;
import profiling.ProfilerUtils;

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
				int tileLength = TileUtils.tileLengthSupplier.get();

				cosmodogGame = InitializationUtils.initializeCosmodogGame(game, customTiledMaps, "Armitage");
				applicationContext.getCosmodog().setCosmodogGame(cosmodogGame);
				cosmodogGame.setGameName(cosmodog.getGameLifeCycle().getGameName());
				CosmodogMap map = cosmodogGame.mapOfPlayerLocation();
				Rectangle scene = Rectangle.fromSize((float) (map.getMapType().getWidth() * tileLength), (float) (map.getMapType().getHeight() * tileLength));

				DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();

				try {
					cosmodogGame.setCam(new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, scene, sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h(), map.getMapType()));
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
		String currentMapMusicId = MusicUtils.currentMapMusicId();
		MusicUtils.loopMusic(currentMapMusicId);

		firstUpdate = true;
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, final int delta) throws SlickException {

		ProfilerUtils.runWithProfiling("GameState.update", () -> {
			int n = delta;
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

			// Modal window actions, like text frames or dialog boxes, have their own
			// modal input handling, so handle the input only in case they are not registered.
			// Also the asyncronous actions from the action registry should be updated only
			// if no modal window action is registered.
			if (cosmodogGame.getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.MODAL_WINDOW) == null) {
				cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME).handleInput(gc, sbg, n, applicationContext);

			}

			cosmodogGame.getActionRegistry().update(n, gc, sbg);
			cosmodogGame.getInterfaceActionRegistry().update(n, gc, sbg);

			// After processing a loop, clear the record of pressed buttons.
			input.clearKeyPressedRecord();
		});
	}

	public static int maxTime = 0;

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		ProfilerUtils.runWithProfiling("SceneRenderer.render", () -> {

			InGameMenu inGameMenu = ApplicationContextUtils.getCosmodogGame().getInGameMenu();

			//No need to render the game world if a menu is open.
			if (inGameMenu == null) {
				interfaceOnSceneRenderer.render(gc, g, null);
				dyingPlayerRenderer.render(gc, g, null);
				wrongSequenceRenderer.render(gc, g, null);
			}
			inGameMenuRenderer.render(gc, g, null);
		});
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
