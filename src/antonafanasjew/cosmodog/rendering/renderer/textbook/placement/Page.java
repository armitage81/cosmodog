package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;

public class Page extends ArrayList<Line> {

	private static final long serialVersionUID = -3034879922307296539L;
	
	private float width;
	private float height;
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	@Override
	public boolean add(Line e) {
		boolean b = super.add(e);
		updateBounds();
		return b;
	}
	
	@Override
	public Line remove(int index) {
		Line l = super.remove(index);
		updateBounds();
		return l;
	}
	
	@Override
	public void clear() {
		super.clear();
		updateBounds();
	}

	private void updateBounds() {
		this.width = (float)this.stream().mapToDouble(l -> l.getWidth()).max().orElse(0);
		this.height = (float)this.stream().mapToDouble(l -> l.getHeight()).sum();
	}

}
