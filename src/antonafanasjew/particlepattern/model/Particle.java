package antonafanasjew.particlepattern.model;

import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;

public class Particle {

	private Vector offset;

	public static final Particle fromCoordinates(float x, float y) {
		return new Particle(x, y);
	}
	
	public static final Particle fromVector(Vector vector) {
		return fromCoordinates(vector.getX(), vector.getY());
	}
	
	public Particle(float x, float y) {
		this(new Vector(x, y));
	}
	
	public Particle(Vector offset) {
		this.offset = offset;
	}
	
	public Vector getOffset() {
		return offset;
	}
	
	public void bindToSurface(Rectangle surface) {
		offset = offset.mod(surface);
	}
	
	@Override
	public String toString() {
		return "Particle with offset: " + offset.toString();
	}

}
