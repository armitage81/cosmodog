package antonafanasjew.cosmodog.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class ImageUtils {

	public static void renderImage(GameContainer gameContainer, Graphics g, String imageId, DrawingContext dc) {
		Image image = ApplicationContext.instance().getImages().get(imageId);
		renderImage(gameContainer, g, image, dc);
	}
	
	public static void renderImage(GameContainer gameContainer, Graphics g, Image image, DrawingContext dc) {
		g.drawImage(image, dc.x(), dc.y(), dc.x() + dc.w(), dc.y() + dc.h(), 0, 0, image.getWidth(), image.getHeight());
	}
	
	public static void renderImageRotated(GameContainer gameContainer, Graphics g, Image image, DrawingContext dc, float angle) {
		
		float degree = (float)(angle * 180 / Math.PI);
		
		image.setCenterOfRotation(dc.w() / 2, dc.h() / 2);
		image.rotate(degree);
		image.draw(dc.x(), dc.y(), dc.x() + dc.w(), dc.y() + dc.h(), 0, 0, image.getWidth(), image.getHeight());
		image.rotate(-degree);
	}
	
}
