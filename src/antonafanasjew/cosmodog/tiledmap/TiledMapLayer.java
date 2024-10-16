package antonafanasjew.cosmodog.tiledmap;

import antonafanasjew.cosmodog.topology.Position;

import java.util.List;

public class TiledMapLayer {

	private String id;
	private String name;
	private int width;
	private int height;
	private boolean visible;
	private float opacity;
	private List<TiledTile> data;
	
	public String getName() {
		return name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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

	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public TiledTile getTile(Position position) {
		return this.getData().get((int)(position.getY() * getWidth() + position.getX()));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + height;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + Float.floatToIntBits(opacity);
		result = prime * result + (visible ? 1231 : 1237);
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Float.floatToIntBits(opacity) != Float.floatToIntBits(other.opacity))
			return false;
		if (visible != other.visible)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	

}
