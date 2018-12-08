package antonafanasjew.cosmodog.rendering.renderer;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction.WormAttackTransition;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WormSnowSparkRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		WormAttackAction wormAttackAction = (WormAttackAction)actionRegistry.getRegisteredAction(AsyncActionType.WORM_ATTACK);
		if (wormAttackAction == null) {
			return;
		}
		
		WormAttackTransition wormAttackTransition = wormAttackAction.getTransition();
		
		if (wormAttackTransition.percentage < 0.2f) {
			return;
		}
		
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

		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		applicationContext.getAnimations().get("wormAttackSnowSpark").draw((player.getPositionX() - tileNoX) * tileWidth - 1f * tileWidth, (player.getPositionY() - tileNoY) * tileHeight);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
