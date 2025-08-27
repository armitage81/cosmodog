package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.particlepattern.OffsetCalculator;
import antonafanasjew.cosmodog.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePattern;
import antonafanasjew.cosmodog.particlepattern.movement.LinearMovementFunction;


public class CloudsDecoration {

	private long initialTimestamp;
	
	public static CloudsDecoration instance = null;
	
	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(1920, 1080);
	private OffsetCalculator offsetCalculator;
	
	public static CloudsDecoration instance(int gridWidth, int gridHeight) {
		if (instance == null) {
			instance = new CloudsDecoration(gridWidth, gridHeight);
		}
		return instance;
	}
	
	private CloudsDecoration(int gridWidth, int gridHeight) {
		initialTimestamp = System.currentTimeMillis();
		movementFunction = new LinearMovementFunction(1, 50);
		particlePattern = new GridParticlePatternBuilder(gridWidth, gridHeight).build(particlePatternSurface);
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
