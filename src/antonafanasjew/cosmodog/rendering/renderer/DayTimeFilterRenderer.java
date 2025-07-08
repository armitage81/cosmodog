package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class DayTimeFilterRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
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
		graphics.fillRect(sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h());
		
	}

}
