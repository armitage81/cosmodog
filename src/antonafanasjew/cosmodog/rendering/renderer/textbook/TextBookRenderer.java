package antonafanasjew.cosmodog.rendering.renderer.textbook;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;

public class TextBookRenderer implements Renderer {
	
	public static final int PAGE_APPEARANCE_DURATION = 8000;
//	public static final int LINE_APPEARANCE_DURATION = 50;
	
	private DrawingContext drawingContext;
	
	public static class TextBookRendererParameter {
		
		public static final short ALIGN_START = 0;
		public static final short ALIGN_CENTER = 1;
		public static final short ALIGN_END = 2;
		
		public String text;
		public FontType fontType;
		public short horizontalAlignment;
		public short verticalAlignment;
		public int page;
		public long millisSincePageOpened; 
		
		public static TextBookRendererParameter instance(String text, FontType fontType, short horAlign, short verAlign, int page, long millisSinsePageOpened) {
			TextBookRendererParameter retVal = new TextBookRendererParameter();
			retVal.text = text;
			retVal.fontType = fontType;
			retVal.horizontalAlignment = horAlign;
			retVal.verticalAlignment = verAlign;
			retVal.page = page;
			retVal.millisSincePageOpened = millisSinsePageOpened;
			return retVal;
		}
		
		public static TextBookRendererParameter instance(String text, FontType fontType, short horAlign, short verAlign, int page) {
			return instance(text, fontType, horAlign, verAlign, page, -1);
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
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		TextBookRendererParameter param = (TextBookRendererParameter)renderingParameter;
		
		String text = param.text;
		Color color = param.fontType.getColor();
		TrueTypeFont font = param.fontType.getFont();
		
		/*
		graphics.setColor(Color.red);
		graphics.drawRect(drawingContext.x(), drawingContext.y(), drawingContext.w(), drawingContext.h());
		*/
		
		TextPageConstraints c = new TextPageConstraints(drawingContext.w(), drawingContext.h());
		List<List<String>> splitText = c.textSplitByLinesAndPages(text, font);
		List<String> pageText = splitText.get(param.page);
		
		int charactersOnPage = pageText.stream().mapToInt(String::length).sum();
		int charactersToRender;
		
		int linesInPage = pageText.size();
		float lineHeight = font.getLineHeight();
		int maxNumberOfLinesInPage = (int)c.getHeight() / (int)lineHeight;
		float usedUpVerticalSpace = lineHeight * linesInPage;
		float remainingVerticalSpace = c.getHeight() - usedUpVerticalSpace;
		
		int verticalOffset = (int)(param.verticalAlignment == TextBookRendererParameter.ALIGN_START ? 0 : (param.verticalAlignment == TextBookRendererParameter.ALIGN_END ? remainingVerticalSpace : remainingVerticalSpace / 2));
		
		int pageAppearanceDurationForNotFullyFilledPage;
		if (maxNumberOfLinesInPage > 0) {
			pageAppearanceDurationForNotFullyFilledPage = (PAGE_APPEARANCE_DURATION / maxNumberOfLinesInPage) * pageText.size();
		} else {
			pageAppearanceDurationForNotFullyFilledPage = 0;
		}
				
		if (param.millisSincePageOpened >= pageAppearanceDurationForNotFullyFilledPage) {
			charactersToRender = charactersOnPage;
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
		} else {
			charactersToRender = (int)(charactersOnPage * param.millisSincePageOpened / (float)pageAppearanceDurationForNotFullyFilledPage);
		}
		
		boolean dynamicPage = param.millisSincePageOpened >= 0;

		int accumulatedCharacters = 0;
		
		for (int i = 0; i < linesInPage; i++) {
			String line = pageText.get(i);
			float lineWidth = font.getWidth(line);
			float remainingWidth = c.getWidth() - lineWidth;
			float horizontalOffset = (param.horizontalAlignment == TextBookRendererParameter.ALIGN_START ? 0 : (param.horizontalAlignment == TextBookRendererParameter.ALIGN_END ? remainingWidth : (remainingWidth / 2f)));
			
			DrawingContext lineDc = new TileDrawingContext(drawingContext, 1, maxNumberOfLinesInPage, 0, i);
			graphics.translate((int)horizontalOffset, (int)verticalOffset);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			String partialLineText;
			
			if (!dynamicPage) {
				partialLineText = line;
			} else {
				if (accumulatedCharacters >= charactersToRender) {
					partialLineText = "";
				} else {
					if (accumulatedCharacters + line.length() < charactersToRender) {
						partialLineText = line;
					} else {
						int rest = charactersToRender - accumulatedCharacters;
						partialLineText = line.substring(0, rest);
					}
				}
			}
			accumulatedCharacters += line.length();
			
//			if (dynamicPage) {
//				
//				long duration = param.millisSincePageOpened;
//				
//				if (duration < PAGE_APPEARANCE_DURATION + LINE_APPEARANCE_DURATION) {
//					float alpha = 0.1f * (lastVisibleLine - i);
//					color = new Color(color.r, color.g, color.b, alpha);	
//				}
//				
//					
//			}
			
			font.drawString((int)lineDc.x(), (int)lineDc.y(), partialLineText, color);
//			if (dynamicPage && i == lastVisibleLine) {
//				graphics.setColor(new Color(1f, 0f, 0f, 0.5f));
//				graphics.fillRect(lineDc.x(), lineDc.y(), lineDc.w(), lineDc.h());
//			}
			graphics.translate(-(int)horizontalOffset, -(int)verticalOffset);
		}
		
	}
	
	public TextBookRenderer withDrawingContext(DrawingContext drawingContext) {
		this.drawingContext = drawingContext;
		return this;
	}
	
}
