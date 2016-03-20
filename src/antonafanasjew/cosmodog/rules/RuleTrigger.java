package antonafanasjew.cosmodog.rules;

import java.io.Serializable;

import antonafanasjew.cosmodog.rules.events.GameEvent;


public interface RuleTrigger extends Serializable {
	
	boolean accept(GameEvent event);

}
