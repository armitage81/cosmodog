package antonafanasjew.cosmodog.rendering.decoration;

import java.util.Random;

import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.topology.Position;

public class BirdsDecoration {

	private Random random = new Random();
	private int minIntervalInSeconds;
	private int maxIntervalInSeconds;
	float speedPerSecond;
	private float flightHeightFactor;
	private Cam cam;
	
	int passedTimeSinceLastBird = 0;
	
	private int nextIntervalInSeconds = 0;
	
	private Position currentBirdsPosition = null;

	public BirdsDecoration(Cam cam, int minIntervalInSeconds, int maxIntervalInSeconds, float speedPerSecond) {
		this(cam, minIntervalInSeconds, maxIntervalInSeconds, speedPerSecond, 1);
	}
	
	public BirdsDecoration(Cam cam, int minIntervalInSeconds, int maxIntervalInSeconds, float speedPerSecond, float flightHeightFactor) {
		this.cam = cam;
		this.minIntervalInSeconds = minIntervalInSeconds;
		this.maxIntervalInSeconds = maxIntervalInSeconds;
		this.speedPerSecond = speedPerSecond;
		this.flightHeightFactor = flightHeightFactor;
		nextIntervalInSeconds = random.nextInt(maxIntervalInSeconds - minIntervalInSeconds) + minIntervalInSeconds;
	}

	public void update(int delta) {

		if (currentBirdsPosition == null) {
			throwDiceAndCreatePosition(delta);
		} else {
			movePosition(delta);
		}
		
	}
	
	private void movePosition(int delta) {
		
		float zoomFactor = cam.getZoomFactor();
		
		float offset = zoomFactor * getFlightHeightFactor() * speedPerSecond * delta / 1000f;
		
		currentBirdsPosition.shift(offset, -offset);
		if (currentBirdsPosition.getX() > cam.viewCopy().width() + 100) {
			currentBirdsPosition = null;
			passedTimeSinceLastBird = 0;
			nextIntervalInSeconds = random.nextInt(maxIntervalInSeconds - minIntervalInSeconds) + minIntervalInSeconds;
		}
	}

	private void throwDiceAndCreatePosition(int delta) {
		
		passedTimeSinceLastBird += delta;
		
		if (passedTimeSinceLastBird >= nextIntervalInSeconds * 1000) {
		
			int horizontalGrace = (int)(cam.viewCopy().width() / 20);
			int verticalGrace = (int)(cam.viewCopy().height() / 20);
			
    		boolean vertical = random.nextBoolean();
    		
    		float x;
    		float y;
    		
    		if (vertical) {
    			x = -100;
    			y = random.nextInt((int)cam.viewCopy().height() - 2 * verticalGrace) + verticalGrace;
    		} else {
    			x = random.nextInt((int)(cam.viewCopy().width() - 100) - 2 * horizontalGrace) + horizontalGrace;
    			y = cam.viewCopy().height();
    		}
    		
    		currentBirdsPosition = Position.fromCoordinates(x, y);
		}
	}

	public Position getCurrentBirdsPosition() {
		return currentBirdsPosition;
	}

	public float getFlightHeightFactor() {
		return flightHeightFactor;
	}

}
