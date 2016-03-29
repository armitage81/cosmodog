package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;

public class TeleportationAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = -2126668222640966302L;
	
	private ActionRegistry actionPhaseRegistry = new ActionRegistry();

	private TiledPolylineObject teleportConnection;
	
	public TeleportationAction(TiledPolylineObject teleportConnection) {
		this.teleportConnection = teleportConnection;
	}
	
	@Override
	public void onTrigger() {
		actionPhaseRegistry.registerAction(AsyncActionType.TELEPORTATION, new TeleportStartActionPhase(1000));
		actionPhaseRegistry.registerAction(AsyncActionType.TELEPORTATION, new ActualTeleportationActionPhase(1000, teleportConnection));
		actionPhaseRegistry.registerAction(AsyncActionType.TELEPORTATION, new TeleportEndActionPhase(1000));
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}
	
	@Override
	public boolean hasFinished() {
		return !actionPhaseRegistry.isActionRegistered(AsyncActionType.TELEPORTATION);
	}

}
