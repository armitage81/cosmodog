package antonafanasjew.cosmodog.util;

import java.util.Set;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.structures.TileCoordinates;

import com.google.common.collect.Sets;

public class TileUtils {

	public static Set<TileCoordinates> getConnectedElements(int x, int y, int layerIndex, CustomTiledMap tiledMap, com.google.common.base.Predicate<TileCoordinates> predicate) {
		Set<TileCoordinates> markedElements = Sets.newHashSet();
		Set<TileCoordinates> checkedElements = Sets.newHashSet();
		processElementAndNeighboursRecursively(new TileCoordinates(x, y, layerIndex), tiledMap, predicate, markedElements, checkedElements);
		return markedElements;
	}
	
	private static void processElementAndNeighboursRecursively(TileCoordinates e, CustomTiledMap tiledMap, com.google.common.base.Predicate<TileCoordinates> predicate, Set<TileCoordinates> markedElements, Set<TileCoordinates> checkedElements) {

		checkedElements.add(e);

		if (predicate.apply(e)) {
			
			markedElements.add(e);
			
			int x = e.getX();
			int y = e.getY();
			
			if (x > 0 && !checkedElements.contains(e.westNeighbour())) {
				processElementAndNeighboursRecursively(e.westNeighbour(), tiledMap, predicate, markedElements, checkedElements);
			}
			
			if (x < tiledMap.getWidth() - 1 && !checkedElements.contains(e.eastNeighbour())) {
				processElementAndNeighboursRecursively(e.eastNeighbour(), tiledMap, predicate, markedElements, checkedElements);
			}
			
			if (y > 0 && !checkedElements.contains(e.northNeighbour())) {
				processElementAndNeighboursRecursively(e.northNeighbour(), tiledMap, predicate, markedElements, checkedElements);
			}
			
			if (y < tiledMap.getHeight() - 1 && !checkedElements.contains(e.southNeighbour())) {
				processElementAndNeighboursRecursively(e.southNeighbour(), tiledMap, predicate, markedElements, checkedElements);
			}
		}
	}
	
}
