package antonafanasjew.cosmodog.rendering.renderer.textbook;

import java.util.ArrayList;
import java.util.List;

import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.globals.FontProvider;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Line;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Page;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Word;

public class TextBookRenderer extends AbstractRenderer {
	
	private DrawingContext drawingContext;
	
	public static class TextBookRendererParameter {
		
		public static final short ALIGN_START = 0;
		public static final short ALIGN_CENTER = 1;
		public static final short ALIGN_END = 2;
		
		public Book book;
		public short horizontalAlignment;
		public short verticalAlignment;
		boolean framed;
		
		public static TextBookRendererParameter instance(Book book, short horAlign, short verAlign, boolean framed) {
			TextBookRendererParameter retVal = new TextBookRendererParameter();
			retVal.book = book;
			retVal.horizontalAlignment = horAlign;
			retVal.verticalAlignment = verAlign;
			retVal.framed = framed;
			return retVal;
		}
		
		public static TextBookRendererParameter instance(Book book, short horAlign, short verAlign) {
			return instance(book, horAlign, verAlign, false);
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
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {
		
		long referenceTime = System.currentTimeMillis();
		
		TextBookRendererParameter param = (TextBookRendererParameter)renderingParameter;
		
		Book book = param.book;
		boolean framed = param.framed;
		
		TextPageConstraints c = TextPageConstraints.fromDc(drawingContext);
		Page page = book.get(book.getCurrentPage());
		int numberOfLines = page.size();
		
		
		//This must be changed.
		float lineHeight = page.get(0).get(0).glyphDescriptor.height();
		int maxNumberOfLinesInPage = (int)c.getHeight() / (int)lineHeight;
		float usedUpVerticalSpace = lineHeight * numberOfLines;
		float remainingVerticalSpace = c.getHeight() - usedUpVerticalSpace;
		
		int verticalOffset = (int)(param.verticalAlignment == TextBookRendererParameter.ALIGN_START ? 0 : (param.verticalAlignment == TextBookRendererParameter.ALIGN_END ? remainingVerticalSpace : remainingVerticalSpace / 2));
		
		int wordsToRender = book.numberOfWordsInCurrentDynamicPage(referenceTime);
		
		if (book.dynamicPageComplete(referenceTime)) {
			ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TEXT_TYPING).stop();
		}
		
		int accumulatedWords = 0;
		for (int i = 0; i < numberOfLines; i++) {
			Line line = page.get(i);
			
			float lineWidth = line.getWidth();
			float remainingWidth = c.getWidth() - lineWidth;
			float horizontalOffset = (param.horizontalAlignment == TextBookRendererParameter.ALIGN_START ? 0 : (param.horizontalAlignment == TextBookRendererParameter.ALIGN_END ? remainingWidth : (remainingWidth / 2f)));
			
			DrawingContext lineDc = new TileDrawingContext(drawingContext, 1, maxNumberOfLinesInPage, 0, i);
			graphics.translate((int)horizontalOffset, (int)verticalOffset);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			List<Word> partialLineText;
			
			if (accumulatedWords >= wordsToRender) {
				partialLineText = new ArrayList<>();
			} else {
				if (accumulatedWords + line.size() < wordsToRender) {
					partialLineText = line;
				} else {
					int rest = wordsToRender - accumulatedWords;
					partialLineText = line.subList(0, rest);
				}
			}
			
			//Draw frame
			if (framed && line.getWidth() > 0) {
				int padding = 2;
				graphics.setColor(Color.white);
				graphics.fillRoundRect(lineDc.x() - padding, lineDc.y() - padding, line.getWidth() + 2 * padding, line.getHeight() + 2 * padding, 10);
			}
			
			accumulatedWords += line.size();
			float placementX = 0.0f;
			for (Word word : partialLineText) {
				String fontRef = word.glyphDescriptor.fontRef();
				FontTypeName fontTypeName = book.getFontRefToFontTypeMap().get(fontRef);
				String wordText = word.textBased ? word.text : " ";
				FontType fontType = FontProvider.getInstance().fontType(fontTypeName);
				TrueTypeFont font = fontType.getFont();
				Color color = fontType.getColor();
				graphics.setFont(font);
				graphics.setColor(color);
				graphics.drawString(wordText, lineDc.x() + placementX, lineDc.y());
				placementX += word.glyphDescriptor.width();
			}


			graphics.translate(-(int)horizontalOffset, -(int)verticalOffset);
		}
		
	}
	
	public TextBookRenderer withDrawingContext(DrawingContext drawingContext) {
		this.drawingContext = drawingContext;
		return this;
	}
	
}
