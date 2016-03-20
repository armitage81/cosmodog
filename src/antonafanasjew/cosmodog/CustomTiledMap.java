package antonafanasjew.cosmodog;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.tiledmap.Tileset;

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
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getOrientation() {
		return orientation;
	}
	
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	
	public String getRenderorder() {
		return renderorder;
	}
	
	public void setRenderorder(String renderorder) {
		this.renderorder = renderorder;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getTileWidth() {
		return tileWidth;
	}
	
	public void setTileWidth(int tileWidth) {
		this.tileWidth = tileWidth;
	}
	
	public int getTileHeight() {
		return tileHeight;
	}
	
	public void setTileHeight(int tileHeight) {
		this.tileHeight = tileHeight;
	}
	
	public int getNextObjectId() {
		return nextObjectId;
	}
	
	public void setNextObjectId(int nextObjectId) {
		this.nextObjectId = nextObjectId;
	}
	
	public Tileset getTileset() {
		return tileset;
	}
	
	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}
	
	public List<TiledMapLayer> getMapLayers() {
		return mapLayers;
	}
	
	public void setMapLayers(List<TiledMapLayer> mapLayers) {
		this.mapLayers = mapLayers;
	}

	public Map<String, TiledObjectGroup> getObjectGroups() {
		return objectGroups;
	}
	
}
