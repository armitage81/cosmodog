package antonafanasjew.cosmodog;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.Tileset;

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
	
}
