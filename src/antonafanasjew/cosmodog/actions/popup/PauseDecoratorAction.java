package antonafanasjew.cosmodog.actions.popup;

import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;

import antonafanasjew.cosmodog.actions.AsyncAction;

import java.io.Serial;

/**
 * Represents a pause decorator action that wraps an underlying action with an optional pause before it and an optional
 * pause after it. That could be used, for instance, to create a dramatic delay before an important action.
 * <p>
 * This is a variable length action. It uses its local action registry to register the underlying action and the pauses
 * as phases. The action is considered finished when all phases have been executed.
 */
public class PauseDecoratorAction  extends PhaseBasedAction {

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
	public void onTriggerInternal() {
		getPhaseRegistry().registerPhase("waitingBeforeAction", new WaitAction(durationBefore));
		getPhaseRegistry().registerPhase("underlyingPhase", underlyingAsyncAction);
		getPhaseRegistry().registerPhase("waitingAfterAction", new WaitAction(durationAfter));
	}

}
