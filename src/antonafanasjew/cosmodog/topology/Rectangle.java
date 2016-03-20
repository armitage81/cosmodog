package antonafanasjew.cosmodog.topology;

import java.io.Serializable;

/**
 * Definition of a rectangle
 */
public class Rectangle implements Serializable {
	
	private static final long serialVersionUID = 9057133312053489869L;

	private float width;
	private float height;
	
	public static Rectangle fromSize(float width, float height) {
		Rectangle rectangle = new Rectangle();
		rectangle.width = width;
		rectangle.height = height;
		return rectangle;
	}
	
	protected Rectangle() {
		
	}
	
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

	public void scale(float scaleFactor) {
		this.width *= scaleFactor;
		this.height *= scaleFactor;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(height);
		result = prime * result + Float.floatToIntBits(width);
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
		Rectangle other = (Rectangle) obj;
		if (Float.floatToIntBits(height) != Float.floatToIntBits(other.height))
			return false;
		if (Float.floatToIntBits(width) != Float.floatToIntBits(other.width))
			return false;
		return true;
	}
	
}
