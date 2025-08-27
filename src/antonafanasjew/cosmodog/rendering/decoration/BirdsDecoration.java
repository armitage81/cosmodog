package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.particlepattern.OffsetCalculator;
import antonafanasjew.cosmodog.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePattern;
import antonafanasjew.cosmodog.particlepattern.movement.LinearMovementFunction;

public class BirdsDecoration {
	
	public static BirdsDecoration instance = null;
	
	private long initialTimestamp;
	
	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(3840, 2160);
	private OffsetCalculator offsetCalculator;
	
	public static BirdsDecoration instance() {
		if (instance == null) {
			instance = new BirdsDecoration();
		}
		return instance;
	}
	
	private BirdsDecoration() {
		initialTimestamp = System.currentTimeMillis();
		movementFunction = new LinearMovementFunction(-1, 10);
		particlePattern = new GridParticlePatternBuilder(1920, 1080).build(particlePatternSurface);
		offsetCalculator = new OffsetCalculator();
		offsetCalculator.setMovementOffsetFunction(movementFunction);
		offsetCalculator.setParticlePattern(particlePattern);

	}
	
	public ParticlePattern particlePatternForPlaceAndTime(PlacedRectangle cam) {
		long timeStamp = System.currentTimeMillis();
		return offsetCalculator.particlePatternForPlaceAndTime(cam, timeStamp - initialTimestamp);
	}
	
	public Rectangle getParticlePatternSurface() {
		return particlePatternSurface;
	}

}
