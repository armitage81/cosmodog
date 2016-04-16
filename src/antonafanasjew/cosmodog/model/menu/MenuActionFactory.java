package antonafanasjew.cosmodog.model.menu;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;

public class MenuActionFactory {

	
	public static MenuAction getStartNewGameMenuAction() {
		return new MenuAction() {
			
			@Override
			public void execute(StateBasedGame sbg) {
				sbg.enterState(CosmodogStarter.GAME_INTRO_STATE_ID, new FadeOutTransition(), new FadeInTransition());				
			}
		};
	}
	
	public static MenuAction getLoadSavedGameMenuAction() {
		return new MenuAction() {
			
			@Override
			public void execute(StateBasedGame sbg) {
				sbg.enterState(CosmodogStarter.GAME_STATE_ID, new FadeOutTransition(), new FadeInTransition());				
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
