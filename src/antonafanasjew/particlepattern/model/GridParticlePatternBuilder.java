package antonafanasjew.particlepattern.model;

import java.math.BigDecimal;

import antonafanasjew.cosmodog.topology.Rectangle;

import com.google.common.base.Preconditions;

public class GridParticlePatternBuilder extends AbstractParticlePatternBuilder {

	private BigDecimal gridWidth;
	private BigDecimal gridHeight;
	
	public GridParticlePatternBuilder(float gridWidth, float gridHeight) {
		this.gridWidth = new BigDecimal(String.valueOf(gridWidth));
		this.gridHeight = new BigDecimal(String.valueOf(gridHeight));
	}
	
	@Override
	protected ParticlePattern buildInternal(Rectangle surface) {
		
		BigDecimal surfaceWidth = new BigDecimal(String.valueOf(surface.getWidth()));
		BigDecimal surfaceHeight = new BigDecimal(String.valueOf(surface.getHeight()));
		
		Preconditions.checkState(surfaceWidth.remainder(gridWidth).floatValue() == 0);
		Preconditions.checkState(surfaceHeight.remainder(gridHeight).floatValue() == 0);
		
		ParticlePattern pp = new ParticlePattern(surface);
		
		for (float i = 0; i < surfaceWidth.floatValue(); i += gridWidth.floatValue()) {
			for (float j = 0; j < surfaceHeight.floatValue(); j += gridHeight.floatValue()) {
				Particle p = Particle.fromCoordinates(i, j);
				pp.addParticle(p);
			}
		}
		
		return pp;
	}

}
