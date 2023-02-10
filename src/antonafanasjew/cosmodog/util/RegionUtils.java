package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;

public class RegionUtils {

	/**
	 * Retrieves the player and the rectangle region object and returns if they intersect each other.
	 * Take note: the region coordinates are real pixel coordinates on map and are not bound to tiles.
	 * To calculate players real position, tile width and height from the tiled map are needed.
	 */
	public static final boolean pieceInRegion(Piece piece, TiledObject region, int tileWidth, int tileHeight) {
		PlacedRectangle playerTileRectangle = pieceRectangle(piece, tileWidth, tileHeight);
		
		return CollisionUtils.intersects(playerTileRectangle, region);
		
	}

	/**
	 * Indicates whether the player rectangle is covering the given position (not the tile position, but the geometrical position)
	 * @param player Player.
	 * @param position A position as x/y pair.
	 * @param tileWidth The width of all tiles.
	 * @param tileHeight The height of all tiles.
	 * @return true if the player frame is covering the given position, false otherwise.
	 */
	public static final boolean playerOnPosition(Piece piece, Position position, int tileWidth, int tileHeight) {
		PlacedRectangle playerTileRectangle = pieceRectangle(piece, tileWidth, tileHeight);
		return CollisionUtils.intersects(playerTileRectangle, position);
	}
	
	/*
	 * Returns the placed rectangle for the player figure.
	 */
	private static PlacedRectangle pieceRectangle(Piece piece, int tileWidth, int tileHeight) {
		int posX = piece.getPositionX();
		int posY = piece.getPositionY();
		
		int x = posX * tileWidth;
		int y = posY * tileHeight;
		
		PlacedRectangle playerTileRectangle = PlacedRectangle.fromAnchorAndSize(x, y, tileWidth, tileHeight);
		return playerTileRectangle;
	}
	
}
