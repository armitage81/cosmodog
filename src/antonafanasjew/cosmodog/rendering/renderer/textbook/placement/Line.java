package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;

public class Line extends ArrayList<Word> {

	private static final long serialVersionUID = -7899468977903705704L;
	
	private float width;
	private float height;
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	@Override
	public boolean add(Word e) {
		boolean b = super.add(e);
		updateBounds();
		return b;
	}
	
	@Override
	public Word remove(int index) {
		Word w = super.remove(index);
		updateBounds();
		return w;
	}
	
	@Override
	public void clear() {
		super.clear();
		updateBounds();
	}

	private void updateBounds() {
		this.width = (float)this.stream().mapToDouble(w -> w.glyphDescriptor.width()).sum();
		this.height = (float)this.stream().mapToDouble(w -> w.glyphDescriptor.height()).max().orElse(0);
	}
	
}
