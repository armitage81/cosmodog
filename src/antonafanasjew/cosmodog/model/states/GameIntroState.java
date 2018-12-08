package antonafanasjew.cosmodog.model.states;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
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

import com.google.common.collect.Lists;

public class GameIntroState  extends CosmodogAbstractState {
	
	private List<String> texts = Lists.newArrayList();
	private int page;

	
	@Override
	public void everyEnter(GameContainer gc, StateBasedGame sbg) throws SlickException {
		
		
		MusicUtils.loopMusic(MusicResources.MUSIC_CUTSCENE);
		String intro1 = ApplicationContext.instance().getGameTexts().get("intro1").getLogText();
		String intro2 = ApplicationContext.instance().getGameTexts().get("intro2").getLogText();
		String intro3 = ApplicationContext.instance().getGameTexts().get("intro3").getLogText();
		String intro4 = ApplicationContext.instance().getGameTexts().get("intro4").getLogText();
		String intro5 = ApplicationContext.instance().getGameTexts().get("intro5").getLogText();
		
		texts.clear();
		texts.add(intro1);
		texts.add(intro2);
		texts.add(intro3);
		texts.add(intro4);
		texts.add(intro5);
		
		page = 0;
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
				
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		gameContainerDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		
		DrawingContext introTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0, 1, 6);
		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 6, 1, 1);
		
		TextBookRendererUtils.renderTextPage(gc, g, introTextDc, texts.get(page), FontType.IntroText, 0);
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, pressEnterTextDc, "Press [ENTER]", FontType.PopUpInterface, 0);
		}
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
		
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_MENU_SELECT).play();
			if (page < texts.size() - 1) {
				page++;
			} else {
				sbg.enterState(CosmodogStarter.GAME_STATE_ID, new LoadingTransition(), new FadeInTransition());
			}
			
		}
		
	}

	@Override
	public int getID() {
		return CosmodogStarter.GAME_INTRO_STATE_ID;
	}

}
