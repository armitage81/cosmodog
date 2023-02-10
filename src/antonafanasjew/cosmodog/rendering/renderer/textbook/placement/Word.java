package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;


/*
 * A word can be either a string of characters, a single space, a single linebreak or a single pagebreak
 * linebreaks and pagebreaks do not have widths
 * 
 */
public class Word {
	
	public String text;
	public GlyphDescriptor glyphDescriptor;
	public boolean space;
	public boolean textBased;
	public boolean lineBreak;
	public boolean pageBreak;
	
	public static Word textBased(String text, GlyphDescriptor glyphDescriptor) {
		Word word = new Word();
		word.text = text;
		word.glyphDescriptor = glyphDescriptor;
		word.textBased = true;
		word.space = false;
		word.lineBreak = false;
		word.pageBreak = false;
		return word;
	}
	
	public static Word space(GlyphDescriptor glyphDescriptor) {
		Word word = new Word();
		word.text = null;
		word.glyphDescriptor = glyphDescriptor;
		word.textBased = false;
		word.space = true;
		word.lineBreak = false;
		word.pageBreak = false;
		return word;
	}
	
	public static Word lineBreak() {
		Word word = new Word();
		word.text = null;
		word.glyphDescriptor = GlyphDescriptorImpl.forControlCharacter();
		word.textBased = false;
		word.space = false;
		word.lineBreak = true;
		word.pageBreak = false;
		return word;
	}
	
	public static Word pageBreak() {
		Word word = new Word();
		word.text = null;
		word.glyphDescriptor = GlyphDescriptorImpl.forControlCharacter();
		word.textBased = false;
		word.space = false;
		word.lineBreak = false;
		word.pageBreak = true;
		return word;
	}
	
	@Override
	public String toString() {
		if (pageBreak) {
			return "<div>";
		} else if (lineBreak) {
			return "<p>";
		} else if (space) {
			return "<space>";
		} else {
			return text;
		}
	}
}
