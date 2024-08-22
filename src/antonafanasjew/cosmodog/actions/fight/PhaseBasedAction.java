package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public abstract class PhaseBasedAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = 7995087870759665127L;

	private ActionRegistry actionPhaseRegistry = new ActionRegistry();
	
	public ActionRegistry getActionPhaseRegistry() {
		return actionPhaseRegistry;
	}

	/**
	 * Updates the action based on the passed time.
	 * <p>
	 * In this case, the update is simply delegated to the phase registry of this action.
	 * This way, the phases of this action are updated in the correct order: f.i. first the player attacks,
	 * then the target enemy explodes, then another adjacent enemy attacks, then an artillery attacks etc.
W	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		getActionPhaseRegistry().update(after - before, gc, sbg);
	}
}
