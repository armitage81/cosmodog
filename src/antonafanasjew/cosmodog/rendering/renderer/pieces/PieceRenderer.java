package antonafanasjew.cosmodog.rendering.renderer.pieces;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.Piece;


public interface PieceRenderer {

	/**
	 * Renders a piece on the visible map.
	 * @param applicationContext Application context.
	 * @param tileWidth The width of a tile in pixels.
	 * @param tileHeight The height of a tile in pixels.
	 * @param tileNoX First visible tile column on the screen.
	 * @param tileNoY First visible tile row on the screen.
	 * @param piece Piece to be rendered. Must be located on the visible part of the map.
	 */
	void renderPiece(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece);
	
}
