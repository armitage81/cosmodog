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


	
	
	
}
