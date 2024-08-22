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

public class TeleportationAction extends PhaseBasedAction {

	private static final long serialVersionUID = -2126668222640966302L;
	
	private TiledPolylineObject teleportConnection;
	
	public TeleportationAction(TiledPolylineObject teleportConnection) {
		this.teleportConnection = teleportConnection;
	}
	
	@Override
	public void onTrigger() {
		Player player = ApplicationContextUtils.getPlayer();
		player.beginTeleportation();
		getActionPhaseRegistry().registerAction(AsyncActionType.TELEPORTATION, new TeleportStartActionPhase(1000));
		getActionPhaseRegistry().registerAction(AsyncActionType.TELEPORTATION, new ActualTeleportationActionPhase(1000, teleportConnection));
		getActionPhaseRegistry().registerAction(AsyncActionType.TELEPORTATION, new TeleportEndActionPhase(1000));
	}
	
	@Override
	public void onEnd() {
		Player player = ApplicationContextUtils.getPlayer();
		player.endTeleportation();
	}
	
	@Override
	public boolean hasFinished() {
		return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.TELEPORTATION);
	}

}
