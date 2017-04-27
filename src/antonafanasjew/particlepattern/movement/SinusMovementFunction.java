package antonafanasjew.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

public class SinusMovementFunction extends AbstractMovementFunction {

	private float horizontalScale;
	private float verticalScale;
	private float horizontalOffset;
	
	public SinusMovementFunction(float millisecondsInUnit) {
		this(1f, 1f, 0f, millisecondsInUnit);
	}
	
	public SinusMovementFunction(float horizontalScale, float verticalScale, float horizontalOffset, float millisecondsInUnit) {
		super(millisecondsInUnit);
		this.horizontalScale = horizontalScale;
		this.verticalScale = verticalScale;
		this.horizontalOffset = horizontalOffset;
	}
	
	@Override
	public Vector applyInternal(float units) {
		float x = units * horizontalScale;
		float y = (float)Math.sin(units + horizontalOffset) * verticalScale; 
		return new Vector(x, y);
	}

}
