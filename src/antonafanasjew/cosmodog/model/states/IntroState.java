package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.GameFlowUtils;

public class IntroState extends BasicGameState {

	private DrawingContext gameContainerDrawingContext;
	private DrawingContext topContainerDrawingContext;
	private DrawingContext centerContainerDrawingContext;
	private DrawingContext bottomContainerDrawingContext;
	
	private long initialTimestamp;
	private long timestamp;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		initialTimestamp = System.currentTimeMillis();
		container.getInput().clearKeyPressedRecord();
		GameFlowUtils.loadScoreList();
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		topContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 0);
		centerContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		bottomContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		
		timestamp = System.currentTimeMillis();
		
		if (timestamp - initialTimestamp > 3000) {
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}
		
	}

	
	
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		Animation logo = ApplicationContext.instance().getAnimations().get("trancescendent");
		DrawingContext dc = new CenteredDrawingContext(centerContainerDrawingContext, 640, 192);
		logo.draw(dc.x(), dc.y(), dc.w(), dc.h());
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.INTRO_STATE_ID;
	}

}
