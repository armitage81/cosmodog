
package antonafanasjew.cosmodog.topology;

import java.math.BigDecimal;

public class Vector {
	
	private float x;
	private float y;
	
	public static final Vector empty() {
		return new Vector(0, 0);
	}
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector inverse() {
		return new Vector(-x, -y);
	}
	
	public Vector shift(Vector v, Rectangle surface) {
		return shift(v, surface.getWidth(), surface.getHeight());
	}
	
	public Vector shift(Vector v, float surfaceWidth, float surfaceHeight) {
		Vector sum = this.add(v);
		Vector retVal = sum.mod(surfaceWidth, surfaceHeight);
		return retVal;
	}
	
	public Vector mod(Rectangle surface) {
		return mod(surface.getWidth(), surface.getHeight());
	}
	
	public Vector mod(float width, float height) {
		
		BigDecimal bigX = new BigDecimal(String.valueOf(x));
		BigDecimal bigY = new BigDecimal(String.valueOf(y));
		
		if (x < 0) {
			int n = (int)(-x / width);
			n = (n * width + x < 0 ) ? n + 1 : n;
			BigDecimal bigN = new BigDecimal(String.valueOf(n));
			BigDecimal bigWidth = new BigDecimal(String.valueOf(width));
			BigDecimal toAdd = bigN.multiply(bigWidth);
			bigX = bigX.add(toAdd);
		}
		
		if (y < 0) {
			int n = (int)(-y / height);
			n = (n * height + y < 0 ) ? n + 1 : n;
			BigDecimal bigN = new BigDecimal(String.valueOf(n));
			BigDecimal bigHeight = new BigDecimal(String.valueOf(height));
			BigDecimal toAdd = bigN.multiply(bigHeight);
			bigY = bigY.add(toAdd);
		}
		
		BigDecimal bigWidth = new BigDecimal(String.valueOf(width));
		BigDecimal bigXRemainder = bigX.remainder(bigWidth);
		
		BigDecimal bigHeight = new BigDecimal(String.valueOf(height));
		BigDecimal bigYRemainder = bigY.remainder(bigHeight);
		
		return new Vector(bigXRemainder.floatValue(), bigYRemainder.floatValue());
	}
	
	public Vector add(float x, float y) {
		return add(new Vector(x, y));
	}
	
	public Vector add(Vector v) {
		Vector retVal = new Vector(this.x + v.x, this.y + v.y);
		return retVal;
	}

	@Override
	public String toString() {
		return x + "/" + y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
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
		Vector other = (Vector) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}
	
	
	
}
