package antonafanasjew.cosmodog.tiledmap;

public class TiledPolylineObject extends TiledLineObject {

	private static final long serialVersionUID = -7534910478573395441L;

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

}
