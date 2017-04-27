package antonafanasjew.particlepattern.movement;

import java.math.BigDecimal;

import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Preconditions;

public class CircleMovementFunction extends AbstractMovementFunction {

	private float radius;
	private boolean clockwise;
	private float horizontalScale;
	private float verticalScale;
	
	public CircleMovementFunction(int millisecondsInUnit) {
		this(1f, false, millisecondsInUnit);
	}
	
	public CircleMovementFunction(float radius, boolean clockwise, float millisecondsInUnit) {
		this(1f, 1f, radius, clockwise, millisecondsInUnit);
	}
	
	public CircleMovementFunction(float horizontalScale, float verticalScale, float radius, boolean clockwise, float millisecondsInUnit) {
		super(millisecondsInUnit);
		Preconditions.checkArgument(radius > 0);
		this.radius = radius;
		this.clockwise = clockwise;
		this.horizontalScale = horizontalScale;
		this.verticalScale = verticalScale;
	}
	
	@Override
	public Vector applyInternal(float units) {
		double alpha = new BigDecimal(units).remainder(new BigDecimal(2 * Math.PI)).doubleValue();
		if (clockwise) {
			alpha = (2 * Math.PI) - alpha;
		}
		double x = Math.cos(alpha);
		x *= radius;
		x *= horizontalScale;
		double y = Math.sin(alpha);
		y *= radius;
		y *= verticalScale;
		return new Vector((float)x, (float)y);
	}

}
