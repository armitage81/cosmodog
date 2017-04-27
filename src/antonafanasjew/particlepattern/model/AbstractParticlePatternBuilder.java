package antonafanasjew.particlepattern.model;

import antonafanasjew.cosmodog.topology.Rectangle;

public abstract class AbstractParticlePatternBuilder implements ParticlePatternBuilder {

	@Override
	public ParticlePattern build(Rectangle surface) {
		return buildInternal(surface);
	}

	protected abstract ParticlePattern buildInternal(Rectangle surface);

}
