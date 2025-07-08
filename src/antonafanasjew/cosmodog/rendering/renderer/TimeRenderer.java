package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Locale;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class TimeRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE) == false) {
			return;
		}
		
		DrawingContext timeDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().timeDrawingContext();
		
		DrawingContext topDc = new TileDrawingContext(timeDrawingContext, 1, 2, 0, 0);
		DrawingContext bottomDc = new TileDrawingContext(timeDrawingContext, 1, 2, 0, 1);
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		String timeText = cosmodogGame.getPlanetaryCalendar().toTimeString(Locale.getDefault());

		FontRefToFontTypeMap fontType = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.HudValues);
		
		Book textBook;
		
		textBook = TextPageConstraints.fromDc(topDc).textToBook(timeText, fontType);
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);
		
		String text = "";
		if (cosmodogGame.getPlanetaryCalendar().isDay()) {
			text = "DAY";
		} else if (cosmodogGame.getPlanetaryCalendar().isMorning()) {
			text = "DAWN";
		} else if (cosmodogGame.getPlanetaryCalendar().isEvening()) {
			text = "DUSK";
		} else if (cosmodogGame.getPlanetaryCalendar().isNight()) {
			text = "NIGHT";
		}
		
		textBook = TextPageConstraints.fromDc(bottomDc).textToBook(text, fontType);
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, textBook);

	}

}
