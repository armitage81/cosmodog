package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class DayTimeFilterRenderer implements Renderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		PlanetaryCalendar calendar = ApplicationContext.instance().getCosmodog().getCosmodogGame().getPlanetaryCalendar();
		
		Color filterColor = new Color(0f,0f,0f,0f);
		
		if (calendar.isEvening()) {
			filterColor = new Color(1f,0f, 0f, 0.2f);
		} else if (calendar.isNight()) {
			filterColor = new Color(0f,0f, 1f, 0.4f);
		} else if (calendar.isMorning()) {
			filterColor = new Color(1f,0f,0f,0.4f);
		}
		
		graphics.setColor(filterColor);
		graphics.fillRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
		
	}

}
