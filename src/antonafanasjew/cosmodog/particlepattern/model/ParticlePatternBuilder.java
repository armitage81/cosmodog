package antonafanasjew.cosmodog.particlepattern.model;

import antonafanasjew.cosmodog.topology.Rectangle;

/**
 * Represents a builder for a particle pattern.
 * <p>
 * A particle pattern is a set of particles that are bound to a surface. A builder is used to create a particle pattern.
 * For instance, a snowfall pattern could be created by a builder that creates particles in a specific pattern.
 * Another example is a grid pattern where particles are created in a grid-like pattern.
 */
public interface ParticlePatternBuilder {

	ParticlePattern build(Rectangle surface);
	
}
