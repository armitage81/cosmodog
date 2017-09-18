package antonafanasjew.cosmodog.tiledmap;

public class TiledTile {

	private short gid;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = (short)gid;
	}
	
	public int getGidAsTileNumber() {
		return gid - 1;
	}

	public void setGidFromTileNumber(int tileNumber) {
		this.gid = (short)(tileNumber + 1);
	}

	
}
