package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.GameFlowUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public class IntroState extends CosmodogAbstractState {

	private DrawingContext gameContainerDrawingContext;
	private DrawingContext centerContainerDrawingContext;
	
	private long initialTimestamp;
	private long timestamp;
	private boolean skipped;
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		
		skipped = false;

		initialTimestamp = System.currentTimeMillis();
		container.getInput().clearKeyPressedRecord();
		GameFlowUtils.loadScoreList();
		MusicUtils.playMusic(MusicResources.MUSIC_LOGO);
	}
	
	@Override
	public void firstEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		centerContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		
		timestamp = System.currentTimeMillis();
		
		if (timestamp - initialTimestamp > 5000 || skipped) {
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}
		
	}

	
	
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		timestamp = System.currentTimeMillis();
		
		if (timestamp - initialTimestamp < 5000) {
			Animation logo = ApplicationContext.instance().getAnimations().get("trancescendent");
			DrawingContext dc = new CenteredDrawingContext(centerContainerDrawingContext, 640, 192);
			logo.draw(dc.x(), dc.y(), dc.w(), dc.h());
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.INTRO_STATE_ID;
	}

}
