package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.model.Piece;
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
	public static boolean pieceInRegion(Piece piece, MapDescriptor regionMapDescriptor, TiledObject region) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		if (!regionMapDescriptor.equals(piece.getPosition().getMapDescriptor())) {
			return false;
		}

		PlacedRectangle playerTileRectangle = pieceRectangle(piece);
		
		return CollisionUtils.intersects(playerTileRectangle, regionMapDescriptor, region);
		
	}
	
	public static boolean tileInRegion(Position position, MapDescriptor mapDescriptor, TiledObject region) {
		//The actual piece implementation does not matter. We just need a type of a piece to satisfy the method signature.
		Piece piece = Block.create(position);
		PlacedRectangle playerTileRectangle = pieceRectangle(piece);
		return CollisionUtils.intersects(playerTileRectangle, mapDescriptor, region);
		
	}

	/**
	 * Indicates whether the player rectangle is covering the given position (not the tile position, but the geometrical position)
	 * @param player Player.
	 * @param position A position as x/y pair.
	 * @return true if the player frame is covering the given position, false otherwise.
	 */
	public static boolean playerOnPosition(Player player, Position position) {

		if (!player.getPosition().getMapDescriptor().equals(position.getMapDescriptor())) {
			return false;
		}

		PlacedRectangle playerTileRectangle = pieceRectangle(player);
		return CollisionUtils.intersects(playerTileRectangle, position);
	}
	
	/*
	 * Returns the placed rectangle for the player figure.
	 */
	private static PlacedRectangle pieceRectangle(Piece piece) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		int posX = (int)piece.getPosition().getX();
		int posY = (int)piece.getPosition().getY();
		
		int x = posX * tileLength;
		int y = posY * tileLength;

        return PlacedRectangle.fromAnchorAndSize(x, y, tileLength, tileLength, piece.getPosition().getMapDescriptor());
	}

	public static Set<TiledObject> roofsOverPiece(Piece piece, CosmodogMap map) {

		Set<TiledObject> roofs = new HashSet<>();

		Map<String, TiledObject> roofRegions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_ROOFS).getObjects();

		for (TiledObject roofRegion : roofRegions.values()) {

			if (RegionUtils.pieceInRegion(piece, map.getMapDescriptor(), roofRegion)) {
				roofs.add(roofRegion);
			}
		}

		return roofs;
	}

	public static Set<TiledObject> roofRemovalBlockersOverPiece(Piece piece, CosmodogMap map) {

		Set<TiledObject> roofRemovalBlockers = new HashSet<>();

		Map<String, TiledObject> regions = map.getObjectGroups().get(ObjectGroups.OBJECT_GROUP_ID_ROOF_REMOVAL_BLOCKERS).getObjects();

		for (TiledObject region : regions.values()) {

			if (RegionUtils.pieceInRegion(piece, map.getMapDescriptor(), region)) {
				roofRemovalBlockers.add(region);
			}
		}

		return roofRemovalBlockers;

	}

}
