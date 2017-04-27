package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.particlepattern.OffsetCalculator;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.particlepattern.model.ParticlePattern;
import antonafanasjew.particlepattern.movement.LinearMovementFunction;

public class BirdsDecoration {
	
	public static BirdsDecoration instance = null;
	
	private long initialTimestamp;
	
	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(2560, 1440);
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
		particlePattern = new GridParticlePatternBuilder(1280, 720).build(particlePatternSurface);
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
