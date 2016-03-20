package antonafanasjew.cosmodog.rules;

import java.io.Serializable;

import antonafanasjew.cosmodog.rules.events.GameEvent;


public interface RuleAction extends Serializable {

	void execute(GameEvent event);
	
}
