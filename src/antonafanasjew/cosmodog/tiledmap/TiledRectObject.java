package antonafanasjew.cosmodog.tiledmap;

public class TiledRectObject extends TiledFigureObject {
	
	private static final long serialVersionUID = 7085173440402693430L;

	@Override
	public String toString() {
		return "[ID: " + this.getId() + " NAME: " + this.getName() + " TYPE: " + this.getType() + " X: " + this.getX() + " Y: " + this.getY() + " WIDTH: " + this.getWidth() + " HEIGHT: " + this.getHeight() + "]";
	}
	
}
