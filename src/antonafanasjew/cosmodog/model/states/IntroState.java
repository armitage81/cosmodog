package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.globals.Fonts;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.TextRenderer;
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
		
		TextRenderer tr = new TextRenderer(Fonts.DEFAULT_FONT, true);
		tr.render(gc, g, topContainerDrawingContext, "Armitage presents");
		
		tr.render(gc, g, centerContainerDrawingContext, "Cosmodog");
		
		tr.render(gc, g, bottomContainerDrawingContext, "Press [Enter]");

	}

	@Override
	public int getID() {
		return CosmodogStarter.INTRO_STATE_ID;
	}

}
