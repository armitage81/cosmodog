package antonafanasjew.cosmodog.rules.actions;

import org.newdawn.slick.state.StateBasedGame;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.rules.AbstractRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEvent;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WinningAction extends AbstractRuleAction {

	private static final long serialVersionUID = 1L;

	private StateBasedGame sbg;
	
	public WinningAction(StateBasedGame sbg) {
		this.sbg = sbg;
	}

	@Override
	public void execute(GameEvent event) {
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		sbg.enterState(CosmodogStarter.OUTRO_STATE_ID);
		cosmodog.getGameLifeCycle().setStartNewGame(true);
		
	}

}
