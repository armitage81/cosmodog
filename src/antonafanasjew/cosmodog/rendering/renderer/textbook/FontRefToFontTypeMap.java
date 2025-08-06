package antonafanasjew.cosmodog.rendering.renderer.textbook;

import java.util.HashMap;

import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;

public class FontRefToFontTypeMap extends HashMap<String, FontTypeName> {

	private static final long serialVersionUID = 1435422704669264731L;
	
	public static final String FONT_REF_DEFAULT = "default";
	
	public static final String FONT_REF_NARRATION = "narration";
	public static final String FONT_REF_NARRATION_MEMORY = "memory";
	public static final String FONT_REF_NARRATION_IMPORTANT = "important";
	public static final String FONT_REF_NARRATION_SPEECH = "speech";
	public static final String FONT_REF_NARRATION_SPEECH_IMPORTANT = "shout";
	
	public static final String FONT_REF_OVERHEAD_CRITICAL = "critical";
	
	public static FontRefToFontTypeMap forNarration() {
		FontRefToFontTypeMap map = new FontRefToFontTypeMap();
		
		map.put(FONT_REF_DEFAULT, FontTypeName.Narration);
		map.put(FONT_REF_NARRATION, FontTypeName.Narration);
		map.put(FONT_REF_NARRATION_MEMORY, FontTypeName.NarrationMemory);
		map.put(FONT_REF_NARRATION_IMPORTANT, FontTypeName.NarrationImportant);
		map.put(FONT_REF_NARRATION_SPEECH, FontTypeName.NarrationSpeech);
		map.put(FONT_REF_NARRATION_SPEECH_IMPORTANT, FontTypeName.NarrationSpeechImportant);
		
		return map;
	}
	
	public static FontRefToFontTypeMap forEmphasizedNarration() {
		FontRefToFontTypeMap map = forNarration();
		map.put(FONT_REF_DEFAULT, FontTypeName.EmphasizedNarration);
		map.put(FONT_REF_NARRATION, FontTypeName.EmphasizedNarration);
		
		return map;
	}
	
	public static FontRefToFontTypeMap forOverhead() {
		FontRefToFontTypeMap map = new FontRefToFontTypeMap();
		
		map.put(FONT_REF_DEFAULT, FontTypeName.Overhead);
		map.put(FONT_REF_OVERHEAD_CRITICAL, FontTypeName.OverheadCritical);
		
		return map;
	}
	
	public static FontRefToFontTypeMap forOneFontTypeName(FontTypeName fontTypeName) {
		FontRefToFontTypeMap map = new FontRefToFontTypeMap();
		map.put(FONT_REF_DEFAULT, fontTypeName);
		return map;
	}

}
