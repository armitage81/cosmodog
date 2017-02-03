package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;

public class PoisonDeactivationAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = -2126668222640966302L;
	
	private ActionRegistry actionPhaseRegistry = new ActionRegistry();

	private TiledPolylineObject connection;
	
	public PoisonDeactivationAction(TiledPolylineObject connection) {
		this.connection = connection;
	}
	
	@Override
	public void onTrigger() {
		actionPhaseRegistry.registerAction(AsyncActionType.TELEPORTATION, new PoisonDeactivationStartActionPhase(1000, connection));
		actionPhaseRegistry.registerAction(AsyncActionType.TELEPORTATION, new ActualPoisonDeactivationActionPhase(1000, connection));
		actionPhaseRegistry.registerAction(AsyncActionType.TELEPORTATION, new PoisonDeactivationEndActionPhase(300, connection));
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
