package antonafanasjew.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

public class LinearMovementFunction extends AbstractMovementFunction {

	private float risingFactor;
	private float offsetConstant;
	
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
		float x = units;
		float y = risingFactor * x + offsetConstant;
		return new Vector(x, y);
	}

}
