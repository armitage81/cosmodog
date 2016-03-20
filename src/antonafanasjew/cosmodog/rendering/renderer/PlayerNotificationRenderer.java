package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.tiled.TiledMap;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.Fonts;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.notifications.Notification;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;

public class PlayerNotificationRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		TiledMap tiledMap = applicationContext.getTiledMap();
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

		
		float movementOffsetX = 0;
		float movementOffsetY = 0;
		
		ActorTransition playerTransition = cosmodogGame.getActorTransitionRegistry().get(player);
		
		if (playerTransition != null) {
			movementOffsetX = tileWidth * playerTransition.getTransitionalOffsetX();
			movementOffsetY = tileHeight * playerTransition.getTransitionalOffsetY();
		}
				
		graphics.translate(x, y); //Just the map  offset
		
		int playerTileNoXOnVisibleMap = player.getPositionX() - tileNoX;
		int playerPosXOnVisibleMap = playerTileNoXOnVisibleMap * tileWidth;
		float playerPosXOnVisibleMapInclMovement = playerPosXOnVisibleMap + movementOffsetX;
		float playerPosXOnVisibleMapInclMovementInclScale = playerPosXOnVisibleMapInclMovement * cam.getZoomFactor();
		
		int playerTileNoYOnVisibleMap = player.getPositionY() - tileNoY;
		int playerPosYOnVisibleMap = playerTileNoYOnVisibleMap * tileHeight;
		float playerPosYOnVisibleMapInclMovement = playerPosYOnVisibleMap + movementOffsetY;
		float playerPosYOnVisibleMapInclMovementInclScale = playerPosYOnVisibleMapInclMovement * cam.getZoomFactor();
		
		
		int notificationsSize = cosmodogGame.getPlayerStatusNotificationList().size();
		for (int i = 0; i < notificationsSize; i++) {
			Notification n = cosmodogGame.getPlayerStatusNotificationList().get(i);
			String text = n.getText();
			int textWidth = Fonts.HOVERTEXT_FONT.getWidth(text);
			float textPosX = playerPosXOnVisibleMapInclMovementInclScale;
			if (playerPosXOnVisibleMapInclMovementInclScale + textWidth > drawingContext.w()) {
				//Why is x substracted here? It has to do with thranslation around the drawing method, but what exactly?
				textPosX = drawingContext.w() - textWidth - x;  
			}
			float overheadMessageStartingY = playerPosYOnVisibleMapInclMovementInclScale - 25;
			float overheadMessageCurrentY = overheadMessageStartingY - 50 * n.durationInPercentage();
			Color color = i == (notificationsSize - 1) ? Color.white : new Color(1f, 1f, 1f, 0.5f);
			graphics.setColor(color);
			graphics.setFont(Fonts.HOVERTEXT_FONT);
			graphics.drawString(text, textPosX, overheadMessageCurrentY);
		}

		graphics.translate(-x, -y);
		
	}

}
