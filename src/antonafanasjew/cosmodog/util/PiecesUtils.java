package antonafanasjew.cosmodog.util;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.topology.Position;

public class PiecesUtils {

	/**
	 * Usage example: Found a boat that is unique in inventory, so remove all remaining boat items from the map.
	 */
	public static void removeAllCollectibleItems(ToolType toolType, CosmodogMap map) {
		Set<Entry<Position, Piece>> pieces = map.getMapPieces().entrySet();
		Iterator<Entry<Position, Piece>> it = pieces.iterator();
		while (it.hasNext()) {
			Piece piece = it.next().getValue();
			if (piece instanceof CollectibleTool) {
				CollectibleTool item = (CollectibleTool)piece;
				if (item.getToolType().equals(toolType)) {
					it.remove();
				}
			}
		}
		
	}
	
	public static float distanceBetweenPieces(Piece piece1, Piece piece2) {
		Position p1 = Position.fromCoordinates(piece1.getPositionX(), piece1.getPositionY());
		Position p2 = Position.fromCoordinates(piece2.getPositionX(), piece2.getPositionY());
		return CosmodogMapUtils.distanceBetweenPositions(p1, p2);
	}
}
