package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.PhaseRegistry;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serial;

public abstract class PhaseBasedAction extends VariableLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = 7995087870759665127L;

	private final PhaseRegistry phaseRegistry = new PhaseRegistry();

	public PhaseRegistry getPhaseRegistry() {
		return phaseRegistry;
	}

	@Override
	public final void onTrigger() {
		getPhaseRegistry().trigger();
		onTriggerInternal();
	}

	protected void onTriggerInternal() {

	}

	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		getPhaseRegistry().update(after - before, gc, sbg);
	}

	@Override
	public boolean hasFinished() {
		return getPhaseRegistry().hasFinished();
	}
}
