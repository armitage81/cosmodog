package antonafanasjew.cosmodog.rendering.maprendererpredicates;

import antonafanasjew.cosmodog.topology.Position;

public interface MapLayerRendererPredicate {
	
	/**
	 * Indicates whether a specific tile should be rendered. That can be used for example
	 * to switch off top layers when a player enters a building.
	 * 
	 * @param layerIndex Whic layer is the tile on.
	 * @param position position of the tile.
	 * @return true, if the tile should be rendered, false otherwise.
	 */
	boolean tileShouldBeRendered(int layerIndex, Position position);
	
	
}
