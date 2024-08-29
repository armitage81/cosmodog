package integration_test;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.StateBasedGameHolder;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.ResolutionHolder;
import antonafanasjew.cosmodog.model.states.CreditsState;
import antonafanasjew.cosmodog.model.states.CutsceneState;
import antonafanasjew.cosmodog.model.states.DebugState;
import antonafanasjew.cosmodog.model.states.GameIntroState;
import antonafanasjew.cosmodog.model.states.GameMenuState;
import antonafanasjew.cosmodog.model.states.GameState;
import antonafanasjew.cosmodog.model.states.IntroState;
import antonafanasjew.cosmodog.model.states.MainMenuState;
import antonafanasjew.cosmodog.model.states.Outro2State;
import antonafanasjew.cosmodog.model.states.OutroState;
import antonafanasjew.cosmodog.model.states.ScoreState;
import antonafanasjew.cosmodog.model.states.SplashState;
import antonafanasjew.cosmodog.model.states.StatisticsState;
import antonafanasjew.cosmodog.model.states.TheEndState;

/**
 * The name is a bit misleading. This singleton is the state machine for the application. Its instance can be used
 * to change states of the application.
 */
public class TestStarter extends StateBasedGame {

	/**
	 * Game state.
	 */
	public static final int GAME_STATE_ID = 0;
	
	/**
	 * Credits state.
	 */
	public static final int CREDITS_STATE_ID = 2;
	
	/**
	 * Cut scene state.
	 */
	public static final int CUTSCENE_STATE_ID = 3;
	
	/**
	 * Game menu state.
	 */
	public static final int GAME_MENU_STATE_ID = 4;
	
	/**
	 * Intro state.
	 */
	public static final int INTRO_STATE_ID = 5;
	
	/**
	 * Main menu state.
	 */
	public static final int MAIN_MENU_STATE_ID = 6;
	
	/**
	 * Score state.
	 */
	public static final int SCORE_STATE_ID = 7;
	
	/**
	 * Outro state.
	 */
	public static final int OUTRO_STATE_ID = 8;
	public static final int OUTRO2_STATE_ID = 13;
	
	/**
	 * Debug state.
	 */
	public static final int DEBUG_STATE_ID = 9;
	
	/**
	 * Game intro state.
	 */
	public static final int GAME_INTRO_STATE_ID = 10;
	
	/**
	 * Post game statistics
	 */
	public static final int STATISTICS_STATE_ID = 11;
	
	public static final int SPLASH_STATE_ID = 12;
	
	public static final int THE_END_STATE_ID = 14;

	/**
	 * The singleton instance of this class.
	 */
	public static final TestStarter instance = new TestStarter("Cosmodog");
	
	/**
	 * Returns the singleton instance of this class.
	 * @return Singleton instance.
	 */
	public static TestStarter instance() {
		return instance;
	}

	/**
	 * Initializes the application with the given name.
	 * @param name Name of the application.
	 */
	public TestStarter(String name) {
		super(name);
	}

	/**
	 * Starts the game application.
	 * @param args Main arguments.
	 * @throws SlickException Wrapper exception of the Slick2D framework.
	 */
	public static void main(String[] args) throws SlickException {

		//Set up features for the test scenario.
		System.setProperty("cosmodog.fullscreen", "true");
		System.setProperty("cosmodog.mapFile", "finalMap.tmx");
		System.setProperty("cosmodog.features.music", "true");
		System.setProperty("cosmodog.features.godfists", "false");
		System.setProperty("cosmodog.features.logo", "false");
		System.setProperty("cosmodog.features.tutorial", "false");
		System.setProperty("cosmodog.features.story", "false");
		System.setProperty("cosmodog.features.cutscenes", "false");
		System.setProperty("cosmodog.features.debugger", "true");
		System.setProperty("cosmodog.features.fastrunning", "false");
		
		
		Log.setVerbose(true);
		
		try {
		     GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/arialichollow.ttf")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/frontageoutline.ttf")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/karmaticarcade.ttf")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/collegehalo.ttf")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/unlearn.ttf")));
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("data/fonts/academy.ttf")));
		} catch (IOException|FontFormatException e) {
		     throw new RuntimeException("Error loading font.", e);
		}
		
		StateBasedGameHolder.stateBasedGame = instance();
		AppGameContainer gameContainer = new AppGameContainer(instance());
		
		gameContainer.setIcon("data/cosmodog_icon.png");
		
		boolean fullScreen = Constants.FULLSCREEN;
		if (System.getProperty("cosmodog.fullscreen") != null) {
			fullScreen = Boolean.valueOf(System.getProperty("cosmodog.fullscreen"));
		}

		try {
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			DisplayMode preferred = null;
			
			String[] preferredResolutions = fullScreen ? Constants.PREFERRED_RESOLUTIONS : Constants.PREFERRED_RESOLUTIONS_WINDOW_MODE;
			
			for (String preferredRes : preferredResolutions) {
				if (preferred == null) {
					for (DisplayMode mode : modes) {
						String supportedMode = mode.getWidth() + "x" + mode.getHeight();
						if (supportedMode.equals(preferredRes)) {
							preferred = mode;
							break;
						}
					}				
				}
			}
			
			if (preferred == null) {
				preferred = modes[0];
			}

			ResolutionHolder.set(preferred.getWidth(), preferred.getHeight());
			DrawingContextProviderHolder.set(preferred.getWidth(), preferred.getHeight());
			
			
		} catch (LWJGLException e) {
			throw new RuntimeException("Error while obtaining supported display modes.", e);
		}
		
		gameContainer.setDisplayMode(ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight(), fullScreen);
		gameContainer.setAlwaysRender(true);
		gameContainer.setTargetFrameRate(60);
		gameContainer.setVSync(true);
		gameContainer.setShowFPS(false);
		if (fullScreen) {
			gameContainer.setMouseGrabbed(true);
		}
		
		gameContainer.start();
	}

	/**
	 * Initializes the game states by their IDs.
	 */
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.addState(new GameState());
		this.addState(new SplashState());
		this.addState(new IntroState());
		this.addState(new MainMenuState());
		this.addState(new ScoreState());
		this.addState(new GameMenuState());
		this.addState(new CutsceneState());
		this.addState(new CreditsState());
		this.addState(new OutroState());
		this.addState(new Outro2State());
		this.addState(new DebugState());
		this.addState(new GameIntroState());
		this.addState(new StatisticsState());
		this.addState(new TheEndState());
	}

}
