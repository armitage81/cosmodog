package antonafanasjew.cosmodog.actions.teleportation;

import antonafanasjew.cosmodog.actions.camera.CamMovementUtils;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject;
import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class TeleportationAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -2126668222640966302L;

	private final TiledPolylineObject teleportConnection;

	public static class TeleportationState {
		public boolean beingTeleported;
		public boolean characterVisible;

		public TeleportationState() {
			this.beingTeleported = false;
			this.characterVisible = true;
		}
	}

	public TeleportationAction(TiledPolylineObject teleportConnection) {
		this.teleportConnection = teleportConnection;
		this.getProperties().put("state", new TeleportationState());
	}

	@Override
	public void onTriggerInternal() {
		Player player = ApplicationContextUtils.getPlayer();
		player.beginTeleportation();

		TiledLineObject.Point startPoint = teleportConnection.getPoints().get(0);
		TiledLineObject.Point endPoint = teleportConnection.getPoints().get(1);
		Position startPixelPosition = Position.fromCoordinates(startPoint.x, startPoint.y, player.getPosition().getMapType());
		Position targetPixelPosition = Position.fromCoordinates(endPoint.x, endPoint.y, player.getPosition().getMapType());

		int durationInMilliseconds = CamMovementUtils.movementDuration(CamMovementUtils.SPEED_FAST, startPixelPosition, targetPixelPosition);
		getPhaseRegistry().registerPhase("preparingTeleportation", new TeleportStartActionPhase(1000, this.getProperty("state")));

		//We could have used CamMovementActionWithConstantSpeed here, but we need to maintain the teleportation state for the renderer.
		getPhaseRegistry().registerPhase("teleporting", new ActualTeleportationActionPhase(durationInMilliseconds, teleportConnection, this.getProperty("state")));
		getPhaseRegistry().registerPhase("postprocessingTeleportation", new TeleportEndActionPhase(1000, this.getProperty("state")));
	}

	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endTeleportation();
	}

}