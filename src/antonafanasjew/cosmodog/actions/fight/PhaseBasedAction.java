package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.generic.PhaseAndCompletionRateBasedTransition;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serial;

/**
 * Represents an asynchronous action that is based on phases.
 * <p>
 * It is a variable length asynchronous action.
 * <p>
 * This class is abstract.
 * <p>
 * This class holds common code for all actions that can have multiple phases, such as FightActions.
 */
public abstract class PhaseBasedAction extends VariableLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = 7995087870759665127L;

	/**
	 * The registry of action phases. They are asynchronous actions themselves.
	 * The PhasebasedAction uses them as sub-actions.
	 * <p>
	 * Example: A fight action can consist of multiple phases: first the player attacks, then the target enemy explodes,
	 * than the next enemy attacks etc.
	 */
	private final ActionRegistry actionPhaseRegistry = new ActionRegistry();

	/**
	 * Returns the registry of action phases for this action.
	 * The PhaseBasedAction ends when the phase registry for a specific action type is empty.
	 */
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
