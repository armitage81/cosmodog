package antonafanasjew.cosmodog.ingamemenu.options;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.controller.InputHandler;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.util.PathUtils;

public class OptionsInputHandler implements InputHandler {
	@Override
	public void handleInput(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		if (gc.getInput().isKeyPressed(Input.KEY_RETURN)) {
			try {
				Cosmodog cosmodog = applicationContext.getCosmodog();
				CosmodogGame game = cosmodog.getCosmodogGame();
				cosmodog.getGamePersistor().saveCosmodogGame(game, PathUtils.gameSaveDir() + "/" + game.getGameName() + ".sav");
				CosmodogStarter.instance.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
			} catch (CosmodogPersistenceException e) {
				Log.error(e.getLocalizedMessage());
			}
		}
	}
}
