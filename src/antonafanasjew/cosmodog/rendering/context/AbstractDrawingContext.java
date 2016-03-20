package antonafanasjew.cosmodog.rendering.context;

public abstract class AbstractDrawingContext implements DrawingContext {

	DrawingContext parentDrawingContext = null;
	
	public AbstractDrawingContext(DrawingContext parentDrawingContext) {
		this.parentDrawingContext = parentDrawingContext;
	}
	
	@Override
	public float x() {
		float parentOffsetX = 0;
		if (parentDrawingContext != null) {
			parentOffsetX = parentDrawingContext.x();
		}
		return parentOffsetX + relativeX();
	}


	@Override
	public float y() {
		float parentOffsetY = 0;
		if (parentDrawingContext != null) {
			parentOffsetY = parentDrawingContext.y();
		}
		return parentOffsetY + relativeY();
	}
	

	public abstract float relativeX();
	
	public abstract float relativeY();
	
	@Override
	public String toString() {
		return this.x() + "/" + this.y() + "/" + this.w() + "/" + this.h();
	}

}
