package antonafanasjew.particlepattern.model;

import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;

/**
 * Represents a particle with an offset in a 2D space.
 * <p>
 * The offset is a vector that represents the position of the particle in a 2D space.
 * <p>
 * The x and y coordinates of the offset are float values.
 * <p>
 * A particle can be seen as a point in a 2D space with the additional functionality to bind it to a surface.
 * <p>
 * A surface is a rectangle with defined width and height.
 * A particle bound to a surface will always stay within the bounds of the surface.
 * <p>
 * It is done by applying the modulo operation to the offset of the particle.
 * There are many examples of this calculation in video games when a character
 * moves out of the screen on one side and reappears on the other side.
 * <p>
 * Particles are meant to represent points that are moving across the surface in a specific pattern.
 * It could be snowflakes, birds that fly over the game world, or any other object that moves in a regular way.
 */
public class Particle {

	private Vector offset;

	public static Particle fromCoordinates(float x, float y) {
		return new Particle(x, y);
	}
	
	public static Particle fromVector(Vector vector) {
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
