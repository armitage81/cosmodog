package antonafanasjew.cosmodog.rules.triggers;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.rules.AbstractRuleTrigger;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class GameProgressWinningConditionTrigger extends AbstractRuleTrigger {

	private static final long serialVersionUID = 5126924424709996224L;

	@Override
	public boolean accept(GameEvent event) {
		GameProgress gameProgress = ApplicationContextUtils.getGameProgress();
		return gameProgress.isWon();
	}
	
}
