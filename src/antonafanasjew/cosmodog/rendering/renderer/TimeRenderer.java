package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Locale;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class TimeRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext dc, Object renderingParameter) {
		
		DrawingContext topDc = new TileDrawingContext(dc, 1, 2, 0, 0);
		DrawingContext bottomDc = new TileDrawingContext(dc, 1, 2, 0, 1);
		
		long timestamp = System.currentTimeMillis();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		String timeText = cosmodogGame.getPlanetaryCalendar().toTimeString(Locale.getDefault());

		String[] timeTextParts = timeText.split(":");
		
		boolean flic = (timestamp / 500 % 2 == 0);
		
		topDc = new CenteredDrawingContext(topDc, 5);
		
		DrawingContext minutesDc = new TileDrawingContext(topDc, 5, 1, 0, 0, 2, 1);
		DrawingContext colonDc = new TileDrawingContext(topDc, 5, 1, 2, 0, 1, 1);
		DrawingContext secondsDc = new TileDrawingContext(topDc, 5, 1, 3, 0, 2, 1);
		
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, minutesDc, timeTextParts[0], FontType.HudTime, 0);
		if (flic) {
			TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, colonDc, ":", FontType.HudTime, 0);
		}
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, secondsDc, timeTextParts[1], FontType.HudTime, 0);
		
		
		
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
		
		
		TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, bottomDc, text, FontType.HudDayTime, 0);
		
		
		
		
	}

}
