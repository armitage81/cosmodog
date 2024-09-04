package antonafanasjew.cosmodog.actions.teleportation;

import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

/**
 * Represents a teleportation when the player enters a tile that teleports him to another tile.
 * <p>
 * The target of a teleportation is defined by a TilePolylineObject that represents the connection between the two tiles.
 * <p>
 * This is a phase-based action that consists of three phases: start, actual teleportation and end.
 * <p>
 * At the beginning, the player is blinking for a short time. Then he is transported to the target position.
 * The camera follows him. Finally, the player is blinking again for a short time at the target destination.
 * <p>
 * Take note: The player's movement listeners are triggered before and after the teleportation. The methods
 * triggered are similar to normal movement listener methods, but not exactly the same.
 * <p>
 * This class extends PhaseBasedAction and keeps its own action phase registry.
 */
public class TeleportationAction extends PhaseBasedAction {

	@Serial
	private static final long serialVersionUID = -2126668222640966302L;

	/**
	 * Represents the teleportation movement. Main information is the target position. It could be stored in a more
	 * simple way, the TiledPolylineObject is used to take the information directly from the map file.
	 */
	private final TiledPolylineObject teleportConnection;

	/**
	 * Creates a new teleportation action with the given teleport connection as TilePolylineObject.
	 *
	 * @param teleportConnection The teleport connection to use. Most interesting is the target position of the
	 *                           teleportation.
	 */
	public TeleportationAction(TiledPolylineObject teleportConnection) {
		this.teleportConnection = teleportConnection;
	}

	/**
	 * At the beginning of the action, the player's teleportation listener is triggered and then all three phases
	 * of the teleportation are registered in the local action phase registry.
	 */
	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		player.beginTeleportation();
		getActionPhaseRegistry().registerAction(AsyncActionType.TELEPORTATION, new TeleportStartActionPhase(1000));
		getActionPhaseRegistry().registerAction(AsyncActionType.TELEPORTATION, new ActualTeleportationActionPhase(1000, teleportConnection));
		getActionPhaseRegistry().registerAction(AsyncActionType.TELEPORTATION, new TeleportEndActionPhase(1000));
	}

	/**
	 * At the end of the action, the player's teleportation listener is triggered. At this point of time, the player
	 * is already at his target position.
	 */
	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endTeleportation();
	}

	/**
	 * The action ends when there are no phases with the type TELEPORTATION registered in the action phase registry.
	 *
	 * @return True if there are no phases with the type TELEPORTATION registered in the action phase registry. False otherwise.
	 */
	@Override
	public boolean hasFinished() {
		return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.TELEPORTATION);
	}
}