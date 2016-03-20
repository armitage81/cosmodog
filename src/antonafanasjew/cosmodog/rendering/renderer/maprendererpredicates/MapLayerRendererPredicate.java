package antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates;

public interface MapLayerRendererPredicate {
	
	/**
	 * Indicates whether a specific tile should be rendered. That can be used for example
	 * to switch off top layers when a player enters a building.
	 * 
	 * @param layerIndex Whic layer is the tile on.
	 * @param tileX X coordinate of the tile.
	 * @param tileY Y coordinate of the tile.
	 * @return true, if the tile should be rendered, false otherwise.
	 */
	boolean tileShouldBeRendered(int layerIndex, int tileX, int tileY);
	
	
}
