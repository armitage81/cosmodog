package antonafanasjew.cosmodog.globals;

public class FontProvider {

	private static FontProvider instance = new FontProvider();
		
	public static enum FontTypeName {
		
		Overhead(FontType.Overhead, FontType.Overhead_LowRes),
		OverheadCritical(FontType.OverheadCritical, FontType.OverheadCritical_LowRes),
		LoadingOrGameOverOrTheEnd(FontType.LoadingOrGameOverOrTheEnd, FontType.LoadingOrGameOverOrTheEnd_LowRes),
		MainMenu(FontType.StartMenu, FontType.StartMenu_LowRes),
		
		Narration(FontType.Narration, FontType.Narration_LowRes),
		NarrationImportant(FontType.NarrationImportant, FontType.NarrationImportant_LowRes),
		NarrationSpeech(FontType.NarrationSpeech, FontType.NarrationSpeech_LowRes),
		NarrationSpeechImportant(FontType.NarrationSpeechImportant, FontType.NarrationSpeechImportant_LowRes),
		
		EmphasizedNarration(FontType.EmphasizedNarration, FontType.EmphasizedNarration_LowRes),
		Informational(FontType.Informational, FontType.Informational_LowRes),
		InformationalSmall(FontType.InformationalSmall, FontType.InformationalSmall_LowRes),
		ControlsHint(FontType.ControlsHint, FontType.ControlsHint_LowRes),
		MainHeader(FontType.MainHeader, FontType.MainHeader_LowRes),
		SubHeader(FontType.SubHeader, FontType.SubHeader_LowRes),
		LicenseText(FontType.LicenseText, FontType.LicenseText_LowRes),
		Credits(FontType.CreditsText, FontType.CreditsText_LowRes),
		OverheadNotification(FontType.OverheadNotification, FontType.OverheadNotification_LowRes),
		HudLabels(FontType.HudLabels, FontType.HudLabels_LowRes),
		HudValues(FontType.HudValues, FontType.HudValues_LowRes),
		Debug(FontType.Debug, FontType.Debug_LowRes),
		;
		
		private FontType fontType;
		private FontType fontTypeLowRes;
		
		private FontTypeName(FontType fontType, FontType fontTypeLowRes) {
			this.fontType = fontType;
			this.fontTypeLowRes = fontTypeLowRes;
		}
		
		public FontType getFontType() {
			return fontType;
		}
		
		public FontType getFontTypeLowRes() {
			return fontTypeLowRes;
		}
		
	}
	
	private FontProvider() {

	}
	
	public static FontProvider getInstance() {
		return instance;
	}
	
	public FontType fontType(FontTypeName fontTypeName, boolean hd) {
		if (hd) {
			return fontTypeName.getFontType();
		} else {
			return fontTypeName.getFontTypeLowRes();
		}
	}
	
	public FontType fontType(FontTypeName fontTypeName) {
		boolean hd = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext().w() >= 1920;
		return fontType(fontTypeName, hd);
	}
}
