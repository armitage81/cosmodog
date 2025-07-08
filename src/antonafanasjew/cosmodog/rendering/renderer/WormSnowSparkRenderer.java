package antonafanasjew.cosmodog.rendering.renderer;


import antonafanasjew.cosmodog.topology.Vector;
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
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

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

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		Vector playerVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition(), camTilePosition);
		
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		applicationContext.getAnimations().get("wormAttackSnowSpark").draw(playerVectorRelatedToCam.getX() - tileLength, playerVectorRelatedToCam.getY());
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
