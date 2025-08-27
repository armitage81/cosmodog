package antonafanasjew.cosmodog.particlepattern.movement;

import java.math.BigDecimal;

import antonafanasjew.cosmodog.topology.Vector;

public class ShakingMovementFunction extends AbstractMovementFunction {


	private float amplitudeX;
	private float amplitudeY;
	
	private float xCycleInUnits;
	private float yCycleInUnits;

	public ShakingMovementFunction(float millisecondInUnit, float amplitudeX, float amplitudeY, float xCycleInUnits, float yCycleInUnits) {
		super(millisecondInUnit);
		this.amplitudeX = amplitudeX;
		this.amplitudeY = amplitudeY;
		this.xCycleInUnits = xCycleInUnits;
		this.yCycleInUnits = yCycleInUnits;
	}
	
	@Override
	protected Vector applyInternal(float units) {
		
		double offsetForX = new BigDecimal(units).remainder(new BigDecimal(xCycleInUnits)).doubleValue();
		double offsetForY = new BigDecimal(units).remainder(new BigDecimal(yCycleInUnits)).doubleValue();
		
		
		double resultX;
		
		if (offsetForX <= 1 * xCycleInUnits / 4) {
			resultX = offsetForX * amplitudeX;
		} else if (offsetForX <= 2 * xCycleInUnits / 4) {
			offsetForX -= (1 * xCycleInUnits / 4);
			resultX = amplitudeX - (offsetForX * amplitudeX);
		} else if (offsetForX <= 3 * xCycleInUnits / 4) {
			offsetForX -= (2 * xCycleInUnits / 4);
			resultX = -offsetForX * amplitudeX;
		} else {
			offsetForX -= (3 * xCycleInUnits / 4);
			resultX = -amplitudeX + (offsetForX * amplitudeX);
		}
		
		double resultY;

		if (offsetForY <= 1 * yCycleInUnits / 4) {
			resultY = offsetForY * amplitudeY;
		} else if (offsetForY <= 2 * yCycleInUnits / 4) {
			offsetForY -= (1 * yCycleInUnits / 4);
			resultY = amplitudeY - (offsetForY * amplitudeY);
		} else if (offsetForY <= 3 * yCycleInUnits / 4) {
			offsetForY -= (2 * yCycleInUnits / 4);
			resultY = -offsetForY * amplitudeY;
		} else {
			offsetForY -= (3 * yCycleInUnits / 4);
			resultY = -amplitudeY + (offsetForY * amplitudeY);
		}

		return new Vector((float)resultX, (float)resultY);
	}

}
