package antonafanasjew.cosmodog.actions.fight;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;

import java.io.Serial;

public abstract class AbstractFightActionPhase extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -8572769280800088275L;

	public AbstractFightActionPhase(int duration) {
		super(duration);
	}

}
