package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.particlepattern.OffsetCalculator;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.particlepattern.model.ParticlePattern;
import antonafanasjew.particlepattern.movement.LinearMovementFunction;


public class SpaceliftDecoration {

	private long initialTimestamp;

	public static SpaceliftDecoration instance = null;

	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(1920, 1080);
	private OffsetCalculator offsetCalculator;

	public static SpaceliftDecoration instance() {
		if (instance == null) {
			instance = new SpaceliftDecoration();
		}
		return instance;
	}

	private SpaceliftDecoration() {
		initialTimestamp = System.currentTimeMillis();
		movementFunction = new LinearMovementFunction(999999, 50);
		particlePattern = new GridParticlePatternBuilder(96, 54).build(particlePatternSurface);
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
