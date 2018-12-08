package antonafanasjew.cosmodog.model.states;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.GameFlowUtils;
import antonafanasjew.cosmodog.util.MusicUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import com.google.common.collect.Lists;

public class OutroState extends CosmodogAbstractState {

	private List<String> texts = Lists.newArrayList();
	private int page;
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		
		MusicUtils.loopMusic(MusicResources.MUSIC_CUTSCENE);
		
		container.getInput().clearKeyPressedRecord();
		GameFlowUtils.updateScoreList();
		
		page = 0;
		texts.clear();
		
		String outro1 = ApplicationContext.instance().getGameTexts().get("outro1").getLogText();
		String outro2 = ApplicationContext.instance().getGameTexts().get("outro2").getLogText();
		
		texts.clear();
		texts.add(outro1);
		texts.add(outro2);
				
		
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			
			if (page < texts.size() - 1) {
				page++;
			} else {
				sbg.enterState(CosmodogStarter.OUTRO2_STATE_ID, new FadeOutTransition(Color.white, 5000), new FadeInTransition());
			}
		}

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
		
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		gameContainerDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		
		DrawingContext outroTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0, 1, 6);
		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 6, 1, 1);
						
		TextBookRendererUtils.renderTextPage(gc, g, outroTextDc, texts.get(page), FontType.OutroText, 0);
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, pressEnterTextDc, "Press [ENTER]", FontType.PopUpInterface, 0);
		}

	}

	@Override
	public int getID() {
		return CosmodogStarter.OUTRO_STATE_ID;
	}

}
