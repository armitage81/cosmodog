package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.particlepattern.OffsetCalculator;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.particlepattern.model.ParticlePattern;
import antonafanasjew.particlepattern.movement.LinearMovementFunction;
import com.google.common.collect.Lists;

import java.util.List;

public class SnowflakesDecoration {

	public static SnowflakesDecoration instance = null;

	private long initialTimestamp;

	private List<LinearMovementFunction> movementFunctions = Lists.newArrayList();
	private List<ParticlePattern> particlePatterns = Lists.newArrayList();
	private Rectangle particlePatternSurface = Rectangle.fromSize(3840, 2160);
	private List<OffsetCalculator> offsetCalculators = Lists.newArrayList();

	public static SnowflakesDecoration instance() {
		if (instance == null) {
			instance = new SnowflakesDecoration();
		}
		return instance;
	}

	private SnowflakesDecoration() {
		initialTimestamp = System.currentTimeMillis();
		movementFunctions.add(new LinearMovementFunction(1f, 40));
        movementFunctions.add(new LinearMovementFunction(0.7f, 30));
		movementFunctions.add(new LinearMovementFunction(0.75f, 20));
		particlePatterns.add(new GridParticlePatternBuilder(768, 540).build(particlePatternSurface));
        particlePatterns.add(new GridParticlePatternBuilder(768, 540).build(particlePatternSurface));
		particlePatterns.add(new GridParticlePatternBuilder(768, 540).build(particlePatternSurface));

		for (int i = 0; i < particlePatterns.size(); i++) {
			OffsetCalculator offsetCalculator = new OffsetCalculator();
			offsetCalculator.setMovementOffsetFunction(movementFunctions.get(i));
			offsetCalculator.setParticlePattern(particlePatterns.get(i));
			offsetCalculators.add(offsetCalculator);
		}
	}
	
	public List<ParticlePattern> particlePatternsForPlaceAndTime(PlacedRectangle cam) {
		long timeStamp = System.currentTimeMillis();
		List<ParticlePattern> particlePatterns = Lists.newArrayList();
        for (OffsetCalculator offsetCalculator : offsetCalculators) {
            particlePatterns.add(offsetCalculator.particlePatternForPlaceAndTime(cam, timeStamp - initialTimestamp));
        }
		return particlePatterns;
	}
	
	public Rectangle getParticlePatternSurface() {
		return particlePatternSurface;
	}

}
