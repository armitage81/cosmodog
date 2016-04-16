package antonafanasjew.cosmodog.util;

import java.util.Iterator;
import java.util.Set;

import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;

public class PiecesUtils {

	/**
	 * Usage example: Found a boat that is unique in inventory, so remove all remaining boat items from the map.
	 */
	public static void removeAllCollectibleItems(ToolType toolType, CosmodogMap map) {
		Set<Piece> pieces = map.getMapPieces();
		Iterator<Piece> it = pieces.iterator();
		while (it.hasNext()) {
			Piece piece = it.next();
			if (piece instanceof CollectibleTool) {
				CollectibleTool item = (CollectibleTool)piece;
				if (item.getToolType().equals(toolType)) {
					it.remove();
				}
			}
		}
		
	}
	
}
