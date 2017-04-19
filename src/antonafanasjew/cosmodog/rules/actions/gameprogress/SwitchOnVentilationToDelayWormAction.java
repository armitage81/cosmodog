package antonafanasjew.cosmodog.rules.actions.gameprogress;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

/**
 * Sets the worm appearance delay to phase 2.
 */
public class SwitchOnVentilationToDelayWormAction extends AbstractRuleAction {

	private static final long serialVersionUID = 8620572862854412890L;

	@Override
	public void execute(GameEvent event) {			
		ApplicationContextUtils.getGameProgress().setTurnsTillWormAppears(GameProgress.TURNS_TILL_WORM_APPEARS_PHASE2);
	}

}
