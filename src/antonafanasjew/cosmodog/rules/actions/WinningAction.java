package antonafanasjew.cosmodog.rules.actions;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.StateBasedGameHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WinningAction extends AbstractRuleAction {

	private static final long serialVersionUID = 1L;

	@Override
	public void execute(GameEvent event) {
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		StateBasedGameHolder.stateBasedGame.enterState(CosmodogStarter.OUTRO2_STATE_ID);
		cosmodog.getGameLifeCycle().setStartNewGame(true);
		
	}

}
