package antonafanasjew.cosmodog.rules;

import antonafanasjew.cosmodog.rules.events.GameEvent;

public abstract class AbstractRuleTrigger implements RuleTrigger {

	private static final long serialVersionUID = 8883155603927264682L;

	@Override
	public abstract boolean accept(GameEvent event);

}
