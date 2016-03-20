package antonafanasjew.cosmodog;

import org.apache.commons.logging.impl.Log4JLogger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.states.CreditsState;
import antonafanasjew.cosmodog.model.states.CutsceneState;
import antonafanasjew.cosmodog.model.states.DebugState;
import antonafanasjew.cosmodog.model.states.GameIntroState;
import antonafanasjew.cosmodog.model.states.GameMenuState;
import antonafanasjew.cosmodog.model.states.GameOverState;
import antonafanasjew.cosmodog.model.states.GameState;
import antonafanasjew.cosmodog.model.states.IntroState;
import antonafanasjew.cosmodog.model.states.MainMenuState;
import antonafanasjew.cosmodog.model.states.OutroState;
import antonafanasjew.cosmodog.model.states.ScoreState;

/**
 * The name is a bit misleading. This singleton is the state machine for the application. Its instance can be used
 * to change states of the application.
 */
public class CosmodogStarter extends StateBasedGame {

	public static final int GAME_STATE_ID = 0;
	public static final int GAME_OVER_STATE_ID = 1;
	public static final int CREDITS_STATE_ID = 2;
	public static final int CUTSCENE_STATE_ID = 3;
	public static final int GAME_MENU_STATE_ID = 4;
	public static final int INTRO_STATE_ID = 5;
	public static final int MAIN_MENU_STATE_ID = 6;
	public static final int SCORE_STATE_ID = 7;
	public static final int OUTRO_STATE_ID = 8;
	public static final int DEBUG_STATE_ID = 9;
	public static final int GAME_INTRO_STATE_ID = 10;

	public static final CosmodogStarter instance = new CosmodogStarter("Cosmodog");
	
	public static CosmodogStarter instance() {
		return instance;
	}

	public CosmodogStarter(String name) {
		super(name);
	}

	public static void main(String[] args) throws SlickException {

		Log.setVerbose(false);
		
		AppGameContainer gameContainer = new AppGameContainer(instance());
		gameContainer.setIcon("data/cosmodog_icon.png");
		gameContainer.setDisplayMode(Constants.RESOLUTION_WIDTH, Constants.RESOLUTION_HEIGHT, Constants.FULLSCREEN);
		gameContainer.setAlwaysRender(true);
		gameContainer.setTargetFrameRate(30);
		gameContainer.start();
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(new IntroState());
		this.addState(new MainMenuState());
		this.addState(new ScoreState());
		this.addState(new GameState());
		this.addState(new GameMenuState());
		this.addState(new GameOverState());
		this.addState(new CutsceneState());
		this.addState(new CreditsState());
		this.addState(new OutroState());
		this.addState(new DebugState());
		this.addState(new GameIntroState());
	}

}
