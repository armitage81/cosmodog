package antonafanasjew.cosmodog.util;

import java.util.List;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.tiledmap.TiledEllipseObject;
import antonafanasjew.cosmodog.tiledmap.TiledFigureObject;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.tiledmap.TiledPolygonObject;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.tiledmap.TiledRectObject;
import antonafanasjew.cosmodog.tiledmap.TiledTileObject;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;

public class CollisionUtils {

	public static boolean intersects(PlacedRectangle r, MapType regionMapType, TiledObject region) {
		if (region instanceof TiledFigureObject) {
			return intersects(r, regionMapType, (TiledFigureObject) region);
		} else if (region instanceof TiledLineObject) {
			return intersects(r, regionMapType, (TiledLineObject) region);
		} else if (region instanceof TiledTileObject) {
			return intersects(r, (TiledTileObject) region);
		} else {
			return false;
		}
	}

	private static boolean intersects(PlacedRectangle r, MapType regionMapType, TiledFigureObject region) {
		if (region instanceof TiledRectObject) {
			return intersects(r, regionMapType, (TiledRectObject) region);
		} else if (region instanceof TiledEllipseObject) {
			return intersects(r, regionMapType, (TiledEllipseObject) region);
		} else {
			return false;
		}
	}

	private static boolean intersects(PlacedRectangle r, MapType regionMapType, TiledLineObject region) {
		if (region instanceof TiledPolylineObject) {
			return intersects(r, regionMapType, (TiledPolylineObject) region);
		} else if (region instanceof TiledPolygonObject) {
			return intersects(r, (TiledPolygonObject) region);
		} else {
			return false;
		}
	}

	private static boolean intersects(PlacedRectangle r, MapType regionMapType, TiledRectObject region) {
		PlacedRectangle regionRectangle = PlacedRectangle.fromAnchorAndSize(region.getX(), region.getY(), region.getWidth(), region.getHeight(), regionMapType);
		return r.intersection(regionRectangle) != null;
	}

	private static boolean intersects(PlacedRectangle r, MapType regionMapType, TiledEllipseObject region) {
		throw new RuntimeException("Not implemented");
	}

	private static boolean intersects(PlacedRectangle r, MapType regionMapType, TiledPolylineObject region) {
		throw new RuntimeException("Not implemented");
	}
	
	/**
	 * Returns true if the placed rectangle intersects (actually contains) a position.
	 * @param r Rectangle
	 * @param position Point
	 * @return true if the position is within the rectangle, false otherwise.
	 */
	public static boolean intersects(PlacedRectangle r, Position position) {
		float x1 = r.minX();
		float x2 = r.maxX();
		float y1 = r.minY();
		float y2 = r.maxY();
		
		float x = position.getX();
		float y = position.getY();
		
		return x >= x1 && x < x2 && y >= y1 && y < y2;
		
	}

	/*
	 * Take care. The intersection relates on the center of the region. If the polygon intrudes the region slightly without crossing its center, it is considered to be non intersecting. 
	 */
	private static boolean intersects(PlacedRectangle r, TiledPolygonObject region) {

		Point p = new Point(r.centerX(), r.centerY());
		
		List<Point> points = region.getPoints();

		//As described at: http://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
		int i;
		int j;
		boolean result = false;
		for (i = 0, j = points.size() - 1; i < points.size(); j = i++) {
			if ((points.get(i).y > p.y) != (points.get(j).y > p.y) && (p.x < (points.get(j).x - points.get(i).x) * (p.y - points.get(i).y) / (points.get(j).y - points.get(i).y) + points.get(i).x)) {
				result = !result;
			}
		}
		
		return result;
		

	}

	private static boolean intersects(PlacedRectangle r, TiledTileObject region) {
		throw new RuntimeException("Not implemented");
	}

}
