package antonafanasjew.cosmodog.rules.triggers.logical;

import java.util.List;

import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;

import com.google.common.collect.Lists;

public class AndTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 30517321826198481L;

	private List<RuleTrigger> triggers = Lists.newArrayList();
	
	public static final RuleTrigger and(RuleTrigger... triggers) {
		return new AndTrigger(triggers);
	}
	
	public AndTrigger(RuleTrigger... triggers) {
		this.triggers.addAll(Lists.newArrayList(triggers));
	}

	@Override
	public boolean accept(GameEvent event) {
		for (RuleTrigger trigger : triggers) {
			if (trigger.accept(event) == false) {
				return false;
			}
		}
		return true;
	}

}
