package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class GameLogRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		long referenceTime = System.currentTimeMillis();
		
		DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogDrawingContext();
		DrawingContext titleDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogHeaderDrawingContext();
		DrawingContext controlsDc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameLogControlsDrawingContext();
		
		Book openBook = ApplicationContextUtils.getCosmodogGame().getOpenBook();
		String openBookTitle = ApplicationContextUtils.getCosmodogGame().getOpenBookTitle();
		
		if (openBook != null) {
			
			ImageUtils.renderImage(gameContainer, graphics, "ui.ingame.gamelogframe", dc);
			
			FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.MainHeader);
			Book header = TextPageConstraints.fromDc(titleDc).textToBook(openBookTitle, fontRefToFontTypeMap);
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, header);
			
			TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, openBook);
			
			boolean renderHint = openBook.dynamicPageComplete(referenceTime);
			boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
			if (renderHint && renderBlinkingHint) {
				fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
				Book controlHint = TextPageConstraints.fromDc(controlsDc).textToBook("Press [ENTER]", fontRefToFontTypeMap);
				TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, controlHint);
			}
			
		}
		
		firstLoop = false;
	}

}
