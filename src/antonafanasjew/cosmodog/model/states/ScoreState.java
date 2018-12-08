package antonafanasjew.cosmodog.model.states;

import java.text.SimpleDateFormat;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.ScoreEntry;
import antonafanasjew.cosmodog.model.ScoreList;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import com.google.common.base.Strings;

public class ScoreState extends CosmodogAbstractState {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	public void everyEnter(GameContainer container, StateBasedGame game) throws SlickException {
		container.getInput().clearKeyPressedRecord();
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
		
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		gameContainerDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 800, 500);
		
		DrawingContext titleDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 0);
		DrawingContext recordsDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 1, 1, 5);
		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 7, 0, 6, 1, 1);
		
		TextBookRendererUtils.renderCenteredLabel(gc, g, titleDc, "RECORDS", FontType.RecordsTitleLabel, 0);
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		ScoreList scoreList = cosmodog.getScoreList();

		if (scoreList.isEmpty()) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, recordsDc, "No records stored yet.", FontType.Records, 0);
		} else {
			for (int i = 0; i < ScoreList.MAX_ELEMENTS; i++) {
				String text = "";
				if (i < scoreList.size()) {
					ScoreEntry scoreEntry = scoreList.get(i);
					text = SDF.format(scoreEntry.getDate())  + Strings.padStart("" + scoreEntry.getScore(), 15, '.') + " points.";
				}
				DrawingContext scoreEntryDc = new TileDrawingContext(recordsDc, 1, ScoreList.MAX_ELEMENTS, 0, i);
				scoreEntryDc = new CenteredDrawingContext(scoreEntryDc, 3);
				TextBookRendererUtils.renderCenteredLabel(gc, g, scoreEntryDc, text, FontType.Records, 0);
			}
		}
		
		boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
		if (renderBlinkingHint) {
			TextBookRendererUtils.renderCenteredLabel(gc, g, pressEnterTextDc, "Press [ENTER]", FontType.PopUpInterface, 0);
		}


	}

	@Override
	public int getID() {
		return CosmodogStarter.SCORE_STATE_ID;
	}

}
