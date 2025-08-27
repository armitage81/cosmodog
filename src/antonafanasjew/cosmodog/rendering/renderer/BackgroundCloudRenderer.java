
package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.decoration.BackgroundCloudsDecoration;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.particlepattern.model.Particle;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePattern;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BackgroundCloudRenderer extends AbstractRenderer {

	private final Image s;
	private final List<BackgroundCloudsDecoration> cloudsDecorations = new ArrayList<>();

	public BackgroundCloudRenderer(SpriteSheet cloudSpriteSheet) {
		s = cloudSpriteSheet.getSprite(0, 0).getScaledCopy(960, 540);
		s.setImageColor(1, 1, 1, 0.75f);
		cloudsDecorations.add(new BackgroundCloudsDecoration(960, 540, 3000));
		cloudsDecorations.add(new BackgroundCloudsDecoration(960, 540, 900));
	}
	
	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Cam cam = cosmodogGame.getCam();
		
		PlacedRectangle view = cam.viewCopy();
		view = PlacedRectangle.fromAnchorAndSize(0, 0, view.width(), view.height(), view.getMapType());

		for (BackgroundCloudsDecoration cloudsDecoration : cloudsDecorations) {
			ParticlePattern currentPattern = cloudsDecoration.particlePatternForPlaceAndTime(view);

			Set<Particle> particles = currentPattern.particlesSet();

			//Note: particles are defined related to the particle pattern, but we draw related to the cam size.
			//Hence we need to translate the difference first.

			Rectangle particlePatternSurface = cloudsDecoration.getParticlePatternSurface();
			particlePatternSurface = Rectangle.fromSize(particlePatternSurface.getWidth() * cam.getZoomFactor(), particlePatternSurface.getHeight() * cam.getZoomFactor());


			float centerOffsetX = -(particlePatternSurface.getWidth()) / 2f;
			float centerOffsetY = -(particlePatternSurface.getHeight()) / 2f;

			Vector particlePatternSurfaceOffsetRelatedToCam = new Vector(centerOffsetX, centerOffsetY);

			graphics.translate(particlePatternSurfaceOffsetRelatedToCam.getX(), particlePatternSurfaceOffsetRelatedToCam.getY());

			for (Particle particle : particles) {
				s.draw(particle.getOffset().getX() * cam.getZoomFactor(), particle.getOffset().getY() * cam.getZoomFactor(), s.getWidth() * cam.getZoomFactor(), s.getHeight() * cam.getZoomFactor());

			}

			graphics.translate(-particlePatternSurfaceOffsetRelatedToCam.getX(), -particlePatternSurfaceOffsetRelatedToCam.getY());

			graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		}
		
	}

}
