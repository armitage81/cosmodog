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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gid;
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
		TiledTile other = (TiledTile) obj;
		if (gid != other.gid)
			return false;
		return true;
	}
	
}
