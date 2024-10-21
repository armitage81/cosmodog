package antonafanasjew.cosmodog.util;

import java.util.Set;
import java.util.function.Supplier;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.structures.TileCoordinates;

import com.google.common.collect.Sets;

public class TileUtils {

	public static Supplier<Integer> tileLengthSupplier = () -> {
		return 16;
	};

	public static Set<TileCoordinates> getConnectedElements(int x, int y, int layerIndex, CosmodogMap map, com.google.common.base.Predicate<TileCoordinates> predicate) {
		Set<TileCoordinates> markedElements = Sets.newHashSet();
		Set<TileCoordinates> checkedElements = Sets.newHashSet();
		processElementAndNeighboursRecursively(new TileCoordinates(x, y, layerIndex), map, predicate, markedElements, checkedElements);
		return markedElements;
	}
	
	private static void processElementAndNeighboursRecursively(TileCoordinates e, CosmodogMap map, com.google.common.base.Predicate<TileCoordinates> predicate, Set<TileCoordinates> markedElements, Set<TileCoordinates> checkedElements) {

		checkedElements.add(e);

		if (predicate.apply(e)) {
			
			markedElements.add(e);
			
			int x = e.getX();
			int y = e.getY();
			
			if (x > 0 && !checkedElements.contains(e.westNeighbour())) {
				processElementAndNeighboursRecursively(e.westNeighbour(), map, predicate, markedElements, checkedElements);
			}
			
			if (x < map.getMapType().getWidth() - 1 && !checkedElements.contains(e.eastNeighbour())) {
				processElementAndNeighboursRecursively(e.eastNeighbour(), map, predicate, markedElements, checkedElements);
			}
			
			if (y > 0 && !checkedElements.contains(e.northNeighbour())) {
				processElementAndNeighboursRecursively(e.northNeighbour(), map, predicate, markedElements, checkedElements);
			}
			
			if (y < map.getMapType().getHeight() - 1 && !checkedElements.contains(e.southNeighbour())) {
				processElementAndNeighboursRecursively(e.southNeighbour(), map, predicate, markedElements, checkedElements);
			}
		}
	}
	
}
