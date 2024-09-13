package antonafanasjew.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;

/**
 * Represents a sinus movement function. Depending on the given time, the offset vector will increase in a sinus way.
 * horizontalScale: The horizontal scale of the sinus function. The bigger the value, the faster the movement. The sinus function has a higher frequency.
 * verticalScale: The vertical scale of the sinus function. The bigger the value, the higher the amplitude.
 * horizontalOffset: The horizontal offset of the sinus function. The bigger the value, the more the sinus function is shifted to the right.
 */
public class SinusMovementFunction extends AbstractMovementFunction {

	private final float horizontalScale;
	private final float verticalScale;
	private final float horizontalOffset;
	
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