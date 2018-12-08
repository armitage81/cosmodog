package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

import com.google.common.base.Strings;

public class InfobitsRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext infobitsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().infobitsDrawingContext();
		
		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE) == false) {
			return;
		}
		
		Player p = ApplicationContextUtils.getPlayer();
		int infobits = p.getGameProgress().getInfobits();
		String infobitsText = String.valueOf(infobits);
		infobitsText = Strings.padStart(infobitsText, 4, '0');
		
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, infobitsDrawingContext, infobitsText, FontType.HudInfobits, 0);
		
	}

}
