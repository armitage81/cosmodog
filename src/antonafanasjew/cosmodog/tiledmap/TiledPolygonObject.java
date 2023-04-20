package antonafanasjew.cosmodog.tiledmap;

public class TiledPolygonObject extends TiledLineObject {

	private static final long serialVersionUID = -6252660902609354789L;

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
