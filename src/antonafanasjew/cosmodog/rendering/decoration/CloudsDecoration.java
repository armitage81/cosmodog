package antonafanasjew.cosmodog.rendering.decoration;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;

import com.google.common.collect.Lists;

/**
 * Take care: The generated cloud bounds are related to the unscaled map bounds (number of tiles * tile width)
 * No real coordinates.
 */
public class CloudsDecoration {

	private Random random = new Random(10343439839573l);
	private Rectangle mapBounds;
	private int timeIntervalBetweenClouds;
	private float horizontalSpeedPerSecond;
	private float verticalSpeedPerSecond;
	private int cloudWidth;
	private int cloudHeight;
	
	private List<PlacedRectangle> cloudRectangles = Lists.newArrayList();
	private int timePassed = 0;
	
	
	public CloudsDecoration(Rectangle mapBounds, int timeIntervalBetweenClouds, float horizontalSpeedPerSecond, float verticalSpeedPerSecond, int cloudWidth, int cloudHeight) {
		this.mapBounds = mapBounds;
		this.timeIntervalBetweenClouds = timeIntervalBetweenClouds;
		this.horizontalSpeedPerSecond = horizontalSpeedPerSecond;
		this.verticalSpeedPerSecond = verticalSpeedPerSecond;
		this.cloudWidth = cloudWidth;
		this.cloudHeight = cloudHeight;
		timePassed = timeIntervalBetweenClouds;
		for (int i = 0; i < 60; i++) {
			update(1000);
		}
	}

	
	public void update(int delta) {
		updateExistingRectangles(delta);
		createNewDrawingContext(delta);
	}

	private void createNewDrawingContext(int delta) {
		
		timePassed += delta;
		int noOfNewClouds = timePassed / timeIntervalBetweenClouds;
		timePassed = timePassed % timeIntervalBetweenClouds;
		
		float x;
		float y;
		
		for (int i = 0; i < noOfNewClouds; i++) {
			
			int count = 0;
			
			do {
				count++;
    			x = 0;
    			y = 0;
    			boolean isVertical = random.nextBoolean();
    			boolean topDown = verticalSpeedPerSecond > 0;
    			boolean leftToRight = horizontalSpeedPerSecond > 0;
    			if (isVertical) {
    				x = leftToRight ? -cloudWidth : mapBounds.getWidth();
    				y = random.nextInt(Integer.MAX_VALUE) % mapBounds.getHeight() - cloudHeight;
    			} else {
    				x = random.nextInt(Integer.MAX_VALUE) % mapBounds.getWidth() - cloudWidth;
    				y = topDown ? -cloudHeight : mapBounds.getHeight();
    			}
			} while (intersectsExisting((int)x, (int)y, cloudWidth, cloudHeight) && count < 10);
			
			if (!intersectsExisting((int)x, (int)y, cloudWidth, cloudHeight)) {
				PlacedRectangle cloudRectangle = PlacedRectangle.fromAnchorAndSize(x, y, cloudWidth, cloudHeight);
				this.cloudRectangles.add(cloudRectangle);
			}
		}
		
	}

	/*
	 * This only works for positive hor. and ver. speed
	 */
	private void updateExistingRectangles(int delta) {
		
		float horizontalChange = horizontalSpeedPerSecond / 1000.0f * delta;
		float verticalChange = verticalSpeedPerSecond / 1000.0f * delta;
		
		Iterator<PlacedRectangle> it = cloudRectangles.iterator();
		while(it.hasNext()) {
			PlacedRectangle cloudRectangle = it.next();
			
			cloudRectangle.move(cloudRectangle.minX() + horizontalChange, cloudRectangle.minY() + verticalChange);
			
			if (cloudRectangle.minX() > mapBounds.getWidth() && cloudRectangle.minY() > mapBounds.getHeight()) {
				it.remove();
			}
		}
		
		
	}


	public List<PlacedRectangle> getCloudRectangles() {
		return cloudRectangles;
	}

	private boolean intersectsExisting(int x, int y, int width, int height) {
		boolean retVal = false;
		PlacedRectangle newPlacedRectangle = PlacedRectangle.fromAnchorAndSize(x, y, width, height);
		
		for (PlacedRectangle c : cloudRectangles) {
			if (newPlacedRectangle.intersection(c) != null) {
				retVal = true;
				break;
			}
		}
		
		return retVal;
	}
	
}
