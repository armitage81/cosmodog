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

public class CamCenteringDecoratorAction  extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = 5171259029091809078L;

	private final int camMovementDuration;

	private final Position targetPosition;

	private final AsyncAction underlyingAsyncAction;

	/**
	 * Creates the action instance by defining where the camera should be pointed on the map,
	 * how long the camera movement should take, and what underlying action should be executed after the camera movement.
	 *
	 * @param camMovementDuration duration in milliseconds
	 * @param targetPosition target position in tiles, not pixels
	 * @param underlyingAsyncAction action that should be executed after the camera movement to its target and before returning back to the player.
	 */
	public CamCenteringDecoratorAction (int camMovementDuration, Position targetPosition, AsyncAction underlyingAsyncAction) {
		this.camMovementDuration = camMovementDuration;
		this.targetPosition = targetPosition;
		this.underlyingAsyncAction = underlyingAsyncAction;
	}

	/**
	 * See above. The only difference to the other constructor is that the target position is not given directly, but via a piece.
	 *
	 * @param camMovementDuration See other constructor.
	 * @param piece The piece on which the camera should be centered. The position of the piece is taken as the target position.
	 * @param underlyingAsyncAction See other constructor.
	 */
	public CamCenteringDecoratorAction (int camMovementDuration, Piece piece, AsyncAction underlyingAsyncAction) {
		this(camMovementDuration, piece.getPosition(), underlyingAsyncAction);
	}

	@Override
	public void onTriggerInternal() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		float tileLength = TileUtils.tileLengthSupplier.get();
		float initialCamX = cosmodogGame.getCam().viewCopy().centerX() / cosmodogGame.getCam().getZoomFactor();
		float initialCamY = cosmodogGame.getCam().viewCopy().centerY() / cosmodogGame.getCam().getZoomFactor();
		Position startPositionInPixels = Position.fromCoordinates(initialCamX, initialCamY, cosmodogGame.getCam().viewCopy().getMapType());
		Position targetPositionInPixels = targetPosition.scale(tileLength).translate(tileLength / 2.0f, tileLength / 2.0f);

		getPhaseRegistry().registerPhase("moveCamToTarget", new CamMovementAction(camMovementDuration, targetPositionInPixels));
		getPhaseRegistry().registerPhase("underlying", underlyingAsyncAction);
		getPhaseRegistry().registerPhase("moveCamBackToPlayer", new CamMovementAction(camMovementDuration, startPositionInPixels));

	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		cosmodogGame.getCam().focusOnPiece(cosmodogGame, 0, 0, player);
	}

}
