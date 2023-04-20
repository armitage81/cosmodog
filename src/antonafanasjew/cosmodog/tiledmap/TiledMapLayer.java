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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + height;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(opacity);
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
		TiledMapLayer other = (TiledMapLayer) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (height != other.height)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Float.floatToIntBits(opacity) != Float.floatToIntBits(other.opacity))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	
}
