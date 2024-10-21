package antonafanasjew.cosmodog.rendering.renderer;


import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.ExplosionAction;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction.MineExplosionTransition;
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
		Object action = actionRegistry.getRegisteredAction(AsyncActionType.MINE_EXPLOSION);
				
		if (action == null) {
			return;
		}
		
		
		MineExplosionTransition explosionTransition;
		
		//Yes, it's dirty. Refactor it. Mine explosion action should just reuse explosion action.
		if (action instanceof MineExplosionAction) {
			explosionTransition = ((MineExplosionAction)action).getTransition();
		} else {
			explosionTransition = ((ExplosionAction)action).getTransition();
		}
		
		
		Animation mineExplosionAnimation = applicationContext.getAnimations().get("explosion");
		
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Position position = explosionTransition.position;

		Cam cam = cosmodogGame.getCam();
		
		int scaledTileWidth = (int) (tileLength * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileLength * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;


		float offsetX = -((mineExplosionAnimation.getWidth() - tileLength) / 2.0f);
		float offsetY = -((mineExplosionAnimation.getHeight() - tileLength) / 2.0f);
		
		float percentagePerFrame = 1f / mineExplosionAnimation.getFrameCount();
		int currentImageIndex = (int)(explosionTransition.percentage / percentagePerFrame);
		
		Image image = mineExplosionAnimation.getImage(currentImageIndex);
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		image.draw((position.getX() - tileNoX) * tileLength + offsetX, (position.getY() - tileNoY) * tileLength + offsetY);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
