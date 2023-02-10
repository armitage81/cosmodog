package antonafanasjew.cosmodog.ingamemenu.options;



import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class OptionsRenderer implements Renderer {

	private static final String TEXT = "Press [RETURN] to save and quit";
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		DrawingContext inGameMenuContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().inGameMenuContentDrawingContext();
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
		
		Book textBook = TextPageConstraints.fromDc(inGameMenuContentDrawingContext).textToBook(TEXT, fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
	}

}
