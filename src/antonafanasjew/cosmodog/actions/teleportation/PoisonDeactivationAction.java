package antonafanasjew.cosmodog.actions.teleportation;

import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;

public class PoisonDeactivationAction extends PhaseBasedAction {

	private static final long serialVersionUID = -2126668222640966302L;
	
	private TiledPolylineObject connection;
	
	public PoisonDeactivationAction(TiledPolylineObject connection) {
		this.connection = connection;
	}
	
	@Override
	public void onTriggerInternal() {
		getPhaseRegistry().registerPhase(new PoisonDeactivationStartActionPhase(1000, connection));
		getPhaseRegistry().registerPhase(new ActualPoisonDeactivationActionPhase(1000, connection));
		getPhaseRegistry().registerPhase(new PoisonDeactivationEndActionPhase(300, connection));
	}
	
}
