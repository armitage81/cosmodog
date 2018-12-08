
package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.decoration.CloudsDecoration;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.particlepattern.model.Particle;
import antonafanasjew.particlepattern.model.ParticlePattern;

public class CloudRenderer extends AbstractRenderer {

	private Image s1;
	private Image s2;
	
	public CloudRenderer(SpriteSheet cloudSpriteSheet) {
		s1 = cloudSpriteSheet.getSprite(0, 1);
		s2 = cloudSpriteSheet.getSprite(0, 0);

	}
	
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Cam cam = cosmodogGame.getCam();
		
		CloudsDecoration cloudsDecoration = CloudsDecoration.instance();
		
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
		view = PlacedRectangle.fromAnchorAndSize(newMin.getX(), newMin.getY(), view.width(), view.height());
		
		ParticlePattern currentPattern = cloudsDecoration.particlePatternForPlaceAndTime(view);

		Set<Particle> particles = currentPattern.particlesSet();
		
		//Note: particles are defined related to the particle pattern, but we draw related to the cam size. 
		//Hence we need to translate the difference first.
		
		Rectangle particlePatternSurface = cloudsDecoration.getParticlePatternSurface();
		particlePatternSurface = Rectangle.fromSize(particlePatternSurface.getWidth() * cam.getZoomFactor(), particlePatternSurface.getHeight() * cam.getZoomFactor());
		
		float centerOffsetX = -(particlePatternSurface.getWidth() - cam.viewCopy().width()) / 2f;
		float centerOffsetY = -(particlePatternSurface.getHeight() - cam.viewCopy().height()) / 2f;
		
		Vector particlePatternSurfaceOffsetRelatedToCam = new Vector(centerOffsetX, centerOffsetY);
		
		graphics.translate(particlePatternSurfaceOffsetRelatedToCam.getX(), particlePatternSurfaceOffsetRelatedToCam.getY());
		
		for (Particle particle : particles) {
			s1.draw(particle.getOffset().getX() * cam.getZoomFactor(), particle.getOffset().getY() * cam.getZoomFactor(), s1.getWidth() * cam.getZoomFactor(), s1.getHeight() * cam.getZoomFactor());
			s2.draw(particle.getOffset().getX() * cam.getZoomFactor(), particle.getOffset().getY() * cam.getZoomFactor(), s2.getWidth() * cam.getZoomFactor(), s2.getHeight() * cam.getZoomFactor());
			
		}
		
		graphics.translate(-particlePatternSurfaceOffsetRelatedToCam.getX(), -particlePatternSurfaceOffsetRelatedToCam.getY());
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

}
