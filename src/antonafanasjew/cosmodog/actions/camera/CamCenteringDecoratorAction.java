package antonafanasjew.cosmodog.actions.camera;

import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;

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
 *
 */
public class CamCenteringDecoratorAction  extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = 5171259029091809078L;

	/*
	* The duration of the camera movement to the target in milliseconds.
	* The same time will be spent on the movement back to the player.
	*/
	private final int camMovementDuration;

	/*
	 * The point in the world to which the center of the camera will be moved.
	 * It is given in unscaled world coordinates.
	 */
	private final Position camCenteringPosition;

	/*
	* This action will be executed after the camera moves to its target position, and before it moves back to the player.
	* This underlying action will be registered as a phase in the phase registry of the actual action.
	 */
	private final AsyncAction underlyingAsyncAction;

	/*
	 * The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	private final CosmodogGame cosmodogGame;

	/**
	 * Creates the action instance by defining where the camera should be pointed on the map,
	 * how long the camera movement should take,
	 * and what underlying action should be executed after the camera movement.
	 * The camera target is defined as a position of the focus tile (not a point in the world).
	 * The actual point of the camera focus in the world is calculated within this constructor from the tile position.
	 *
	 * @param camMovementDuration The duration of the camera movement to the target in milliseconds.
	 * The same time will be spent on the movement back to the player.
	 * @param position The tile position to which the center of the camera will be moved.
	 * @param underlyingAsyncAction Underlying action that will be executed after the camera moves to its target position, and before it moves back to the player.
	 * It will be registered as a phase in the phase registry of the actual action.
	 * @param cosmodogGame The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	public CamCenteringDecoratorAction(
			int camMovementDuration,
			Position position,
			AsyncAction underlyingAsyncAction,
			CosmodogGame cosmodogGame) {

		this.camMovementDuration = camMovementDuration;
		CosmodogMap map = cosmodogGame.getMaps().get(position.getMapType());
		float tileLength = TileUtils.tileLengthSupplier.get();
		this.camCenteringPosition = Position.fromCoordinates(
				position.getX() * tileLength + tileLength / 2,
				position.getY() * tileLength + tileLength / 2,
				position.getMapType()
		);
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
		this(camMovementDuration, piece.getPosition(), underlyingAsyncAction, cosmodogGame);

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
	public void onTriggerInternal() {
		float initialCamX = cosmodogGame.getCam().viewCopy().centerX() / cosmodogGame.getCam().getZoomFactor();
		float initialCamY = cosmodogGame.getCam().viewCopy().centerY() / cosmodogGame.getCam().getZoomFactor();

		Position initialCamPosition = Position.fromCoordinates(initialCamX, initialCamY, camCenteringPosition.getMapType());

		getPhaseRegistry().registerPhase(new CamMovementAction(camMovementDuration, camCenteringPosition, cosmodogGame));
		getPhaseRegistry().registerPhase(underlyingAsyncAction);
		getPhaseRegistry().registerPhase(new CamMovementAction(camMovementDuration, initialCamPosition, cosmodogGame));

	}

	/**
	 * Ends the action by focusing the camera on the player.
	 * <p>
	 * This step must be executed to make the precise focus on the player after the camera has moved to the target point and back.
	 * Otherwise, the last call of the update method would not provide the exact focus.
	 * <p>
	 * Note: With the introduction of the "map" dimension in a position, the camera movement can happen on a map different
	 * to the one where player is located. Example: Pressing a switch in a space station map, can open a door on the land map and
	 * the camera can go there on this different map to show the door opening. When returning back, the camera must switch
	 * to the player's map again. This happens in this method.
	 */
	@Override
	public void onEnd() {
		Player player = cosmodogGame.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		MapType mapType = player.getPosition().getMapType();
		cosmodogGame.getCam().focusOnPiece(cosmodogGame, 0, 0, player);
	}

}
