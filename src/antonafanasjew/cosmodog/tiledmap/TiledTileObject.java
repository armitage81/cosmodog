package antonafanasjew.cosmodog.tiledmap;

public class TiledTileObject extends TiledObject {

	private static final long serialVersionUID = 8941701272218753912L;
	
	private int gid;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + gid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TiledTileObject other = (TiledTileObject) obj;
		if (gid != other.gid)
			return false;
		return true;
	}
	
}
