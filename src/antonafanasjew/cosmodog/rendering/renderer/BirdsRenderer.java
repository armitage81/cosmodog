package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.Animations;
import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.decoration.BirdsDecoration;
import antonafanasjew.cosmodog.topology.Position;

public class BirdsRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics g, DrawingContext drawingContext, Object renderingParameter) {

		BirdsDecoration birdsDecoration = (BirdsDecoration)renderingParameter;
		
		Position position = birdsDecoration.getCurrentBirdsPosition();
		float flightHeightFactor = birdsDecoration.getFlightHeightFactor();

		if (position != null) {

			ApplicationContext applicationContext = ApplicationContext.instance();
			Cosmodog cosmodog = applicationContext.getCosmodog();
			CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

			float zoomFactor = cosmodogGame.getCam().getZoomFactor() * flightHeightFactor;

			Animation a = applicationContext.getAnimations().get("birds");

			float birdsX = position.getX();
			float birdsY = position.getY();
			float birdsXCenter = birdsX + a.getWidth() / 2;
			float birdsYCenter = birdsY + a.getHeight() / 2;

			// System.out.println("GC: " + gc.getWidth() + "/" +
			// gc.getHeight());
			// System.out.println("MC: " + mapDrawingContext.w() + "/" +
			// mapDrawingContext.h());
			// System.out.println("MC_OFFSET: " + mapDrawingContext.x() + "/" +
			// mapDrawingContext.y());
			// System.out.println("Birds: " + birdsX + "/" + birdsY);

			g.translate(birdsXCenter, birdsYCenter);
			g.scale(zoomFactor, zoomFactor);
			a.draw(-a.getWidth() / 2, -a.getHeight() / 2);
			g.scale(1 / zoomFactor, 1 / zoomFactor);
			g.translate(-birdsXCenter, -birdsYCenter);

		}
	}

}
