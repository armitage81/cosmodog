package antonafanasjew.cosmodog.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;

public class TextBookRendererUtils {
	
	public static void renderCenteredLabel(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType, int page) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(text, fontType, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER, page);
		TextBookRenderer.getInstance().withDrawingContext(dc).render(gc, g, param);
	}
	
	public static void renderVerticallyCenteredLabel(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType, int page) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(text, fontType, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_CENTER, page);
		TextBookRenderer.getInstance().withDrawingContext(dc).render(gc, g, param);
	}
	
	public static void renderTextPage(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType, int page) {
		renderDynamicTextPage(gc, g, dc, text, fontType, page, -1);
	}
	
	public static void renderDynamicTextPage(GameContainer gc, Graphics g, DrawingContext dc, String text, FontType fontType, int page, long millisSinsePageOpened) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(text, fontType, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_START, page, millisSinsePageOpened);
		TextBookRenderer.getInstance().withDrawingContext(dc).render(gc, g, param);
	}
	
	public static void renderFromParam(GameContainer gc, Graphics g, DrawingContext dc, Object param) {
		TextBookRendererParameter textBookRenderingParam = (TextBookRendererParameter)param;
		TextBookRenderer.getInstance().withDrawingContext(dc).render(gc, g, textBookRenderingParam);
	}
	
}
