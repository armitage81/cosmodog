package antonafanasjew.cosmodog.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

/**
 * Represents a linear movement function. Depending on the given time, the offset vector will increase in a linear way.
 * The function is defined by a rising factor and an offset constant. (y = risingFactor * x + offsetConstant)
 */
public class LinearMovementFunction extends AbstractMovementFunction {

	private final float risingFactor;
	private final float offsetConstant;
	
	public LinearMovementFunction(float risingFactor, float offsetConstant, int millisecondsInUnit) {
		super(millisecondsInUnit);
		this.risingFactor = risingFactor;
		this.offsetConstant = offsetConstant;
	}
	
	public LinearMovementFunction(float risingFactor, int millisecondsInUnit) {
		this(risingFactor, 0f, millisecondsInUnit);
	}
	
	@Override
	public Vector applyInternal(float units) {
        float y = risingFactor * units + offsetConstant;
		return new Vector(units, y);
	}

}
