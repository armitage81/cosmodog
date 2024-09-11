package antonafanasjew.particlepattern.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

/**
 * Represents a set of particles bound to a surface.
 * <p>
 * Particles can be added to the pattern. Their offsets will always be within the bounds of the surface
 * which is provided as an argument to the constructor.
 * <p>
 * Take note: the surface is not the screen but an abstract rectangle that is usually bigger than the screen. When
 * rendered, it will be aligned to the screen center to center so that its borders are not visible.
 * The coordinates of the decoration are then translated to the screen.
 * Example: The screen is 1000x800 pixels wide. The surface is 1200x900 pixels wide. The pattern bound to the surface
 * contains one particle at offset 0/0. When rendered, the particle will be at -100/-50 on the screen meaning that it
 * won't be visible. When the particle is at 300/200, it will be rendered at 200/150 on the screen.
 * This way sudden appearance of particles at the screen borders is avoided.
 * As a rule, the surface should be bigger than the screen and the difference should be at least twice as big as the
 * particle's size. (Particles usually represent top left corner of an image).
 */
public class ParticlePattern {
	
	private final Rectangle surface;
	private final Map<Vector, Particle> particles = new HashMap<Vector, Particle>();
	
	public ParticlePattern(Rectangle surface) {
		Preconditions.checkArgument(surface != null);
		this.surface = surface;
	}
	
	public Map<Vector, Particle> getParticles() {
		return particles;
	}
	
	public Set<Particle> particlesSet() {
		return Sets.newHashSet(particles.values());
	}
	
	public void addParticle(Particle particle) {
		particle.bindToSurface(surface);		
		this.particles.put(particle.getOffset(), particle);
	}

	public Rectangle getSurface() {
		return surface;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ParticlePattern: ");
		int size = particles.size();
		if (size > 20) {
			sb.append(size).append(" particles.");
		} else {
			sb.append(particles.values().toString());
		}
		return sb.toString();
	}
}
