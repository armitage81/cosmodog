package antonafanasjew.cosmodog.tiledmap;

public class Tileset {

	private int firstgid;
	private String name;
	private int tilewidth;
	private int tileheight;
	TileImage tileImage;
	
	public int getFirstgid() {
		return firstgid;
	}
	
	public void setFirstgid(int firstgid) {
		this.firstgid = firstgid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getTilewidth() {
		return tilewidth;
	}
	
	public void setTilewidth(int tilewidth) {
		this.tilewidth = tilewidth;
	}
	
	public int getTileheight() {
		return tileheight;
	}
	
	public void setTileheight(int tileheight) {
		this.tileheight = tileheight;
	}
	
	public TileImage getTileImage() {
		return tileImage;
	}
	
	public void setTileImage(TileImage tileImage) {
		this.tileImage = tileImage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + firstgid;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tileImage == null) ? 0 : tileImage.hashCode());
		result = prime * result + tileheight;
		result = prime * result + tilewidth;
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
		Tileset other = (Tileset) obj;
		if (firstgid != other.firstgid)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tileImage == null) {
			if (other.tileImage != null)
				return false;
		} else if (!tileImage.equals(other.tileImage))
			return false;
		if (tileheight != other.tileheight)
			return false;
		if (tilewidth != other.tilewidth)
			return false;
		return true;
	}

}
