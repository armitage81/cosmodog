package antonafanasjew.cosmodog.controller;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PathUtils;

/**
 * Handles meta commands in the game, like ctrl+s for "save" 
 */
public class InGameControlInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		
		Input input = gc.getInput();
		
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		if (cosmodogGame.getActionRegistry().inputBlocked()) {
			return;
		}
		
		if (input.isKeyDown(Input.KEY_LCONTROL) && input.isKeyPressed(Input.KEY_S)) {
			try {
				Player player = ApplicationContextUtils.getPlayer();
				CosmodogGame game = applicationContext.getCosmodog().getCosmodogGame();
				cosmodog.getGamePersistor().saveCosmodogGame(game, PathUtils.gameSaveDir() + "/" + game.getGameName() + ".sav");
				OverheadNotificationAction.registerOverheadNotification(player, "Game saved");
			} catch (CosmodogPersistenceException e) {
				Log.error("Could not save game", e);
				System.exit(1);
			}
		}
		
		if (input.isKeyDown(Input.KEY_LCONTROL) && input.isKeyPressed(Input.KEY_L)) {
			try {
				CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
				String filePath = PathUtils.gameSaveDir() + "/" + game.getGameName() + ".sav";
				File f = new File(filePath);
				if (f.exists()) {
					cosmodogGame = cosmodog.getGamePersistor().restoreCosmodogGame(filePath);
					applicationContext.getCosmodog().setCosmodogGame(cosmodogGame);
				}
			} catch (CosmodogPersistenceException e) {
				Log.error("Could not restore game", e);
				System.exit(1);
			}
		}
		
		input.clearKeyPressedRecord();
		
	}

}
