package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;
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
	
	private TextBookRenderer r = new TextBookRenderer();
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		topContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 0);
		centerContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		bottomContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);
		
	}

	public int page = 0;
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		
		Input input = gc.getInput();
		
		boolean inputLeft = input.isKeyPressed(Input.KEY_LEFT); 
		boolean inputRight = input.isKeyPressed(Input.KEY_RIGHT);
		
		if (inputLeft) {
			page--;
		} 
		if (inputRight) {
			page++;
		}
		
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}
		
		input.clearControlPressedRecord();

	}

	
	
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
//		Animation logo = ApplicationContext.instance().getAnimations().get("logo");
//		DrawingContext dc = new CenteredDrawingContext(centerContainerDrawingContext, 640, 192);
//		logo.draw(dc.x(), dc.y(), dc.w(), dc.h());
//		
//		long timestamp = System.currentTimeMillis();
//		timestamp = timestamp / 500;
//		
//		if (timestamp % 2 == 0) {
//			LetterTextRenderingParameter param = LetterTextRenderingParameter.fromText("Press [Enter]");
//			param.scaleFactor = 2.0f;
//			param.horAlignment = LetterTextRenderingParameter.HOR_ALIGNMENT_CENTER;
//			param.verAlignment = LetterTextRenderingParameter.VER_ALIGNMENT_CENTER;
//			LetterTextRenderer.getInstance().render(gc, g, bottomContainerDrawingContext, param);
//		}
		
		SimpleDrawingContext dc = new SimpleDrawingContext(null, 50, 50, 500, 500);
		g.setColor(Color.gray);
		g.drawRect(dc.x(), dc.y(), dc.w(), dc.h());
		r.render(gc, g, dc, TextBookRendererParameter.instance("bla", FontType.GameLog, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_START, page));
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.INTRO_STATE_ID;
	}

}
