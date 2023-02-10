package antonafanasjew.cosmodog.rules.triggers.logical;

import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;

public class InvertedTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 30517321826198481L;

	private RuleTrigger trigger;
	
	public static final RuleTrigger not(RuleTrigger trigger) {
		return new InvertedTrigger(trigger);
	}
	
	public InvertedTrigger(RuleTrigger trigger) {
		this.trigger = trigger;
	}

	@Override
	public boolean accept(GameEvent event) {
		return !trigger.accept(event);
	}

}
