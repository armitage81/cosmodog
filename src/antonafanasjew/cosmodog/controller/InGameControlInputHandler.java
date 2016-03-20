package antonafanasjew.cosmodog.controller;

import java.io.File;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.util.PathUtils;

/**
 * Handles meta commands in the game, like ctrl+s for "save" 
 */
public class InGameControlInputHandler extends AbstractInputHandler {

	@Override
	protected void handleInputInternal(GameContainer gc, StateBasedGame sbg, int delta, ApplicationContext applicationContext) {
		
		Input input = gc.getInput();
		
		Cosmodog cosmodog = applicationContext.getCosmodog();
		
		if (input.isKeyDown(Input.KEY_LCONTROL) && input.isKeyDown(Input.KEY_S)) {
			String fileName = null;
			boolean saveNow = false;
			if (input.isKeyPressed(Input.KEY_1)) {
				fileName = "1.sav";
				saveNow = true;
			} else if (input.isKeyPressed(Input.KEY_2)) {
				fileName = "2.sav";
				saveNow = true;
			}
			if (saveNow) {
				try {
					cosmodog.getGamePersistor().saveCosmodogGame(applicationContext.getCosmodog().getCosmodogGame(), PathUtils.gameSaveDir() + "/" + fileName);
				} catch (CosmodogPersistenceException e) {
					Log.error("Could not save game", e);
				}
			}
		}
		
		if (input.isKeyDown(Input.KEY_LCONTROL) && input.isKeyDown(Input.KEY_L)) {
			String fileName = null;
			boolean restoreNow = false;
			if (input.isKeyPressed(Input.KEY_1)) {
				fileName = "1.sav";
				restoreNow = true;
			} else if (input.isKeyPressed(Input.KEY_2)) {
				fileName = "2.sav";
				restoreNow = true;
			}
			if (restoreNow) {
				try {
					String filePath = PathUtils.gameSaveDir() + "/" + fileName;
					File f = new File(filePath);
					if (f.exists()) {
						applicationContext.getCosmodog().setCosmodogGame(cosmodog.getGamePersistor().restoreCosmodogGame(filePath));
					}
				} catch (CosmodogPersistenceException e) {
					Log.error("Could not restore game", e);
				}
			}
		}
		
		input.clearKeyPressedRecord();
		
	}

}
