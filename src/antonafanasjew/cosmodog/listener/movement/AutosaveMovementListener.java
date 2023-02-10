package antonafanasjew.cosmodog.listener.movement;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PathUtils;

public class AutosaveMovementListener extends MovementListenerAdapter {

	public static final int AUTOSAVE_INTERVAL_IN_TURNS = 20;
	
	private static final long serialVersionUID = -4664038701486172215L;

	
	
	@Override
	public void afterMovement(Actor actor, int x1, int y1, int x2, int y2, ApplicationContext applicationContext) {
		if (timeToAutosave()) {
			autosave();
		}
	}
	
	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
		if (timeToAutosave()) {
			autosave();
		}
	}
	
	private boolean timeToAutosave() {
		int turn = ApplicationContextUtils.getGameProgress().getTurn();
		return turn > 0 && turn % AUTOSAVE_INTERVAL_IN_TURNS == 0;
	}
	
	private void autosave() {
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		cosmodog.getGamePersistor().saveCosmodogGame(cosmodogGame, PathUtils.gameSaveDir() + "/" + cosmodogGame.getGameName() + ".sav");
	}
	
}
