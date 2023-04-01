package antonafanasjew.cosmodog.globals;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;



public enum FontType {

	Overhead(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), new Color(0.0f, 0.7f, 0.0f)),
	OverheadCritical(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.red),
	LoadingOrGameOverOrTheEnd(new TrueTypeFont(new Font("Courier New", Font.BOLD, 36), false), Color.red),
	StartMenu(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.red),
	Narration(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.green),
	NarrationImportant(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.red),
	NarrationSpeech(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.yellow),
	NarrationSpeechImportant(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.red),
	
	EmphasizedNarration(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.orange),
	Informational(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.white),
	InformationalSmall(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 20), false), Color.white),
	ControlsHint(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.gray),
	MainHeader(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.orange),
	SubHeader(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.orange),
	LicenseText(new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), false), Color.black),
	CreditsText(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.white),
	OverheadNotification(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.green),
	HudLabels(new TrueTypeFont(new Font("Arial", Font.BOLD, 16), false), Color.orange),
	HudValues(new TrueTypeFont(new Font("Courier", Font.BOLD, 20), false), Color.orange),
	Debug(new TrueTypeFont(new Font("Courier", Font.BOLD, 36), false), Color.red),
	
	Overhead_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), true), new Color(0.0f, 0.7f, 0.0f)),
	OverheadCritical_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), true), Color.red),
	LoadingOrGameOverOrTheEnd_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.red),
	StartMenu_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 24), false), Color.red),
	Narration_LowRes(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 20), false), Color.green),
	NarrationImportant_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.red),
	NarrationSpeech_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.yellow),
	NarrationSpeechImportant_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.red),
	
	EmphasizedNarration_LowRes(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 20), false), Color.orange),
	Informational_LowRes(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 18), false), Color.white),
	InformationalSmall_LowRes(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 16), false), Color.white),
	ControlsHint_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.gray),
	MainHeader_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.orange),
	SubHeader_LowRes(new TrueTypeFont(new Font("Courier New", Font.BOLD, 20), false), Color.orange),
	LicenseText_LowRes(new TrueTypeFont(new Font("Arial", Font.PLAIN, 12), false), Color.black),
	CreditsText_LowRes(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 14), false), Color.white),
	OverheadNotification_LowRes(new TrueTypeFont(new Font("Courier New", Font.PLAIN, 24), false), Color.green),
	HudLabels_LowRes(new TrueTypeFont(new Font("Arial", Font.BOLD, 10), false), Color.orange),
	HudValues_LowRes(new TrueTypeFont(new Font("Courier", Font.BOLD, 14), false), Color.orange),
	Debug_LowRes(new TrueTypeFont(new Font("Courier", Font.BOLD, 36), false), Color.red);
	
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
