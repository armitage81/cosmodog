package antonafanasjew.cosmodog.actions.wait;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

public class WaitAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -7371591553509263521L;

	public WaitAction(int duration) {
		super(duration);
	}

	
}
