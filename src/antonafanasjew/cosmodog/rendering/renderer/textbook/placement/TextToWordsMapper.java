package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.TrueTypeFont;

import antonafanasjew.cosmodog.globals.FontProvider;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;

public class TextToWordsMapper {

	private FontProvider fontProvider = FontProvider.getInstance();
	
	public List<Word> map(String text, FontRefToFontTypeMap fontRefToFontTypeMap) {

		text = text.replaceAll("<p>", "<p><p>");
		text = text.replaceAll("<br>", "<p>");
		
		text = text.replaceAll("\r\n\r\n", "<div>");
		text = text.replaceAll("\r\n", "<p><p>");
		
		text = text.replaceAll("<p>", " <p> ");
		text = text.replaceAll("<div>", " <div> ");
		
		List<Word> words = new ArrayList<>();
		
		String[] parts = text.split("\\s+");
		
		String currentFontRef = FontRefToFontTypeMap.FONT_REF_DEFAULT;
		FontTypeName fontTypeName = fontRefToFontTypeMap.get(currentFontRef);
		FontType fontType = fontProvider.fontType(fontTypeName);
		TrueTypeFont font = fontType.getFont();
		
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.startsWith("<font:") && part.endsWith(">")) {
				currentFontRef = part.substring(6, part.length() - 1);
			} else if (part.equals("<div>")) {
				words.add(Word.pageBreak());
			} else if (part.equals("<p>")) {
				words.add(Word.lineBreak());
			} else {
				words.add(Word.textBased(part, GlyphDescriptorImpl.of(font.getWidth(part), font.getLineHeight(), currentFontRef)));
				if (i < parts.length - 1) {
					words.add(Word.space(GlyphDescriptorImpl.of(font.getWidth(" "), font.getLineHeight(), FontRefToFontTypeMap.FONT_REF_DEFAULT)));
				}
			}
		}
		
		return words;
		
	}

}
