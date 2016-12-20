package antonafanasjew.cosmodog.rendering.context;

/**
 * Don't mix it with map tiles. This context is just calculating a cell in the
 * grid of equal tiles in which the parent context is divided.
 */
public class TileDrawingContext extends AbstractDrawingContext {

	private float width;
	private float height;
	private float relativeX;
	private float relativeY;
	
	public TileDrawingContext(DrawingContext parentDrawingContext, int noOfColumns, int noOfRows, int column, int row) {
		this(parentDrawingContext, noOfColumns, noOfRows, column, row, 1, 1);
	}
	
	public TileDrawingContext(DrawingContext parentDrawingContext, int noOfColumns, int noOfRows, int column, int row, int widthInColumns, int heightInRows) {
		super(parentDrawingContext);
		float tileWidth = parentDrawingContext.w() / (float)noOfColumns;
		float tileHeight = parentDrawingContext.h() / (float)noOfRows;
		
		width = (tileWidth * widthInColumns);
		height = (tileHeight * heightInRows);
		relativeX = (tileWidth * column);
		relativeY = (tileHeight * row);
		
	}
	
	@Override
	public float relativeX() {
		return relativeX;
	}

	@Override
	public float relativeY() {
		return relativeY;
	}

	@Override
	public float w() {
		return width;
	}

	@Override
	public float h() {
		return height;
	}

}
