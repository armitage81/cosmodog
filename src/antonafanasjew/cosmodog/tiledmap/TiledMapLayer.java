package antonafanasjew.cosmodog.tiledmap;

import java.util.List;

public class TiledMapLayer {

	private String name;
	private int width;
	private int height;
	private float opacity;
	private List<TiledTile> data;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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

	public List<TiledTile> getData() {
		return data;
	}

	public void setData(List<TiledTile> data) {
		this.data = data;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public TiledTile getTile(int x, int y) {
		return this.getData().get(y * getWidth() + x);
	}
	
}
