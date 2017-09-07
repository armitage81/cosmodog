package antonafanasjew.cosmodog.globals;

import java.awt.Color;
import java.awt.Font;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;



public enum FontType {

	References(createUnicodeFont(new Font("Arial", Font.PLAIN, 12), 12, true, false, Color.white)),
	MainMenu(createUnicodeFont(new Font("Courier New", Font.PLAIN, 24), 24, true, false, Color.red)),
	IntroText(createUnicodeFont(new Font("Courier New", Font.PLAIN, 18), 18, true, false, Color.white)),
	OutroText(createUnicodeFont(new Font("Courier New", Font.PLAIN, 18), 18, true, false, Color.white)),
	EndLabel(createUnicodeFont(new Font("Courier New", Font.PLAIN, 36), 36, true, false, Color.white)),
	CreditsText(createUnicodeFont(new Font("Courier New", Font.PLAIN, 18), 18, false, false, Color.white)),
	GameLog(createUnicodeFont(new Font("Courier New", Font.PLAIN, 16), 16, true, false, Color.white)),
	GameLogHeader(createUnicodeFont(new Font("Courier New", Font.BOLD, 12), 12, true, false, Color.ORANGE)),
	GameLogControlsHint(createUnicodeFont(new Font("Courier New", Font.BOLD, 12), 12, true, false, Color.GRAY)),
	PopUp(createUnicodeFont(new Font("Courier New", Font.PLAIN, 16), 16, false, false, Color.white)),
	PopUpHeader(createUnicodeFont(new Font("Courier New", Font.BOLD, 12), 12, true, false, Color.ORANGE)),
	PopUpInterface(createUnicodeFont(new Font("Arial", Font.PLAIN, 16), 16, true, false, Color.gray)),
	InGameMenuInterface(createUnicodeFont(new Font("Courier New", Font.PLAIN, 20), 20, false, false, Color.white)),
	InventoryDescription(createUnicodeFont(new Font("Courier New", Font.PLAIN, 24), 24, false, false, Color.white)),
	Test(createUnicodeFont(new Font("Arial", Font.PLAIN, 24), 72, true, true, Color.red)), 
	GameOver(createUnicodeFont(new Font("Arial", Font.BOLD, 72), 72, true, true, Color.red)),
	GameOverScore(createUnicodeFont(new Font("Courier New", Font.BOLD, 24), 24, true, true, Color.white)), 
	RadiationLabelDanger(createUnicodeFont(new Font("Arial", Font.BOLD, 10), 10, true, false, Color.red)),
	RadiationLabelSafe(createUnicodeFont(new Font("Arial", Font.BOLD, 14), 14, true, false, Color.green)),
	SuppliesTrackerLabel(createUnicodeFont(new Font("Arial", Font.BOLD, 10), 10, true, false, Color.green)),
	GameProgressLabel(createUnicodeFont(new Font("Courier New", Font.PLAIN, 14), 14, false, false, Color.white)), 
	GameProgressLabelInBars(createUnicodeFont(new Font("Arial", Font.BOLD, 20), 20, true, false, Color.red)),
	RecordsTitleLabel(createUnicodeFont(new Font("Courier New", Font.PLAIN, 36), 36, true, false, Color.white)), 
	Records(createUnicodeFont(new Font("Courier New", Font.PLAIN, 24), 24, true, false, Color.red)), 
	Loading(createUnicodeFont(new Font("Courier New", Font.PLAIN, 36), 36, true, false, Color.white)), 
	Hud(createUnicodeFont(new Font("Arial", Font.BOLD, 10), 10, true, false, Color.WHITE)), 
	HudTime(createUnicodeFont(new Font("Courier", Font.BOLD, 10), 10, true, false, Color.RED)),
	HudDayTime(createUnicodeFont(new Font("Courier", Font.BOLD, 10), 10, true, false, Color.RED));
		
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
