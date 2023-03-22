package antonafanasjew.cosmodog.rules.triggers.logical;

import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;

public class TrueTrigger  extends AbstractRuleTrigger {

	private static final long serialVersionUID = -7512064075456353990L;

	@Override
	public boolean accept(GameEvent event) {
		return true;
	}

}
