package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.VariableLengthAsyncAction;

public abstract class PhaseBasedAction extends VariableLengthAsyncAction {

	private static final long serialVersionUID = 7995087870759665127L;

	private ActionRegistry actionPhaseRegistry = new ActionRegistry();
	
	public ActionRegistry getActionPhaseRegistry() {
		return actionPhaseRegistry;
	}
}
