package antonafanasjew.cosmodog.text;

import org.newdawn.slick.Image;

/**
 * This class defines the representation of a character.
 */
public class Letter {

	public static final float LETTER_HEIGHT = 17;
	
	private char c;
	private float w;
	private Image image; 
	
	private Letter(char c, float w, Image image) {
		this.c = c;
		this.w = w;
		this.image = image;
	}

	public static Letter create(char c, float w, Image image) {
		return new Letter(c, w, image);
	}

	public char getC() {
		return c;
	}

	public float getW() {
		return w;
	}

	public Image getImage() {
		return image;
	}

}
