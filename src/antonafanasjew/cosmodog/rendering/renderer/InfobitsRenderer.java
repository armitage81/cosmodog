package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Locale;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.base.Strings;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class InfobitsRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext dc, Object renderingParameter) {
		
//		graphics.setColor(Color.red);
//		graphics.fillRect(dc.x(), dc.y(), dc.w(), dc.h());
		
		//Offset to the right, because text is not centered properly for some reason. 
		dc = new SimpleDrawingContext(dc, 7, 0, dc.w() - 7, dc.h());
		
		Player p = ApplicationContextUtils.getPlayer();
		int infobits = p.getGameProgress().getInfobits();
		String infobitsText = String.valueOf(infobits);
		infobitsText = Strings.padStart(infobitsText, 4, '0');
		
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, dc, infobitsText, FontType.HudInfobits, 0);
		
		
		
		
	}

}
