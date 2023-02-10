package antonafanasjew.cosmodog.model.states;

import java.text.SimpleDateFormat;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import com.google.common.base.Strings;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.ScoreEntry;
import antonafanasjew.cosmodog.model.ScoreList;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

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
		
		long referenceTime = System.currentTimeMillis();
		
		DrawingContext gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());
		
		DrawingContext textDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 800, 500);
		
		DrawingContext titleDc = new TileDrawingContext(textDrawingContext, 1, 7, 0, 0);
		DrawingContext recordsDc = new TileDrawingContext(textDrawingContext, 1, 7, 0, 1, 1, 5);
		DrawingContext pressEnterTextDc = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 9, 1, 1);
		
		FontRefToFontTypeMap fontTypeHeader = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.MainHeader);
		FontRefToFontTypeMap fontTypeInformational = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Informational);
		FontRefToFontTypeMap fontTypeControlsHint = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
		
		Book textBook;
		textBook = TextPageConstraints.fromDc(titleDc).textToBook("RECORDS", fontTypeHeader);
		TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
		
		Cosmodog cosmodog = ApplicationContext.instance().getCosmodog();
		ScoreList scoreList = cosmodog.getScoreList();

		if (scoreList.isEmpty()) {
			textBook = TextPageConstraints.fromDc(recordsDc).textToBook("No records stored yet.", fontTypeInformational);
			TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
		} else {
			for (int i = 0; i < ScoreList.MAX_ELEMENTS; i++) {
				String text = "";
				if (i < scoreList.size()) {
					ScoreEntry scoreEntry = scoreList.get(i);
					text = SDF.format(scoreEntry.getDate())  + Strings.padStart("" + scoreEntry.getScore(), 15, '.') + " points.";
				}
				DrawingContext scoreEntryDc = new TileDrawingContext(recordsDc, 1, ScoreList.MAX_ELEMENTS, 0, i);
				scoreEntryDc = new CenteredDrawingContext(scoreEntryDc, 3);
				textBook = TextPageConstraints.fromDc(scoreEntryDc).textToBook(text, fontTypeInformational);
				TextBookRendererUtils.renderCenteredLabel(gc, g, textBook);
			}
		}
		
		boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
		if (renderBlinkingHint) {
			Book controlHint = TextPageConstraints.fromDc(pressEnterTextDc).textToBook("Press [ENTER]", fontTypeControlsHint);
			TextBookRendererUtils.renderCenteredLabel(gc, g, controlHint);
		}


	}

	@Override
	public int getID() {
		return CosmodogStarter.SCORE_STATE_ID;
	}

}
