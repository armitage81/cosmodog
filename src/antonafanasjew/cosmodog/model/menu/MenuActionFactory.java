package antonafanasjew.cosmodog.model.menu;

import java.io.File;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.filesystem.CosmodogPersistenceException;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.statetransitions.LoadingTransition;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.PathUtils;

public class MenuActionFactory {

	
	public static MenuAction getStartNewGameMenuAction(int no) {
		return new MenuAction() {
			@Override
			public void execute(StateBasedGame sbg) {
				ApplicationContextUtils.getCosmodog().getGameLifeCycle().setStartNewGame(true);
				ApplicationContextUtils.getCosmodog().getGameLifeCycle().setGameName(String.valueOf(no));
				sbg.enterState(CosmodogStarter.GAME_INTRO_STATE_ID, new FadeOutTransition(), new FadeInTransition());				
			}
		};
	}
	
	public static MenuAction getLoadSavedGameMenuAction(int no) {
		return new MenuAction() {
			@Override
			public void execute(StateBasedGame sbg) {
				try {
					String filePath = PathUtils.gameSaveDir() + "/" + no + ".sav";
					File f = new File(filePath);

					if (f.exists()) {
						ApplicationContext appCx = ApplicationContext.instance();
						CosmodogGame cosmodogGame = appCx.getCosmodog().getGamePersistor().restoreCosmodogGame(filePath);
						appCx.getCosmodog().getGameLifeCycle().setStartNewGame(false);
						appCx.getCosmodog().setCosmodogGame(cosmodogGame);
						sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
					}
				} catch (CosmodogPersistenceException e) {
					Log.error("Could not restore game", e);
				}
			}
		};
	}
	
	public static MenuAction getShowRecordsMenuAction() {
		return new MenuAction() {
			
			@Override
			public void execute(StateBasedGame sbg) {
				sbg.enterState(CosmodogStarter.SCORE_STATE_ID, new FadeOutTransition(), new FadeInTransition());				
			}
		};
	}
	
	public static MenuAction getQuitGameMenuAction() {
		return new MenuAction() {
			
			@Override
			public void execute(StateBasedGame sbg) {
				System.exit(0);				
			}
		};
	}
}
