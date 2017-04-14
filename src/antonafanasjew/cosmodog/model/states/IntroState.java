package antonafanasjew.cosmodog.model.states;

import java.util.Date;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
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
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer;
import antonafanasjew.cosmodog.rendering.renderer.LetterTextRenderer.LetterTextRenderingParameter;
import antonafanasjew.cosmodog.util.GameFlowUtils;

public class IntroState extends BasicGameState {

	private DrawingContext gameContainerDrawingContext;
	private DrawingContext topContainerDrawingContext;
	private DrawingContext centerContainerDrawingContext;
	private DrawingContext bottomContainerDrawingContext;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
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
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		
		Animation logo = ApplicationContext.instance().getAnimations().get("logo");
		DrawingContext dc = new CenteredDrawingContext(centerContainerDrawingContext, 640, 192);
		logo.draw(dc.x(), dc.y(), dc.w(), dc.h());
		
		Date date = new Date();
		long timestamp = date.getTime();
		timestamp = timestamp / 500;
		
		if (timestamp % 2 == 0) {
			LetterTextRenderingParameter param = LetterTextRenderingParameter.fromText("Press [Enter]");
			param.scaleFactor = 2.0f;
			param.horAlignment = LetterTextRenderingParameter.HOR_ALIGNMENT_CENTER;
			param.verAlignment = LetterTextRenderingParameter.VER_ALIGNMENT_CENTER;
			LetterTextRenderer.getInstance().render(gc, g, bottomContainerDrawingContext, param);
		}
		

	}

	@Override
	public int getID() {
		return CosmodogStarter.INTRO_STATE_ID;
	}

}
