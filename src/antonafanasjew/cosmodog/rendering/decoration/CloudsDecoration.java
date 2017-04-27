package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.particlepattern.OffsetCalculator;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.particlepattern.model.ParticlePattern;
import antonafanasjew.particlepattern.movement.LinearMovementFunction;


public class CloudsDecoration {

	private long initialTimestamp;
	
	public static CloudsDecoration instance = null;
	
	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(1280, 720);
	private OffsetCalculator offsetCalculator;
	
	public static CloudsDecoration instance() {
		if (instance == null) {
			instance = new CloudsDecoration();
		}
		return instance;
	}
	
	private CloudsDecoration() {
		initialTimestamp = System.currentTimeMillis();
		movementFunction = new LinearMovementFunction(1, 50);
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
