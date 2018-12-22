package antonafanasjew.cosmodog.globals;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;



public enum FontType {

	References(new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), false), Color.black),
	MainMenu(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.red),
	IntroText(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.green),
	OutroText(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.green),
	Outro2Text(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.orange),
	EndLabel(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 36), false), Color.white),
	CreditsText(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.white),
	Cutscene(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.yellow),
	GameLog(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 16), false), Color.white),
	GameLogHeader(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 16), false), Color.white),
	GameLogControlsHint(new TrueTypeFont(new Font("Courier New", Font.BOLD, 12), false), Color.white),
	PopUp(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 16), false), Color.white),
	PopUpHeader(new TrueTypeFont(new Font("Courier New", Font.BOLD, 18), false), Color.orange),
	PopUpInterface(new TrueTypeFont(new Font("Arial", Font.PLAIN, 16), false), Color.white),
	InGameMenuInterface(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 20), false), Color.white),
	InventoryDescription(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.white),
	Test(new TrueTypeFont(new Font("Arial", Font.PLAIN, 24), false), Color.white), 
	GameOver(new TrueTypeFont(new Font("Arial", Font.BOLD, 36), false), Color.red),
	GameOverScore(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.white), 
	RadiationLabelDanger(new TrueTypeFont(new Font("Arial", Font.BOLD, 10), false), Color.white),
	RadiationLabelSafe(new TrueTypeFont(new Font("Arial", Font.BOLD, 14), false), Color.white),
	SuppliesTrackerLabel(new TrueTypeFont(new Font("Arial", Font.BOLD, 10), false), Color.white),
	GameProgressLabel(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 14), false), Color.white), 
	GameProgressLabelInBars(new TrueTypeFont(new Font("Arial", Font.BOLD, 20), false), Color.white),
	RecordsTitleLabel(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 36), false), Color.white), 
	Records(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.white), 
	Loading(new TrueTypeFont(new Font("Courier New", Font.BOLD, 48), false), Color.red), 
	Hud(new TrueTypeFont(new Font("Arial", Font.BOLD, 16), false), Color.green),
	HudInfobits(new TrueTypeFont(new Font("Courier", Font.BOLD, 26), false), Color.green),
	HudTime(new TrueTypeFont(new Font("Courier", Font.BOLD, 12), false), Color.green),
	HudDayTime(new TrueTypeFont(new Font("Courier", Font.BOLD, 14), false), Color.green),
	HintsActions(new TrueTypeFont(new Font("Courier", Font.BOLD, 14), false), Color.white),
	HintsKeys(new TrueTypeFont(new Font("Courier", Font.BOLD, 14), false), Color.white),
	StatisticsLabel(new TrueTypeFont(new Font("Courier", Font.BOLD, 18), false), Color.white),
	StatisticsHeader(new TrueTypeFont(new Font("Courier", Font.BOLD, 36), false), Color.white);
		
	private TrueTypeFont font;
	
	private Color color;
	
	
	private FontType(TrueTypeFont font, Color color) {
		this.font = font;
		this.color = color;
	}

	public TrueTypeFont getFont() {
		return font;
	}
	
	public Color getColor() {
		return color;
	}

}
