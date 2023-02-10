package antonafanasjew.cosmodog.rendering.renderer.textbook.placement;

import java.util.HashMap;
import java.util.Map;

import antonafanasjew.cosmodog.globals.FontType;

/*
 * When calculating placement for text, this descriptor
 * can be attached to each word to describe the font and its measures for word's width and height.
 * 
 * We could have used some kind of a Font class for this purpose, 
 * but the corresponding classes are quite heavy-weight and not easy to use
 * in unit tests.
 * 
 * Take care: It is important that the font which the fontRef property refers to
 * is the same font as which was used to define the width and the height properties.
 * Otherwise, there will be a discrepancy between the placement calculation and the actual rendering.
 * 
 */
public class GlyphDescriptorImpl implements GlyphDescriptor {

	private float width;
	private float height;
	private String fontRef;
	
	public static final Map<String, FontType> FONT_REFS_MAP = new HashMap<>();
	
	public static GlyphDescriptorImpl of(float width, float height, String fontRef) {
		GlyphDescriptorImpl o  = new GlyphDescriptorImpl();
		o.width = width;
		o.height = height;
		o.fontRef = fontRef;
		return o;
	}
	
	public static GlyphDescriptorImpl forControlCharacter() {
		GlyphDescriptorImpl o  = new GlyphDescriptorImpl();
		o.width = 0;
		o.height = 0;
		o.fontRef = null;
		return o;
	}
	
	@Override
	public float width() {
		return width;
	}

	@Override
	public float height() {
		return height;
	}

	@Override
	public String fontRef() {
		return fontRef;
	}

}
