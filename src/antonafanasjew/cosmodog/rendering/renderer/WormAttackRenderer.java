package antonafanasjew.cosmodog.rendering.renderer;


import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

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

public class WormAttackRenderer extends AbstractRenderer {

	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		
		ActionRegistry actionRegistry = cosmodogGame.getActionRegistry();
		WormAttackAction wormAttackAction = (WormAttackAction)actionRegistry.getRegisteredAction(AsyncActionType.WORM_ATTACK);
		if (wormAttackAction == null) {
			return;
		}

		float wormHeightPercentage = wormAttackAction.wormHeightPercentage();
		
		Animation wormAttackAnimation = applicationContext.getAnimations().get("wormAttack");
		
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		Vector playerVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(player.getPosition(), camTilePosition);


		float wormAttackOffsetX = -((wormAttackAnimation.getWidth() - tileLength) / 2.0f);
		float wormAttackOffsetY = -(wormAttackAnimation.getHeight()) * wormHeightPercentage + tileLength;
		
		Image wormImage = wormAttackAnimation.getCurrentFrame().getSubImage(0, 0, (int)wormAttackAnimation.getWidth(), (int)(wormHeightPercentage * wormAttackAnimation.getHeight()));
		
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		wormImage.draw(playerVectorRelatedToCam.getX() + wormAttackOffsetX, playerVectorRelatedToCam.getY() + wormAttackOffsetY);
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
