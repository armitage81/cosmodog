package antonafanasjew.cosmodog.rendering.renderer.textbook;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;

public class TextBookRenderer implements Renderer {
	
	public static class TextBookRendererParameter {
		
		public static final short ALIGN_START = 0;
		public static final short ALIGN_CENTER = 1;
		public static final short ALIGN_END = 2;
		
		public String text;
		public FontType fontType;
		public short horizontalAlignment;
		public short verticalAlignment;
		public int page;
		
		public static TextBookRendererParameter instance(String text, FontType fontType, short horAlign, short verAlign, int page) {
			TextBookRendererParameter retVal = new TextBookRendererParameter();
			retVal.text = text;
			retVal.fontType = fontType;
			retVal.horizontalAlignment = horAlign;
			retVal.verticalAlignment = verAlign;
			retVal.page = page;
			return retVal;
		}
		
	}
	
	
	public static TextBookRenderer instance = null;
	
	public static TextBookRenderer getInstance() {
		if (instance == null) {
			instance = new TextBookRenderer();
		}
		return instance;
	}
	
	private TextBookRenderer() {

	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		TextBookRendererParameter param = (TextBookRendererParameter)renderingParameter;
		
		String text = param.text;
		UnicodeFont font = param.fontType.getFont();
		
		
		TextPageConstraints c = new TextPageConstraints(drawingContext.w(), drawingContext.h());
		List<List<String>> splitText = c.textSplitByLinesAndPages(text, font);
		List<String> pageText = splitText.get(param.page);
		
		int linesInPage = pageText.size();
		float lineHeight = font.getLineHeight();
		int maxNumberOfLinesInPage = (int)c.getHeight() / (int)lineHeight;
		float usedUpVerticalSpace = lineHeight * linesInPage;
		float remainingVerticalSpace = c.getHeight() - usedUpVerticalSpace;
		
		int verticalOffset = (int)(param.verticalAlignment == TextBookRendererParameter.ALIGN_START ? 0 : (param.verticalAlignment == TextBookRendererParameter.ALIGN_END ? remainingVerticalSpace : remainingVerticalSpace / 2));
		
		
		for (int i = 0; i < linesInPage; i++) {
			String line = pageText.get(i);
			float lineWidth = font.getWidth(line);
			float remainingWidth = c.getWidth() - lineWidth;
			float horizontalOffset = (param.horizontalAlignment == TextBookRendererParameter.ALIGN_START ? 0 : (param.horizontalAlignment == TextBookRendererParameter.ALIGN_END ? remainingWidth : (remainingWidth / 2f)));
			
			TileDrawingContext lineDc = new TileDrawingContext(drawingContext, 1, maxNumberOfLinesInPage, 0, i);
			graphics.translate(horizontalOffset, verticalOffset);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			font.drawString(lineDc.x(), lineDc.y(), line);
			graphics.translate(-horizontalOffset, -verticalOffset);
		}
		
	}
	
}
