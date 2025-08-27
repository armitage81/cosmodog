package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.particlepattern.OffsetCalculator;
import antonafanasjew.cosmodog.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePattern;
import antonafanasjew.cosmodog.particlepattern.movement.LinearMovementFunction;


public class SpaceliftDecoration {

	private long initialTimestamp;

	private boolean upNotDown;

	public static SpaceliftDecoration instanceForGoingUp = null;
	public static SpaceliftDecoration instanceForGoingDown = null;

	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(1920, 1080);
	private OffsetCalculator offsetCalculator;

	public static SpaceliftDecoration instanceForGoingUp() {
		if (instanceForGoingUp == null) {
			instanceForGoingUp = new SpaceliftDecoration(true);
		}
		return instanceForGoingUp;
	}

	public static SpaceliftDecoration instanceForGoingDown() {
		if (instanceForGoingDown == null) {
			instanceForGoingDown = new SpaceliftDecoration(false);
		}
		return instanceForGoingDown;
	}

	private SpaceliftDecoration(boolean upNotDown) {
		initialTimestamp = System.currentTimeMillis();
		movementFunction = new LinearMovementFunction(upNotDown ? 10 : -10, upNotDown ? 300 : 100);
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
