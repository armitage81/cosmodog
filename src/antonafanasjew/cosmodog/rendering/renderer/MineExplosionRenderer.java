package antonafanasjew.cosmodog.rendering.renderer;


import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction.MineExplosionTransition;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class MineExplosionRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		MineExplosionAction mineExplosionAction = (MineExplosionAction)actionRegistry.getRegisteredAction(AsyncActionType.MINE_EXPLOSION);
		if (mineExplosionAction == null) {
			return;
		}
		
		MineExplosionTransition mineExplosionTransition = mineExplosionAction.getTransition();
		
		
		Animation mineExplosionAnimation = applicationContext.getAnimations().get("explosion");
		
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();
		
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;


		float offsetX = -((mineExplosionAnimation.getWidth() - tileWidth) / 2.0f);
		float offsetY = -((mineExplosionAnimation.getHeight() - tileHeight) / 2.0f);
		
		float percentagePerFrame = 1f / mineExplosionAnimation.getFrameCount();
		int currentImageIndex = (int)(mineExplosionTransition.percentage / percentagePerFrame);
		
		Image image = mineExplosionAnimation.getImage(currentImageIndex);
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		image.draw((player.getPositionX() - tileNoX) * tileWidth + offsetX, (player.getPositionY() - tileNoY) * tileHeight + offsetY);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		
	}

}
