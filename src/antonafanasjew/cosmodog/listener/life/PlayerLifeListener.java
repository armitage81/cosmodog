package antonafanasjew.cosmodog.listener.life;

import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class PlayerLifeListener implements LifeListener {

	@Override
	public void onLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		if (((Player)actorAfterLifeChange).dead()) {
			Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
			cosmodog.getGameLifeCycle().setStartNewGame(true);
			CosmodogStarter.instance.enterState(CosmodogStarter.GAME_OVER_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public void onMaxLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		
	}

}
