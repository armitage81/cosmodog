package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.particlepattern.OffsetCalculator;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.particlepattern.model.ParticlePattern;
import antonafanasjew.particlepattern.movement.*;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

public class SnowflakesDecoration {

	public static SnowflakesDecoration instance = null;

	private final long initialTimestamp;

    private final Rectangle particlePatternSurface = Rectangle.fromSize(3840, 2160);
	private final List<OffsetCalculator> offsetCalculators = Lists.newArrayList();
	private final List<String> animationIds = Lists.newArrayList();

	public static SnowflakesDecoration instance() {
		if (instance == null) {
			instance = new SnowflakesDecoration();
		}
		return instance;
	}

	private SnowflakesDecoration() {
		initialTimestamp = System.currentTimeMillis();
		List<Function<Long, Vector>> movementFunctions = Lists.newArrayList();

		AbstractMovementFunction l;
		AbstractMovementFunction s;
		ComposedMovementFunction c;

		l = new LinearMovementFunction(3f, 40);
		s = new SinusMovementFunction(20f, 20f, 0, 800f);
		c = new ComposedMovementFunction();
		c.getElements().add(l);
		c.getElements().add(s);
		movementFunctions.add(c);
		animationIds.add("snowflake0");

		l = new LinearMovementFunction(3.5f, 60);
		s = new SinusMovementFunction(15f, 20f, 320, 600f);
		c = new ComposedMovementFunction();
		c.getElements().add(l);
		c.getElements().add(s);
		movementFunctions.add(c);
		animationIds.add("snowflake0");

		l = new LinearMovementFunction(4f, 80);
		s = new SinusMovementFunction(20f, 15f, 0, 800f);
		c = new ComposedMovementFunction();
		c.getElements().add(l);
		c.getElements().add(s);
		movementFunctions.add(c);
		animationIds.add("snowflake1");

		l = new LinearMovementFunction(4.5f, 90);
		s = new SinusMovementFunction(10f, 30f, 320, 800f);
		c = new ComposedMovementFunction();
		c.getElements().add(l);
		c.getElements().add(s);
		movementFunctions.add(c);
		animationIds.add("snowflake1");

		l = new LinearMovementFunction(0.75f, 20);
		s = new SinusMovementFunction(20f, 10f, 0, 800f);
		c = new ComposedMovementFunction();
		c.getElements().add(l);
		c.getElements().add(s);
		movementFunctions.add(c);
		animationIds.add("snowflake2");

		l = new LinearMovementFunction(0.85f, 20);
		s = new SinusMovementFunction(20f, 10f, 320, 800f);
		c = new ComposedMovementFunction();
		c.getElements().add(l);
		c.getElements().add(s);
		movementFunctions.add(c);
		animationIds.add("snowflake2");

        List<ParticlePattern> particlePatterns = Lists.newArrayList();
        particlePatterns.add(new GridParticlePatternBuilder(768, 540).build(particlePatternSurface));
        particlePatterns.add(new GridParticlePatternBuilder(768, 540).build(particlePatternSurface));
		particlePatterns.add(new GridParticlePatternBuilder(768, 540).build(particlePatternSurface));
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

	public List<String> getAnimationIds() {
		return animationIds;
	}
}
