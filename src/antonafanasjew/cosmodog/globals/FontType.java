package antonafanasjew.cosmodog.globals;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;



public enum FontType {

	GameLog(createUnicodeFont(new Font("Courier New", Font.PLAIN, 18), 18, false, false, Color.white)),
	GameLogHeader(createUnicodeFont(new Font("Courier New", Font.BOLD, 24), 24, true, false, Color.white)),
	PopUp(createUnicodeFont(new Font("Courier New", Font.PLAIN, 24), 24, false, false, Color.white)),
	PopUpInterface(createUnicodeFont(new Font("Courier New", Font.PLAIN, 24), 28, false, false, Color.red)),
	InGameMenuInterface(createUnicodeFont(new Font("Courier New", Font.PLAIN, 28), 28, false, false, Color.white)),
	InventoryDescription(createUnicodeFont(new Font("Courier New", Font.PLAIN, 24), 24, false, false, Color.white)),
	Test(createUnicodeFont(new Font("Arial", Font.PLAIN, 24), 72, true, true, Color.red)), ;
		
	private UnicodeFont font;
	
	@SuppressWarnings("unchecked")
	private static UnicodeFont createUnicodeFont(Font font, int size, boolean bold, boolean italic, Color color) {
		try {
			UnicodeFont unicodeFont = new UnicodeFont(font, size, bold, italic);
			unicodeFont.addAsciiGlyphs();
			unicodeFont.addGlyphs(400, 600);
			unicodeFont.getEffects().add(new ColorEffect(color));
			unicodeFont.loadGlyphs();
			return unicodeFont;
		} catch (SlickException e) {
			throw new RuntimeException("Error while loading font", e);
		}
	}
	
	private FontType(UnicodeFont font) {
		this.font = font;
	}

	public UnicodeFont getFont() {
		return font;
	}

}
