package antonafanasjew.cosmodog.listener.life;

import java.util.List;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.death.DyingAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public class PlayerLifeListener implements LifeListener {

	private static final long serialVersionUID = -155138251982746987L;
	
	@Override
	public void onLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		if (((Player)actorAfterLifeChange).dead()) {
			Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
			cosmodog.getGameLifeCycle().setStartNewGame(true);
			
			List<String> dyingHints = ApplicationContext.instance().getDyingHints();
			int dyingHintIndex = (int)(System.currentTimeMillis() % dyingHints.size());
			String dyingHint = dyingHints.get(dyingHintIndex);
			
			ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.DYING, new DyingAction(5000, dyingHint, Constants.DEFAULT_RESPAWN_POSITION));
			MusicUtils.playMusic(MusicResources.MUSIC_GAME_OVER);
		}
	}

	@Override
	public void onMaxLifeChange(Actor actorAfterLifeChange, int lifeBeforeChange, int lifeAfterChange) {
		
	}

}
