package antonafanasjew.cosmodog.globals;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;



public enum FontType {

	GameLog(createUnicodeFont(new Font("Verdana", Font.PLAIN, 1), 24, false, false), Color.white),
	Test(createUnicodeFont(new Font("Arial", Font.PLAIN, 1), 72, true, true), Color.red);
	
	private UnicodeFont font;
	private Color color;
	
	@SuppressWarnings("unchecked")
	private static UnicodeFont createUnicodeFont(Font font, int size, boolean bold, boolean italic) {
		try {
			UnicodeFont unicodeFont = new UnicodeFont(font, size, bold, italic);
			unicodeFont.addAsciiGlyphs();
			unicodeFont.addGlyphs(400, 600);
			unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
			unicodeFont.loadGlyphs();
			return unicodeFont;
		} catch (SlickException e) {
			throw new RuntimeException("Error while loading font", e);
		}
	}
	
	private FontType(UnicodeFont font, Color color) {
		this.font = font;
		this.color = color;
	}

	public UnicodeFont getFont() {
		return font;
	}

	public Color getColor() {
		return color;
	}

}
