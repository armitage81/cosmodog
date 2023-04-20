package antonafanasjew.cosmodog.tiledmap;

public abstract class TiledFigureObject extends TiledObject {
	
	private static final long serialVersionUID = -5588102774262912262L;
	
	private float width;
	private float height;
	
	public float getWidth() {
		return width;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(width);
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
		TiledFigureObject other = (TiledFigureObject) obj;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}
	
}
