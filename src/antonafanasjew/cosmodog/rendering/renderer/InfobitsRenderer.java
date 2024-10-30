package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.base.Strings;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InfobitsRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext infobitsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().infobitsDrawingContext();
		
		if (!Features.getInstance().featureOn(Features.FEATURE_INTERFACE)) {
			return;
		}
		
		Player p = ApplicationContextUtils.getPlayer();
		int infobits = p.getGameProgress().getInfobits();
		String infobitsText = String.valueOf(infobits);
		infobitsText = Strings.padStart(infobitsText, 4, '0');
		FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.HudValues);
		Book infobitsBook = TextPageConstraints.fromDc(infobitsDrawingContext).textToBook(infobitsText, fontRefToFontTypeMap);
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, infobitsBook);
		
	}

}
