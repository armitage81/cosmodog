package antonafanasjew.cosmodog.globals;

import antonafanasjew.cosmodog.AbstractDrawingContextProvider;

public class DrawingContextProviderHolder {

	private AbstractDrawingContextProvider drawingContextProvider;
	
	private static DrawingContextProviderHolder instance = null;
	
	private DrawingContextProviderHolder() {

	}
	
	public static void set(int width, int height) {
		instance = new DrawingContextProviderHolder();
		instance.drawingContextProvider = AbstractDrawingContextProvider.createDrawingContextProviderForResolution(width, height);
	}
	
	public static DrawingContextProviderHolder get() {
		return instance;
	}
	
	public AbstractDrawingContextProvider getDrawingContextProvider() {
		return drawingContextProvider;
	}
	
}
