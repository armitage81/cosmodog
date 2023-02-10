package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;

import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;

public class Book extends ArrayList<Page> {

	private static final long serialVersionUID = 2365946535807501796L;
	
	private float width;
	private float height;
	
	private DrawingContext drawingContext;
	private FontRefToFontTypeMap fontRefToFontTypeMap;
	private int currentPage;
	private long millisWhenPageOpened;
	private boolean skipPageBuildUpRequest = false;
	private int timeBetweenWords;
	
	public Book(DrawingContext drawingContext, FontRefToFontTypeMap fontRefToFontTypeMap, int timeBetweenWords) {
		this.drawingContext = drawingContext;
		this.fontRefToFontTypeMap = fontRefToFontTypeMap;
		this.currentPage = 0;
		this.millisWhenPageOpened = System.currentTimeMillis();
		this.timeBetweenWords = timeBetweenWords;
	}
	
	public DrawingContext getDrawingContext() {
		return drawingContext;
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	@Override
	public boolean add(Page e) {
		boolean b = super.add(e);
		updateBounds();
		return b;
	}
	
	@Override
	public Page remove(int index) {
		Page p = super.remove(index);
		updateBounds();
		return p;
	}
	
	@Override
	public void clear() {
		super.clear();
		updateBounds();
	}

	private void updateBounds() {
		this.width = (float)this.stream().mapToDouble(l -> l.getWidth()).max().orElse(0);
		this.height = (float)this.stream().mapToDouble(l -> l.getHeight()).max().orElse(0);
	}

	public FontRefToFontTypeMap getFontRefToFontTypeMap() {
		return fontRefToFontTypeMap;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}

	public boolean onLastPage() {
		return currentPage == size() - 1;
	}
	
	public void nextPage() {
		if (currentPage < size() - 1) {
			currentPage++;
		}
		this.millisWhenPageOpened = System.currentTimeMillis();
		this.skipPageBuildUpRequest = false;
	}
	
	public void firstPage() {
		this.currentPage = 0;
		this.millisWhenPageOpened = System.currentTimeMillis();
		this.skipPageBuildUpRequest = false;
	}
	
	public long getMillisWhenPageOpened() {
		return millisWhenPageOpened;
	}
	
	public void setMillisWhenPageOpened(long millisWhenPageOpened) {
		this.millisWhenPageOpened = millisWhenPageOpened;
	}
	
	public boolean isSkipPageBuildUpRequest() {
		return skipPageBuildUpRequest;
	}
	
	public void setSkipPageBuildUpRequest(boolean skipPageBuildUpRequest) {
		this.skipPageBuildUpRequest = skipPageBuildUpRequest;
	}
	
	public int getTimeBetweenWords() {
		return timeBetweenWords;
	}
	
	public void resetTimeAfterPageOpen() {
		this.millisWhenPageOpened = System.currentTimeMillis();
	}
	
	public int numberOfWordsInCurrentDynamicPage(long referenceTime) {
		
		int numberOfWords = numberOfWordsInCurrentPage();
		
		if (skipPageBuildUpRequest) {
			return numberOfWords;
		}
		
		long timeSincePageOpened = referenceTime - millisWhenPageOpened;
		
		int retVal = timeBetweenWords == 0 ? numberOfWords : (int)(timeSincePageOpened / timeBetweenWords) + 1;
		
		if (retVal > numberOfWords) {
			retVal = numberOfWords;
		}
		
		return retVal;
	}
	
	public int numberOfWordsInCurrentPage() {
		Page page = get(currentPage);
		int wordsOnPage = page.stream().mapToInt(l -> l.size()).sum();
		return wordsOnPage;
	}
	
	public boolean dynamicPageComplete(long referenceTime) {
		
		if (skipPageBuildUpRequest) {
			return true;
		}
		
		int wordsToRender = numberOfWordsInCurrentDynamicPage(referenceTime);
		int numberOfWordsInPage = numberOfWordsInCurrentPage();
		return wordsToRender >= numberOfWordsInPage;
		
	}
	
}
