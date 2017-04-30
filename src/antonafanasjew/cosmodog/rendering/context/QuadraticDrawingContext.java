package antonafanasjew.cosmodog.rendering.context;

public class QuadraticDrawingContext extends AbstractDrawingContext {

	protected float w;
	protected float h;

	public QuadraticDrawingContext(DrawingContext parentDrawingContext) {
		super(parentDrawingContext);
		this.w = parentDrawingContext.w() < parentDrawingContext.h() ? parentDrawingContext.w() : parentDrawingContext.h();
		this.h = w;
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
