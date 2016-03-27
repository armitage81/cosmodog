package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Set;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Mark;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class MarkedTileRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		CosmodogMap cosmodogMap = cosmodogGame.getMap();
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
		Cam cam = cosmodogGame.getCam();
		Player player = cosmodogGame.getPlayer();
		
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
		
		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		
		Set<Piece> markedPieces = cosmodogMap.visibleMarkedTilePieces(tileNoX, tileNoY, tilesW, tilesH, 2);
		
		for (Piece piece : markedPieces) {
			
			Mark mark = (Mark)piece;
			
			if (mark.getMarkType().equals(Mark.FUEL_MARK_TYPE)) {
				applicationContext.getAnimations().get("markFuel").draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * (tileHeight));
			}
			
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
	}


}
