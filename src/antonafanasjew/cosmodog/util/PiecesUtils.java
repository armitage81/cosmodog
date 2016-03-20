package antonafanasjew.cosmodog.util;

import java.util.Iterator;
import java.util.Set;

import antonafanasjew.cosmodog.model.CollectibleItem;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;

public class PiecesUtils {

	/**
	 * Usage example: Found a boat that is unique in inventory, so remove all remaining boat items from the map.
	 */
	public static void removeAllCollectibleItems(String itemType, CosmodogMap map) {
		Set<Piece> pieces = map.getMapPieces();
		Iterator<Piece> it = pieces.iterator();
		while (it.hasNext()) {
			Piece piece = it.next();
			if (piece instanceof CollectibleItem) {
				CollectibleItem item = (CollectibleItem)piece;
				if (item.getItemType().equals(itemType)) {
					it.remove();
				}
			}
		}
		
	}
	
}
