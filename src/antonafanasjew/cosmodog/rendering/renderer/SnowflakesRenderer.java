package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.weather.SnowfallChangeAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.decoration.SnowflakesDecoration;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.particlepattern.model.Particle;
import antonafanasjew.particlepattern.model.ParticlePattern;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;
import java.util.Set;

public class SnowflakesRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {


		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();

		float snowfallChangeRate;

		SnowfallChangeAction snowfallChangeAction = (SnowfallChangeAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.SNOWFALL_CHANGE);
		if (snowfallChangeAction == null) {
			return;
		} else {
			snowfallChangeRate = snowfallChangeAction.getChangeRate();
		}

		SnowflakesDecoration snowflakeDecoration = SnowflakesDecoration.instance();
		
		/*
		 * 	This is complicated, so read carefully.
		 * 
		 *	When zooming, we actually increase the size of the scene while keeping the size of the cam and moving the cam to keep the ratio.
		 *	Example: Cam center is exactly in the middle of a 1000 * 1000 scene. Cam size is 100 * 100 and it's center is at 500/500. Now zoom x2. The scene is now 2000 * 2000. 
		 *  The cam size is still 100 * 100, but its center is now at 1000/1000.
		 *  
		 *  This is problematic for the offset calculator algorithm as it is based on the cam offset. In our example, the cam offset has changed because of the zoom, even if 
		 *  there was no change in terms of the offset calculator. Imagine a particle, like a tiny cloud, which is shown in the center of the screen. When zooming out, the cloud
		 *  still has to be there even if, technically, its offset should be recalculated because the cams offset is halfed now.
		 *  
		 *  Hence we cannot base the offset calculator on the cam offset. Instead, we are assuming the unzoomed offset. So in the example above, the offset calculator is still assuming
		 *  the cam offset of 500/500.
		 *  
		 *  
		 *  A second problem is positioning of particles related to the cam. Imagine two clouds close to each other. Now zoom in. The distance between the clouds will be bigger.
		 *  This is not handled by the offset calculator. We have to zoom the particle surface accordingly and recalculate the particle offsets related to the surface by adding the zoom factor.
		 *     
		 */
		
		PlacedRectangle view = cam.viewCopy();
		Vector newCenter = new Vector(view.centerX() / cam.getZoomFactor(), view.centerY() / cam.getZoomFactor());
		Vector newMin = newCenter.add(-view.width() / 2, -view.height() / 2);
		view = PlacedRectangle.fromAnchorAndSize(newMin.getX(), newMin.getY(), view.width(), view.height(), view.getMapType());
		
		List<ParticlePattern> currentPatterns = snowflakeDecoration.particlePatternsForPlaceAndTime(view);
		List<String> animationIds = snowflakeDecoration.getAnimationIds();

		for (int i = 0; i < currentPatterns.size(); i++) {
			ParticlePattern currentPattern = currentPatterns.get(i);
			Set<Particle> particles = currentPattern.particlesSet();

			//Note: particles are defined related to the particle pattern surface, but we draw related to the cam size.
			//Hence we need to translate the difference first.

			Rectangle particlePatternSurface = snowflakeDecoration.getParticlePatternSurface();
			particlePatternSurface = Rectangle.fromSize(particlePatternSurface.getWidth() * cam.getZoomFactor(), particlePatternSurface.getHeight() * cam.getZoomFactor());

			float centerOffsetX = -(particlePatternSurface.getWidth() - cam.viewCopy().width()) / 2f;
			float centerOffsetY = -(particlePatternSurface.getHeight() - cam.viewCopy().height()) / 2f;

			Vector particlePatternSurfaceOffsetRelatedToCam = new Vector(centerOffsetX, centerOffsetY);

			graphics.translate(particlePatternSurfaceOffsetRelatedToCam.getX(), particlePatternSurfaceOffsetRelatedToCam.getY());

			Animation a = applicationContext.getAnimations().get(animationIds.get(i));

			for (Particle particle : particles) {
				a.draw(particle.getOffset().getX() * cam.getZoomFactor(), particle.getOffset().getY() * cam.getZoomFactor(), a.getWidth() * cam.getZoomFactor(), a.getHeight() * cam.getZoomFactor(), new Color(1, 1, 1, snowfallChangeRate));
			}

			graphics.translate(-particlePatternSurfaceOffsetRelatedToCam.getX(), -particlePatternSurfaceOffsetRelatedToCam.getY());

			graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		}
	}
	

}
