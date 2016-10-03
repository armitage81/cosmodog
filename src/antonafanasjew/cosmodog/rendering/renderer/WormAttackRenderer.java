package antonafanasjew.cosmodog.rendering.renderer;


import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction.WormAttackTransition;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class WormAttackRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		WormAttackAction wormAttackAction = (WormAttackAction)actionRegistry.getRegisteredAction(AsyncActionType.WORM_ATTACK);
		if (wormAttackAction == null) {
			return;
		}
		WormAttackTransition wormAttackTransition = wormAttackAction.getTransition();
		
		float wormHeightPercentage = wormAttackTransition.wormHeightPercentage();
		
		Animation wormAttackAnimation = applicationContext.getAnimations().get("wormAttack");
		
		
		
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();
		
		int tileWidth = tiledMap.getTileWidth();
		int tileHeight = tiledMap.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;


		float wormAttackOffsetX = -((wormAttackAnimation.getWidth() - tileWidth) / 2.0f);
		float wormAttackOffsetY = -(wormAttackAnimation.getHeight()) * wormHeightPercentage + tileHeight;
		
		Image wormImage = wormAttackAnimation.getCurrentFrame().getSubImage(0, 0, (int)wormAttackAnimation.getWidth(), (int)(wormHeightPercentage * wormAttackAnimation.getHeight()));
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		wormImage.draw((player.getPositionX() - tileNoX) * tileWidth + wormAttackOffsetX, (player.getPositionY() - tileNoY) * tileHeight + wormAttackOffsetY);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		
	}

}
