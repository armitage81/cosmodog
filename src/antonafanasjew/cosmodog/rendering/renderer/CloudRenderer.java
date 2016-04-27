
package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SpriteSheet;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.topology.PlacedRectangle;

public class CloudRenderer extends AbstractRenderer {

	private SpriteSheet cloudSpriteSheet;
	
	public CloudRenderer(SpriteSheet cloudSpriteSheet) {
		this.cloudSpriteSheet = cloudSpriteSheet;
	}
	
	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		@SuppressWarnings("unchecked")
		List<PlacedRectangle> cloudsRectangles = (List<PlacedRectangle>)renderingParameter;
		
		
//		Leave this for test
//		List<PlacedRectangle> cloudsRectangles = Lists.newArrayList();
//		cloudsRectangles.add(PlacedRectangle.fromAnchorAndSize(0f, 0f, 240, 240));
//		cloudsRectangles.add(PlacedRectangle.fromAnchorAndSize(0f, 240f, 240, 240));
//		cloudsRectangles.add(PlacedRectangle.fromAnchorAndSize(240f, 0f, 240, 240));
//		cloudsRectangles.add(PlacedRectangle.fromAnchorAndSize(240f, 240f, 240, 240));
		
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Cam cam = cosmodogGame.getCam();
		

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();



		
		for (PlacedRectangle cloudRectangle : cloudsRectangles) {
			if (cloudRectangle.minX() * cam.getZoomFactor() >= camX + cam.viewCopy().width()) {
				continue;
			}
			if (cloudRectangle.minY() * cam.getZoomFactor() >= camY + cam.viewCopy().height()) {
				continue;
			}
			if (cloudRectangle.maxX() * cam.getZoomFactor() < camX) {
				continue;
			}
			if (cloudRectangle.maxY() * cam.getZoomFactor() < camY) {
				continue;
			}
			
			graphics.translate(cloudRectangle.centerX() * cam.getZoomFactor() - camX, cloudRectangle.centerY() * cam.getZoomFactor() - camY);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			cloudSpriteSheet.getSprite(0, 1).draw(-cloudRectangle.width() / 2, -cloudRectangle.height() / 2);
			cloudSpriteSheet.getSprite(0, 0).draw(-cloudRectangle.width() / 2, -cloudRectangle.height() / 2);
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-(cloudRectangle.centerX() * cam.getZoomFactor() - camX), -(cloudRectangle.centerY() * cam.getZoomFactor() - camY));
		}
		
	}

}
