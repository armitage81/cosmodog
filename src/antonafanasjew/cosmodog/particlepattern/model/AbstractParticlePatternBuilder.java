package antonafanasjew.cosmodog.particlepattern.model;

import antonafanasjew.cosmodog.topology.Rectangle;

/**
 * Holds the common logic for all particle pattern builders. Is currently empty.
 */
public abstract class AbstractParticlePatternBuilder implements ParticlePatternBuilder {

	@Override
	public ParticlePattern build(Rectangle surface) {
		return buildInternal(surface);
	}

	protected abstract ParticlePattern buildInternal(Rectangle surface);

}
