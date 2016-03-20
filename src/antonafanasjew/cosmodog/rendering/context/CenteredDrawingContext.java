package antonafanasjew.cosmodog.rendering.context;

public class CenteredDrawingContext extends AbstractDrawingContext {

	protected float w;
	protected float h;

	public CenteredDrawingContext(DrawingContext parentDrawingContext, float borderWidth) {
		this(parentDrawingContext, parentDrawingContext.w() - 2 * borderWidth, parentDrawingContext.h() - 2 * borderWidth);
	}
	
	public CenteredDrawingContext(DrawingContext parentDrawingContext, float w, float h) {
		super(parentDrawingContext);
		this.w = w;
		this.h = h;
	}
	
	@Override
	public float w() {
		return this.w;
	}

	@Override
	public float h() {
		return this.h;
	}

	@Override
	public float relativeX() {
		return (parentDrawingContext.w() - w()) / 2.0f;
	}

	@Override
	public float relativeY() {
		return (parentDrawingContext.h() - h()) / 2.0f;
	}


}
