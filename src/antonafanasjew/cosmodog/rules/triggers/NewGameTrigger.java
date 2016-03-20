package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;

public class NewGameTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = -7325438979653214792L;

	@Override
	public boolean accept(GameEvent event) {
		return event instanceof GameEventNewGame;
	}

}
