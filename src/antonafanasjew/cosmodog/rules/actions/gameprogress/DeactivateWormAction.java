package antonafanasjew.cosmodog.rules.actions.gameprogress;

import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Deactivates the worm
 */
public class DeactivateWormAction extends AbstractRuleAction {

	private static final long serialVersionUID = 8620572862854412890L;

	@Override
	public void execute(GameEvent event) {			
		ApplicationContextUtils.getGameProgress().setWormActive(false);
	}

}
