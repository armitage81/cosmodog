package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class DayTimeIconRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext dc, Object renderingParameter) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

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
		
		
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, dc, text, FontType.HudDayTime, 0);
	}

}
