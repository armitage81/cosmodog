package antonafanasjew.cosmodog;

import java.util.List;
import java.util.Map;

import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.TiledTile;
import antonafanasjew.cosmodog.tiledmap.Tileset;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

/**
 * This is a data structure for the initial map information of the game.
 * It will never be changed by the application and will always be used to initialize the static parts of the map.
 * It contains all relevant information of the tmx map format.
 */
public class CustomTiledMap {

	private String version;
	private String orientation;
	private String renderorder;
	private int width;
	private int height;
	private int tileWidth;
	private int tileHeight;
	private int nextObjectId;
	private Tileset tileset;
	private List<TiledMapLayer> mapLayers;
	
	private ImmutableMap<String, TiledMapLayer> mapLayersByNames;
	
	
	private Map<String, TiledObjectGroup> objectGroups = Maps.newHashMap();
	
	/**
	 * Returns the version of the map.
	 * @return Version of the map.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version of the map. 
	 * @param version Version of the map.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Returns the map orientation.
	 * @return Map orientation.
	 */
	public String getOrientation() {
		return orientation;
	}
	
	/**
	 * Sets the map orientation.
	 * @param orientation Map orientation.
	 */
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	
	/**
	 * Returns the render order.
	 * @return Render order.
	 */
	public String getRenderorder() {
		return renderorder;
	}
	
	/**
	 * Sets the render order.
	 * @param renderorder Render order.
	 */
	public void setRenderorder(String renderorder) {
		this.renderorder = renderorder;
	}
	
	/**
	 * Returns the map width in tiles.
	 * @return Map width in tiles.
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Sets the map width in tiles.
	 * @param width Map width in tiles.
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Returns the map height in tiles.
	 * @return Map height in tiles.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Sets the map height in tiles.
	 * @param width Map height in tiles.
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Returns the tile width in pixels.
	 * @return Tile width in pixels.
	 */
	public int getTileWidth() {
		return tileWidth;
	}
	
	/**
	 * Sets the tile width in pixels.
	 * @param tileWidth Tile width in pixels.
	 */
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}
	
	/**
	 * Returns the tile height in pixels.
	 * @return Tile height in pixels.
	 */
	public int getTileHeight() {
		return tileHeight;
	}
	
	/**
	 * Sets the tile height in pixels.
	 * @param tileHeight Tile height in pixels.
	 */
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
	
	/**
	 * Returns the next object id.
	 * @return Next object id.
	 */
	public int getNextObjectId() {
		return nextObjectId;
	}
	
	/**
	 * Sets the next object id.
	 * @param nextObjectId Next object id.
	 */
	public void setNextObjectId(int nextObjectId) {
		this.nextObjectId = nextObjectId;
	}
	
	/**
	 * Returns the tile set.
	 * @return Tile set.
	 */
	public Tileset getTileset() {
		return tileset;
	}
	
	/**
	 * Sets the tile set.
	 * @param tileset Tileset.
	 */
	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}
	
	/**
	 * Returns the layers of the map.
	 * @return Map layers.
	 */
	public List<TiledMapLayer> getMapLayers() {
		return mapLayers;
	}
	
	/**
	 * Sets the layers of the map.
	 * @param mapLayers Map layers.
	 */
	public void setMapLayers(List<TiledMapLayer> mapLayers) {
		this.mapLayers = mapLayers;
	}

	/**
	 * Returns the object groups of the map.
	 * @return Map's object groups.
	 */
	public Map<String, TiledObjectGroup> getObjectGroups() {
		return objectGroups;
	}
	
	/**
	 * This method should be called after the map has been completely initialized
	 * and will not be modified any more.
	 * Here, some cache data will be created to facilitate the access of map data.
	 */
	public void afterInitialization() {
		
		Map<String, TiledMapLayer> map = Maps.newHashMap();
		
		for (TiledMapLayer layer : mapLayers) {
			map.put(layer.getName(), layer);
		}
		
		this.mapLayersByNames = ImmutableMap.<String, TiledMapLayer>builder().putAll(map).build();
	}
	
	/**
	 * Returns the cached map layers by name which has been initialized after the afterInitialization method was called.
	 * Don't use it during the initialization. Don't try to modify it.
	 * @return Map layers by name. Immutable.
	 */
	public Map<String, TiledMapLayer> getMapLayersByNames() {
		return mapLayersByNames;
	}
	
	/**
	 * Short cut method to get a tile id from the position and layer.
	 * The tile id is the id as it was saved on the map file, which is 0 if 
	 * the tile is not set, 1 for the tile at the location 0/0 in the sprite sheet etc.
	 * 
	 */
	public int getTileId(int x, int y, int layerIndex) {
		TiledMapLayer layer = mapLayers.get(layerIndex);
		TiledTile tile = layer.getTile(x, y);
		return tile == null ? 0 : tile.getGid();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((mapLayers == null) ? 0 : mapLayers.hashCode());
		result = prime * result + nextObjectId;
		result = prime * result + ((objectGroups == null) ? 0 : objectGroups.hashCode());
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		result = prime * result + ((renderorder == null) ? 0 : renderorder.hashCode());
		result = prime * result + tileHeight;
		result = prime * result + tileWidth;
		result = prime * result + ((tileset == null) ? 0 : tileset.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomTiledMap other = (CustomTiledMap) obj;
		if (height != other.height)
			return false;
		if (mapLayers == null) {
			if (other.mapLayers != null)
				return false;
		} else if (!mapLayers.equals(other.mapLayers))
			return false;
		if (nextObjectId != other.nextObjectId)
			return false;
		if (objectGroups == null) {
			if (other.objectGroups != null)
				return false;
		} else if (!objectGroups.equals(other.objectGroups))
			return false;
		if (orientation == null) {
			if (other.orientation != null)
				return false;
		} else if (!orientation.equals(other.orientation))
			return false;
		if (renderorder == null) {
			if (other.renderorder != null)
				return false;
		} else if (!renderorder.equals(other.renderorder))
			return false;
		if (tileHeight != other.tileHeight)
			return false;
		if (tileWidth != other.tileWidth)
			return false;
		if (tileset == null) {
			if (other.tileset != null)
				return false;
		} else if (!tileset.equals(other.tileset))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
	
	
	
}
