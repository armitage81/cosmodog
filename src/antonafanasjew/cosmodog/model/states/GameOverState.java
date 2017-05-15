package antonafanasjew.cosmodog.model.states;

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
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.globals.Fonts;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.TextRenderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.GameFlowUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GameOverState extends BasicGameState {

	private DrawingContext gameContainerDrawingContext;
	private DrawingContext centerContainerDrawingContext;
	
	private DrawingContext centerContainerRow1DrawingContext;
	private DrawingContext centerContainerRow2DrawingContext;
	
	private DrawingContext bottomContainerDrawingContext;
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
		GameFlowUtils.updateScoreList();
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		centerContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		
		centerContainerRow1DrawingContext = new TileDrawingContext(centerContainerDrawingContext, 1, 5, 0, 0, 1, 4);
		centerContainerRow2DrawingContext = new TileDrawingContext(centerContainerDrawingContext, 1, 5, 0, 4, 1, 1);
		
		bottomContainerDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			sbg.enterState(CosmodogStarter.MAIN_MENU_STATE_ID, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		TextBookRendererUtils.renderCenteredLabel(gc, g, centerContainerRow1DrawingContext, "GAME OVER", FontType.GameOver, 0);
		
		Player player = ApplicationContextUtils.getCosmodogGame().getPlayer();
		long score = player.getGameProgress().getGameScore();
		
		TextBookRendererUtils.renderCenteredLabel(gc, g, centerContainerRow2DrawingContext, "Your score: " + score, FontType.GameOverScore, 0);
		
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, bottomContainerDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
		}
		
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_OVER_STATE_ID;
	}

}
