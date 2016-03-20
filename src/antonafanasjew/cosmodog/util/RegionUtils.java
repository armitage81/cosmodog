package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;

public class RegionUtils {

	/**
	 * Retrieves the player and the rectangle region object and returns if they intersect each other.
	 * Take note: the region coordinates are real pixel coordinates on map and are not bound to tiles.
	 * To calculate players real position, tile width and height from the tiled map are needed.
	 */
	public static final boolean playerInRegion(Player player, TiledObject region, int tileWidth, int tileHeight) {
		int posX = player.getPositionX();
		int posY = player.getPositionY();
		
		int x = posX * tileWidth;
		int y = posY * tileHeight;
		
		PlacedRectangle playerTileRectangle = PlacedRectangle.fromAnchorAndSize(x, y, tileWidth, tileHeight);
		
		return CollisionUtils.intersects(playerTileRectangle, region);
		
	}
	
}
