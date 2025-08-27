package antonafanasjew.cosmodog.particlepattern;

import antonafanasjew.cosmodog.particlepattern.model.Particle;
import antonafanasjew.cosmodog.particlepattern.model.ParticlePattern;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.topology.Vector;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

public class OffsetCalculator {

	private Function<Long, Vector> movementOffsetFunction;
	private ParticlePattern particlePattern;

	public ParticlePattern particlePatternForPlaceAndTime(PlacedRectangle cam, Long timeOffset) {
		Preconditions.checkState(cam != null);
		Preconditions.checkState(movementOffsetFunction != null);
		Preconditions.checkState(particlePattern != null);

		Rectangle particlePatternSurface = particlePattern.getSurface();
		
		Preconditions.checkState(particlePatternSurface.getWidth() >= cam.width());
		Preconditions.checkState(particlePatternSurface.getHeight() >= cam.height());
		
		ParticlePattern particlePatternForPlaceAndTime = new ParticlePattern(particlePatternSurface);

		float centerOffsetX = -(particlePatternSurface.getWidth() - cam.width()) / 2f;
		float centerOffsetY = -(particlePatternSurface.getHeight() - cam.height()) / 2f;

		if (centerOffsetX != 0) {
			@SuppressWarnings("unused")
			String s = "Added for test coverage";
		}
		
		if (centerOffsetY != 0) {
			@SuppressWarnings("unused")
			String s = "Added for test coverage";
		}
		
		Vector particlePatternSurfaceOffsetRelatedToCam = new Vector(centerOffsetX, centerOffsetY);
		
		for (Particle particle : particlePattern.particlesSet()) {
			Vector newVector = Vector.empty();
			newVector = newVector.shift(particle.getOffset(), particlePatternSurface);
			newVector = newVector.shift(movementOffsetFunction.apply(timeOffset), particlePatternSurface);
			newVector = newVector.shift(new Vector(cam.bottomLeftPosition().getX(), cam.bottomLeftPosition().getY()).inverse(), particlePatternSurface);
			newVector = newVector.shift(particlePatternSurfaceOffsetRelatedToCam, particlePatternSurface);
			
			Particle newParticle = Particle.fromVector(newVector);
			particlePatternForPlaceAndTime.addParticle(newParticle);
		}
		
		return particlePatternForPlaceAndTime;
	}
	
	public Function<Long, Vector> getMovementOffsetFunction() {
		return movementOffsetFunction;
	}

	public void setMovementOffsetFunction(Function<Long, Vector> movementOffsetFunction) {
		this.movementOffsetFunction = movementOffsetFunction;
	}

	public ParticlePattern getParticlePattern() {
		return particlePattern;
	}

	public void setParticlePattern(ParticlePattern particlePattern) {
		this.particlePattern = particlePattern;
	}
	
	
	
	
}
