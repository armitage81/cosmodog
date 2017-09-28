package antonafanasjew.cosmodog.globals;

public class ResolutionHolder {

	private int width;
	private int height;
	
	private static ResolutionHolder instance = null;
	
	private ResolutionHolder() {

	}
	
	public static void set(int width, int height) {
		instance = new ResolutionHolder();
		instance.width = width;
		instance.height = height;
	}
	
	public static ResolutionHolder get() {
		return instance;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
