package antonafanasjew.cosmodog.tiledmap;

import java.util.List;

import com.google.common.collect.Lists;

public abstract class TiledLineObject extends TiledObject {

	private static final long serialVersionUID = -4100680246093467329L;

	public static class Point {
		
		public float x;
		public float y;
		
		
		
		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

		public Point() {

		}

		@Override
		public String toString() {
			return String.valueOf(x) + "," + String.valueOf(y);
		}
	}
	
	private List<Point> points = Lists.newArrayList();

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
}
