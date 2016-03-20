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
	

}
