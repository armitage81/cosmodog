package antonafanasjew.cosmodog.listener.life;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.dying.DyingAction;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class PlayerLifeListener implements LifeListener {

	private static final long serialVersionUID = -155138251982746987L;

	@Override
	public void onLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		if (((Player)actorAfterLifeChange).dead()) {
			Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
			cosmodog.getGameLifeCycle().setStartNewGame(true);
			ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.DYING, new DyingAction(5000));
		}
	}

	@Override
	public void onMaxLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		
	}

}
