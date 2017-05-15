package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Locale;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class TimeRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext dc, Object renderingParameter) {

		long timestamp = System.currentTimeMillis();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		String timeText = cosmodogGame.getPlanetaryCalendar().toTimeString(Locale.getDefault());
		if (timestamp / 500 % 2 == 0) {
			timeText = timeText.replace(':', ' ');
		}
		
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, dc, timeText, FontType.HudTime, 0);
	}

}
