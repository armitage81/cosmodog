package antonafanasjew.cosmodog.rendering.renderer;


import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.ActionRegistry;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.death.WormAttackAction;
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

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		WormAttackAction wormAttackAction = (WormAttackAction)actionRegistry.getRegisteredAction(AsyncActionType.WORM_ATTACK);
		if (wormAttackAction == null) {
			return;
		}
		

		if (wormAttackAction.getCompletionRate() < 0.2f) {
			return;
		}
		
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();
		
		int scaledTileLength = (int) (tileLength * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileLength));
		int y = -(int) ((camY % scaledTileLength));

		int tileNoX = camX / scaledTileLength;
		int tileNoY = camY / scaledTileLength;

		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		applicationContext.getAnimations().get("wormAttackSnowSpark").draw((player.getPosition().getX() - tileNoX) * tileLength - 1f * tileLength, (player.getPosition().getY() - tileNoY) * tileLength);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
