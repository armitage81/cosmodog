package antonafanasjew.cosmodog.tiledmap;

public class TiledTile {

	private int gid;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}
	
	public int getGidAsTileNumber() {
		return gid - 1;
	}

	public void setGidFromTileNumber(int tileNumber) {
		this.gid = tileNumber + 1;
	}

	
}
