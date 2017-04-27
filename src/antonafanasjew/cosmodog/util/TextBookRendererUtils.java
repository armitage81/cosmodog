package antonafanasjew.cosmodog.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;

public class TextBookRendererUtils {
	
	public static void renderCenteredLabel(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(text, fontType, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER, 0);
		TextBookRenderer.getInstance().render(gc, g, dc, param);
	}
	
	public static void renderVerticallyCenteredLabel(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(text, fontType, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_CENTER, 0);
		TextBookRenderer.getInstance().render(gc, g, dc, param);
	}
	
	public static void renderTextPage(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(text, fontType, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_START, 0);
		TextBookRenderer.getInstance().render(gc, g, dc, param);
	}
	
	public static void renderFromParam(GameContainer gc, Graphics g, DrawingContext dc, Object param) {
		TextBookRendererParameter textBookRenderingParam = (TextBookRendererParameter)param;
		TextBookRenderer.getInstance().render(gc, g, dc, textBookRenderingParam);
	}
	
}
