package antonafanasjew.cosmodog.util;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;

public class TextBookRendererUtils {
	
	public static void renderLeftAlignedLabel(GameContainer gc, Graphics g, Book book) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(book, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_CENTER);
		TextBookRenderer.getInstance().withDrawingContext(book.getDrawingContext()).render(gc, g, param);
	}
	
	public static void renderCenteredLabel(GameContainer gc, Graphics g, Book book) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(book, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER, false);
		TextBookRenderer.getInstance().withDrawingContext(book.getDrawingContext()).render(gc, g, param);
	}
	
	public static void renderCenteredLabel(GameContainer gc, Graphics g, Book book, boolean framed) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(book, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER, framed);
		TextBookRenderer.getInstance().withDrawingContext(book.getDrawingContext()).render(gc, g, param);
	}
	
	public static void renderVerticallyCenteredLabel(GameContainer gc, Graphics g, Book book) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(book, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_CENTER);
		TextBookRenderer.getInstance().withDrawingContext(book.getDrawingContext()).render(gc, g, param);
	}
	
	public static void renderDynamicTextPage(GameContainer gc, Graphics g, Book book) {
		TextBookRendererParameter param = TextBookRendererParameter.instance(book, TextBookRendererParameter.ALIGN_START, TextBookRendererParameter.ALIGN_START);
		TextBookRenderer.getInstance().withDrawingContext(book.getDrawingContext()).render(gc, g, param);
	}
	
	public static void renderFromParam(GameContainer gc, Graphics g, Object param) {
		TextBookRendererParameter textBookRenderingParam = (TextBookRendererParameter)param;
		TextBookRenderer.getInstance().withDrawingContext(textBookRenderingParam.book.getDrawingContext()).render(gc, g, textBookRenderingParam);
	}
	
}
