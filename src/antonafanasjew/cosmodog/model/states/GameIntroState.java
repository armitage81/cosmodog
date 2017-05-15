package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.statetransitions.LoadingTransition;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GameIntroState  extends BasicGameState {

	private static final String TEXT = "It's been three years since you were hired by the Archeological Institute of Vera as a pilot transporting the scientists to remote corners of the galaxy "
			+ "in search of a mysterious alien race."
			+ " <p> "
			+ "Finally, a hint of some ancient monolyths has brought you to Phaeton. This planet has a bad reputation among the colonists. "
			+ "Everyone knows Phaeton from the stories about insane rebellions, extinct settlements and mass suicides. "
			+ "Under these circumstances, The Star Union would never give a landing permission to a civilian so you don't even try. "
			+ "Archeologists are used to operate under semi-legal conditions."
			+ " <p> "
			+ "Upon reaching the Phaetons orbit, your ship Cosmodog is hit by a missile fired from an automated guardian satellite. "
			+ "You manage to reach the escape pod before the ship is destroyed."
			+ " <p> "
			+ "You land on the surface of the planet as a lone survivor. There is no way back. "
			+ " <p> "
			+ "Reveal the mystery around the aliens and escape the planet.";

	
	@Override
	public void enter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		MusicUtils.loopMusic(MusicResources.MUSIC_CUTSCENE);
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		gameContainerDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 800, 500);
		
		DrawingContext introTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0, 1, 6);
		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 6, 1, 1);
		
		TextBookRendererUtils.renderTextPage(gc, g, introTextDc, TEXT, FontType.IntroText, 0);
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, pressEnterTextDc, "Press [ENTER]", FontType.PopUpInterface, 0);
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_INTRO_STATE_ID;
	}

}
