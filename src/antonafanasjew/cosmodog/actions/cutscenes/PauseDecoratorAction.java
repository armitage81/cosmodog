package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import antonafanasjew.cosmodog.actions.wait.WaitAction;

import java.io.Serial;

/**
 * Represents a pause decorator action that wraps an underlying action with an optional pause before it and an optional
 * pause after it. That could be used, for instance, to create a dramatic delay before an important action.
 * <p>
 * This is a variable length action. It uses its local action registry to register the underlying action and the pauses
 * as phases. The action is considered finished when all phases have been executed.
 */
public class PauseDecoratorAction  extends VariableLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = 5171259029091809078L;

	/**
	 * The duration of the pause before the underlying action is executed.
	 */
	private final int durationBefore;

	/**
	 * The duration of the pause after the underlying action is executed.
	 */
	private final int durationAfter;

	/**
	 * The underlying action that is executed in between the pauses.
	 */
	private final AsyncAction underlyingAsyncAction;

	/**
	 * The local action registry that is used to register the underlying action and the pauses as phases.
	 */
	private final ActionRegistry actionPhaseRegistry = new ActionRegistry();

	/**
	 * Creates a new pause decorator action with the specified durations and the underlying action.
	 *
	 * @param durationBefore the duration of the pause before the underlying action is executed (in milliseconds).
	 * @param durationAfter  the duration of the pause after the underlying action is executed (in milliseconds).
	 * @param underlyingAsyncAction the underlying action that is executed in between the pauses.
	 */
	public PauseDecoratorAction(int durationBefore, int durationAfter, AsyncAction underlyingAsyncAction) {
		this.durationBefore = durationBefore;
		this.durationAfter = durationAfter;
		this.underlyingAsyncAction = underlyingAsyncAction;
	}

	/**
	 * Initializes the execution of the action.
	 * Registers the underlying action and the pauses as phases in the local action registry so they can
	 * be executed in order.
	 */
	@Override
	public void onTrigger() {
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new WaitAction(durationBefore));
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, underlyingAsyncAction);
		actionPhaseRegistry.registerAction(AsyncActionType.CUTSCENE, new WaitAction(durationAfter));
	}

	/**
	 * Updates the action based on the passed time.
	 * <p>
	 * In this case, the update is simply delegated to the phase registry of this action.
	 * This way, the phases of this action are updated in the correct order: First, the first pause is being executed.
	 * Then, the underlying action is executed. Finally, the second pause is being executed.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		actionPhaseRegistry.update(after - before, gc, sbg);
	}

	/**
	 * States whether the action is finished.
	 * <p>
	 * This is the case when the last phase of the action has been unregistered and the phase registry is empty.
	 *
	 * @return true if the action has finished, false otherwise.
	 */
	@Override
	public boolean hasFinished() {
		return !actionPhaseRegistry.isActionRegistered(AsyncActionType.CUTSCENE);
	}
	
}
