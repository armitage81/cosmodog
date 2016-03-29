package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;

public class RegionUtils {

	/**
	 * Retrieves the player and the rectangle region object and returns if they intersect each other.
	 * Take note: the region coordinates are real pixel coordinates on map and are not bound to tiles.
	 * To calculate players real position, tile width and height from the tiled map are needed.
	 */
	public static final boolean playerInRegion(Player player, TiledObject region, int tileWidth, int tileHeight) {
		PlacedRectangle playerTileRectangle = playerRectangle(player, tileWidth, tileHeight);
		
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
	public static final boolean playerOnPosition(Player player, Position position, int tileWidth, int tileHeight) {
		PlacedRectangle playerTileRectangle = playerRectangle(player, tileWidth, tileHeight);
		return CollisionUtils.intersects(playerTileRectangle, position);
	}
	
	/*
	 * Returns the placed rectangle for the player figure.
	 */
	private static PlacedRectangle playerRectangle(Player player, int tileWidth, int tileHeight) {
		int posX = player.getPositionX();
		int posY = player.getPositionY();
		
		int x = posX * tileWidth;
		int y = posY * tileHeight;
		
		PlacedRectangle playerTileRectangle = PlacedRectangle.fromAnchorAndSize(x, y, tileWidth, tileHeight);
		return playerTileRectangle;
	}
	
}
