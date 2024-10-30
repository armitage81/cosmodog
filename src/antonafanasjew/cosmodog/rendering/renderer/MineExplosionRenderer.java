package antonafanasjew.cosmodog.rendering.renderer;


import antonafanasjew.cosmodog.actions.*;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MineExplosionRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		FixedLengthAsyncAction action = (FixedLengthAsyncAction) actionRegistry.getRegisteredAction(AsyncActionType.MINE_EXPLOSION);
				
		if (action == null) {
			return;
		}
		
		Position position = action.getProperty("position");
		float completionRate = action.getCompletionRate();

		Animation mineExplosionAnimation = applicationContext.getAnimations().get("explosion");
		
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		Vector positionVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(position, camTilePosition);

		float offsetX = -((mineExplosionAnimation.getWidth() - tileLength) / 2.0f);
		float offsetY = -((mineExplosionAnimation.getHeight() - tileLength) / 2.0f);
		
		float percentagePerFrame = 1f / mineExplosionAnimation.getFrameCount();
		int currentImageIndex = (int)(completionRate / percentagePerFrame);
		
		Image image = mineExplosionAnimation.getImage(currentImageIndex);
		
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		image.draw(positionVectorRelatedToCam.getX() + offsetX, positionVectorRelatedToCam.getY() + offsetY);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
