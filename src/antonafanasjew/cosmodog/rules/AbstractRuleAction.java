package antonafanasjew.cosmodog.rules;

import antonafanasjew.cosmodog.rules.events.GameEvent;

public abstract class AbstractRuleAction implements RuleAction {

	private static final long serialVersionUID = -4815288131067573961L;
			@Override
	public abstract void execute(GameEvent event);

}
