package antonafanasjew.cosmodog.topology;

import antonafanasjew.cosmodog.domains.MapType;

import java.io.Serializable;

/**
 * Defines a rectangle that is set on a 2D plane. Its position defines the top
 * left point of the rectangle.
 */
public class PlacedRectangle implements Serializable {

	private static final long serialVersionUID = -5774489558371712141L;

	private Position topLeftAnchor;
	private Rectangle rectangle;
	private MapType mapType;

	public static PlacedRectangle fromAnchorAndSize(float topLeftX, float topLeftY, float width, float height, MapType mapType) {
		PlacedRectangle placedRectangle = new PlacedRectangle();
		placedRectangle.topLeftAnchor = Position.fromCoordinates(topLeftX, topLeftY, mapType);
		placedRectangle.rectangle = Rectangle.fromSize(width, height);
		return placedRectangle;
	}

	private PlacedRectangle() {

	}

	public float x() {
		return topLeftAnchor.getX();
	};

	public float y() {
		return topLeftAnchor.getY();
	};
	
	public float width() {
		return rectangle.getWidth();
	}
	
	public float height() {
		return rectangle.getHeight();
	}

	public float minX() {
		return topLeftPosition().getX();
	}
	
	public float maxX() {
		return topRightPosition().getX();
	}
	
	public float minY() {
		return topLeftPosition().getY();
	}
	
	public float maxY() {
		return bottomLeftPosition().getY();
	}
	
	public float centerX() {
		return centerPosition().getX();
	}
	
	public float centerY() {
		return centerPosition().getY();
	}
	
	public Position topLeftPosition() {
		return Position.fromCoordinates(topLeftAnchor.getX(),
				topLeftAnchor.getY(), mapType);
	}

	public Position topRightPosition() {
		return Position.fromCoordinates(topLeftAnchor.getX() + this.width(),
				topLeftAnchor.getY(), mapType);
	}

	public Position bottomLeftPosition() {
		return Position.fromCoordinates(topLeftAnchor.getX(),
				topLeftAnchor.getY() + this.height(), mapType);
	}

	public Position bottomRightPosition() {
		return Position.fromCoordinates(topLeftAnchor.getX() + this.width(),
				topLeftAnchor.getY() + this.height(), mapType);
	}

	public Position centerPosition() {
		return Position.fromCoordinates(
				topLeftAnchor.getX() + this.width() / 2.0f,
				topLeftAnchor.getY() + this.height() / 2.0f, mapType);
	}

	public void scale(float scaleFactor) {
		float xCenterOld = this.centerX();
		float yCenterOld = this.centerY();
		float widthOld = this.rectangle.getWidth();
		float heightOld = this.rectangle.getHeight();
		
		this.rectangle.scale(scaleFactor);
		
		float xCenterNew = xCenterOld / widthOld * this.rectangle.getWidth();
		float yCenterNew = yCenterOld / heightOld * this.rectangle.getHeight();
		float widthNew = this.rectangle.getWidth();
		float heightNew = this.rectangle.getHeight();
		
		this.topLeftAnchor = Position.fromCoordinates(xCenterNew - (widthNew / 2), yCenterNew - (heightNew / 2), mapType);
	}

	public MapType getMapType() {
		return mapType;
	}

	public void shift(float offsetX, float offsetY) {
		this.topLeftAnchor.shift(offsetX, offsetY);
	}
	
	public void move(Position position) {
		this.topLeftAnchor.move(position.getX(), position.getY());
	}	
	
	/**
	 * Returns null if the rectangles do not intersect.
	 */
	public PlacedRectangle intersection(PlacedRectangle otherPlacedRectangle) {
		float xmin = Math.max(this.x(), otherPlacedRectangle.x());
		float xmax1 = this.x() + this.width();
		float xmax2 = otherPlacedRectangle.x()
				+ otherPlacedRectangle.width();
		float xmax = Math.min(xmax1, xmax2);
		if (xmax > xmin) {
			float ymin = Math.max(this.y(), otherPlacedRectangle.y());
			float ymax1 = this.y() + this.height();
			float ymax2 = otherPlacedRectangle.y() + otherPlacedRectangle.height();
			float ymax = Math.min(ymax1, ymax2);
			if (ymax > ymin) {
				return fromAnchorAndSize(xmin, ymin, xmax - xmin, ymax - ymin, mapType);
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((rectangle == null) ? 0 : rectangle.hashCode());
		result = prime * result
				+ ((topLeftAnchor == null) ? 0 : topLeftAnchor.hashCode());
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
		PlacedRectangle other = (PlacedRectangle) obj;
		if (rectangle == null) {
			if (other.rectangle != null)
				return false;
		} else if (!rectangle.equals(other.rectangle))
			return false;
		if (topLeftAnchor == null) {
			if (other.topLeftAnchor != null)
				return false;
		} else if (!topLeftAnchor.equals(other.topLeftAnchor))
			return false;
		return true;
	}
	

}
