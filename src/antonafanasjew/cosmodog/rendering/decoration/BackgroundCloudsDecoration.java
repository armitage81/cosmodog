package antonafanasjew.cosmodog.rendering.decoration;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.particlepattern.OffsetCalculator;
import antonafanasjew.particlepattern.model.GridParticlePatternBuilder;
import antonafanasjew.particlepattern.model.JitteringParticlePatternBuilder;
import antonafanasjew.particlepattern.model.ParticlePattern;
import antonafanasjew.particlepattern.model.ParticlePatternBuilder;
import antonafanasjew.particlepattern.movement.LinearMovementFunction;


public class BackgroundCloudsDecoration {

	private long initialTimestamp;

	private LinearMovementFunction movementFunction;
	private ParticlePattern particlePattern;
	private Rectangle particlePatternSurface = Rectangle.fromSize(1920, 1080);
	private OffsetCalculator offsetCalculator;

	public BackgroundCloudsDecoration(int gridWidth, int gridHeight, int millisecondsInUnit) {
		initialTimestamp = System.currentTimeMillis();
		movementFunction = new LinearMovementFunction(1, millisecondsInUnit);
		ParticlePatternBuilder builder = new GridParticlePatternBuilder(gridWidth, gridHeight);
		builder = JitteringParticlePatternBuilder.instance(builder, 200, 400);
		particlePattern = builder.build(particlePatternSurface);
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
