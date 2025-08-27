package antonafanasjew.cosmodog.particlepattern.model;

import java.math.BigDecimal;

import antonafanasjew.cosmodog.topology.Rectangle;

import com.google.common.base.Preconditions;

/**
 * Builds a particle pattern based on a grid.
 * <p>
 * With this approach, the surface is split into a grid and a particle is placed at each grid point.
 * <p>
 * The grid width and height are provided as arguments to the constructor. They must "fit" in the surface.
 * <p>
 * For instance: If a surface is 100x100 and the grid width and height are 10,
 * then 100/10 = 10 particles will be placed in each row and column.
 * <p>
 * But if the grid width and height are 15, an exception will be thrown because 100/15 is not an integer.
 * <p>
 * Grid based patterns can be used to show regular patterns. This does not mean that the visual representation
 * must be "rectangular", however. For instance, an image of random snowflakes can be used as a particle. This image will be
 * repeated in each grid cell, but visually, the snowflakes will look random.
 */
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
