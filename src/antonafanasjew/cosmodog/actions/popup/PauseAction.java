package antonafanasjew.cosmodog.actions.popup;

import antonafanasjew.cosmodog.actions.AbstractAsyncAction;

public class PauseAction extends AbstractAsyncAction {

	private static final long serialVersionUID = 4542769916577259433L;
	
	private int timeout;

	public PauseAction(int millis) {
		this.timeout = millis;
	}
	
	@Override
	public boolean hasFinished() {
		return getPassedTime() >= timeout;
	}
	
	
	
}
