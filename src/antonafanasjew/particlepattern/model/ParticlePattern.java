package antonafanasjew.particlepattern.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

public class ParticlePattern {
	
	private Rectangle surface;
	private Map<Vector, Particle> particles = new HashMap<Vector, Particle>(); 
	
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
