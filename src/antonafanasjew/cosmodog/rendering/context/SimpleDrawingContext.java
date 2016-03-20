package antonafanasjew.cosmodog.rendering.context;

import java.awt.Rectangle;


public class SimpleDrawingContext extends AbstractDrawingContext {
	
	protected float x;
	protected float y;
	protected float w;
	protected float h;

	public SimpleDrawingContext(DrawingContext parentDrawingContext, float x, float y, float w, float h) {
		super(parentDrawingContext);
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public SimpleDrawingContext(DrawingContext parentDrawingContext, Rectangle r) {
		this(parentDrawingContext, r.x, r.y, r.width, r.height);
	}
	

	@Override
	public float w() {
		return w;
	}

	@Override
	public float h() {
		return h;
	}

	@Override
	public float relativeX() {
		return x;
	}

	@Override
	public float relativeY() {
		return y;
	}

	public void setRelativeX(float x) {
		this.x = x;
	}

	public void setRelativeY(float y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return x + "/" + y + "/" + w + "/" + h;
	}
	
}
