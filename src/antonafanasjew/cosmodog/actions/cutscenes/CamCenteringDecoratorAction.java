package antonafanasjew.cosmodog.actions.cutscenes;

import antonafanasjew.cosmodog.camera.Cam;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;

import java.io.Serial;

/**
 * Represents an asynchronous action for a camera movement.
 * Example: The camera moves to a specific position on the map, then something happens, then the camera moves back to the player.
 * This action is a decorator for another asynchronous action. The lifecycle is as following:
 * The camera loses focus on the player and moves towards a position.
 * The underlying action is executed.
 * The camera moves back to the player and focuses on him.
 * The speed of the camera movement is defined as time duration. That is, the camera will move faster if the target position
 * is more remote from the player.
 * The camera movement to the focus and the movement back are themselves async actions with fixed length.
 * Being components of this async action, they are considered phases and registered in the phase registry of this action.
 * The same happens with the underlying action.
 */
public class CamCenteringDecoratorAction  extends VariableLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = 5171259029091809078L;

	/*
	* The duration of the camera movement to the target in milliseconds.
	* The same time will be spent on the movement back to the player.
	*/
	private final int camMovementDuration;

	/*
	 * Horizontal component of the point in the world to which the center of the camera will be moved.
	 * It is given in unscaled world coordinates.
	 */
	private final float camCenteringX;

	/*
	 * Vertical component of the point in the world to which the center of the camera will be moved.
	 * It is given in unscaled world coordinates.
	 */
	private final float camCenteringY;

	/*
	* This action will be executed after the camera moves to its target position, and before it moves back to the player.
	* This underlying action will be registered as a phase in the phase registry of the actual action.
	 */
	private final AsyncAction underlyingAsyncAction;

	/*
	 * The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	private final CosmodogGame cosmodogGame;

	/*
	 * The registry of the phases of this action. It is used to hold the state of all phases of this action.
	 */
	private final ActionRegistry actionPhaseRegistry = new ActionRegistry();

	/**
	 * Creates the action instance by defining where the camera should be pointed on the map,
	 * how long the camera movement should take,
	 * and what underlying action should be executed after the camera movement.
	 * The camera target is defined as a position of the focus tile (not a point in the world).
	 * The actual point of the camera focus in the world is calculated within this constructor from the tile position.
	 *
	 * @param camMovementDuration The duration of the camera movement to the target in milliseconds.
	 * The same time will be spent on the movement back to the player.
	 * @param positionX The horizontal component of the tile position to which the center of the camera will be moved.
	 * @param positionY The vertical component of the tile position to which the center of the camera will be moved.
	 * @param underlyingAsyncAction Underlying action that will be executed after the camera moves to its target position, and before it moves back to the player.
	 * It will be registered as a phase in the phase registry of the actual action.
	 * @param cosmodogGame The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	public CamCenteringDecoratorAction(
			int camMovementDuration,
			int positionX,
			int positionY,
			AsyncAction underlyingAsyncAction,
			CosmodogGame cosmodogGame) {

		this.camMovementDuration = camMovementDuration;
		float tileWidth = cosmodogGame.getMap().getTileWidth();
		float tileHeight = cosmodogGame.getMap().getTileHeight();
		this.camCenteringX = positionX * tileWidth + tileWidth / 2;
		this.camCenteringY = positionY * tileHeight + tileHeight / 2;
		this.underlyingAsyncAction = underlyingAsyncAction;
		this.cosmodogGame = cosmodogGame;
	}

	/**
	 * Creates the action instance by defining where the camera should be pointed on the map,
	 * how long the camera movement should take,
	 * and what underlying action should be executed after the camera movement.
	 * The camera target is defined as a piece on the map (which could be a npc, a dynamic piece etc.).
	 * The actual point of the camera focus in the world is calculated within this constructor from the piece's position.
	 *
	 * @param camMovementDuration The duration of the camera movement to the target in milliseconds.
	 * The same time will be spent on the movement back to the player.
	 * @param piece The piece on which the camera should focus. Can be an enemy, a npc, a dynamic piece etc.
	 * @param underlyingAsyncAction Underlying action that will be executed after the camera moves to its target position, and before it moves back to the player.
	 * It will be registered as a phase in the phase registry of the actual action.
	 * @param cosmodogGame The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	public CamCenteringDecoratorAction(int camMovementDuration, Piece piece, AsyncAction underlyingAsyncAction, CosmodogGame cosmodogGame) {
		this(camMovementDuration, piece.getPositionX(), piece.getPositionY(), underlyingAsyncAction, cosmodogGame);
		
	}

	/**
	 * Creates the action instance by defining where the camera should be pointed on the map,
	 * how long the camera movement should take,
	 * and what underlying action should be executed after the camera movement.
	 * <p>
	 * The camera target is defined as a point in the world.
	 * <p>
	 * Take note: Zooming is irrelevant for the target point in the world.
	 * In Cosmodog, zooming means that the scene is scaled while the view's size stays the same.
	 * But here, camCenteringX and camCenteringY are given in the unscaled world's coordinate system.
	 * <p>
	 * Example: Scene is 40x40 pixels big and the view is 10x10 pixels big. The camera must be moved to the middle of the scene.
	 * Without zoom, the target point is 20/20. With zoom factor 2.0, the target point would be 40/40.
	 * But since zooming is not considered, the target point is still 20/20.
	 *
	 * @param camMovementDuration The duration of the camera movement to the target in milliseconds.
	 * The same time will be spent on the movement back to the player.
	 * @param camCenteringX The horizontal component of the point in the world to which the center of the camera will be moved.
	 * @param camCenteringY The vertical component of the point in the world to which the center of the camera will be moved.
	 * @param underlyingAsyncAction Underlying action that will be executed after the camera moves to its target position, and before it moves back to the player.
	 * It will be registered as a phase in the phase registry of the actual action.
	 * @param cosmodogGame The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	public CamCenteringDecoratorAction(int camMovementDuration, float camCenteringX, float camCenteringY, AsyncAction underlyingAsyncAction, CosmodogGame cosmodogGame) {
		this.camMovementDuration = camMovementDuration;
		this.camCenteringX = camCenteringX;
		this.camCenteringY = camCenteringY;
		this.underlyingAsyncAction = underlyingAsyncAction;
		this.cosmodogGame = cosmodogGame;
		
	}

	/**
	 * Initializes the action by doing the following steps:
	 * <p>
	 * 1. Calculates the point of the current camera focus in the world.
	 * <p>
	 * 2. Registers a phase in the local action registry that defines the camera movement to the target point.
	 * <p>
	 * 3. Registers the underlying action as a phase in the local action registry.
	 * <p>
	 * 4. Registers a phase in the local action registry that defines the camera movement back to the current point.
	 * <p>
	 * Take note: No zooming is considered when talking about both initial and target points. The target point
	 * is given in unscaled world coordinates. However, the positioning methods of the {@link Cam} class consider the zoom factor.
	 * That means that the initial point includes the zoom factor. That's why it must be divided by the latter.
	 */
	@Override
	public void onTrigger() {
		float initialCamX = cosmodogGame.getCam().viewCopy().centerX() / cosmodogGame.getCam().getZoomFactor();
		float initialCamY = cosmodogGame.getCam().viewCopy().centerY() / cosmodogGame.getCam().getZoomFactor();
		
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new CamMovementAction(camMovementDuration, camCenteringX, camCenteringY, cosmodogGame));
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, underlyingAsyncAction);
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new CamMovementAction(camMovementDuration, initialCamX, initialCamY, cosmodogGame));
		
	}

	/**
	 * Updates the action based on the passed time.
	 * <p>
	 * In this case, the update is simply delegated to the phase registry of this action.
	 * This way, the phases of this action are updated in the correct order: First, the camera moves to the target point.
	 * Then, the underlying action is executed. Finally, the camera moves back to the player.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}

	/**
	 * Ends the action by focusing the camera on the player.
	 * <p>
	 * This step must be executed to make the precise focus on the player after the camera has moved to the target point and back.
	 * Otherwise, the last call of the update method would not provide the exact focus.
	 */
	@Override
	public void onEnd() {
		cosmodogGame.getCam().focusOnPiece(cosmodogGame.getMap(), 0, 0, cosmodogGame.getPlayer());
	}

	/**
	 * States whether the action is finished.
	 * <p>
	 * This is the case when the last phase of the action has been unregistered and the phase registry is empty.
	 *
	 * @return true if the action has finished, false otherwise.
	 */
	@Override
	public boolean hasFinished() {
		return !actionPhaseRegistry.isActionRegistered(AsyncActionType.CUTSCENE);
	}
}
