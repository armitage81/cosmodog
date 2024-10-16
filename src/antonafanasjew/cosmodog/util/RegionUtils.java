package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.Block;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	
	public static boolean tileInRegion(Position position, TiledObject region, int tileWidth, int tileHeight) {
		//The actual piece implementation does not matter. We just need a type of a piece to satisfy the method signature.
		Piece piece = Block.create(position);
		PlacedRectangle playerTileRectangle = pieceRectangle(piece, tileWidth, tileHeight);
		return CollisionUtils.intersects(playerTileRectangle, region);
		
	}

	/**
	 * Indicates whether the player rectangle is covering the given position (not the tile position, but the geometrical position)
	 * @param piece Piece.
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
		int posX = (int)piece.getPosition().getX();
		int posY = (int)piece.getPosition().getY();
		
		int x = posX * tileWidth;
		int y = posY * tileHeight;

        return PlacedRectangle.fromAnchorAndSize(x, y, tileWidth, tileHeight);
	}

	public static Set<TiledObject> roofsOverPiece(Piece piece, CosmodogMap map) {

		Set<TiledObject> roofs = new HashSet<>();

		Map<String, TiledObject> roofRegions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_ROOFS).getObjects();

		for (TiledObject roofRegion : roofRegions.values()) {

			if (RegionUtils.pieceInRegion(piece, roofRegion, map.getTileWidth(), map.getTileHeight())) {
				roofs.add(roofRegion);
			}
		}

		return roofs;
	}

	public static Set<TiledObject> roofRemovalBlockersOverPiece(Piece piece, CosmodogMap map) {

		Set<TiledObject> roofRemovalBlockers = new HashSet<>();

		Map<String, TiledObject> regions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_ROOF_REMOVAL_BLOCKERS).getObjects();

		for (TiledObject region : regions.values()) {

			if (RegionUtils.pieceInRegion(piece, region, map.getTileWidth(), map.getTileHeight())) {
				roofRemovalBlockers.add(region);
			}
		}

		return roofRemovalBlockers;

	}

}
