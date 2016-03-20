package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.Animations;
import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

public class DialogBoxRenderer implements Renderer {

	private WritingRenderer writingRenderer = new WritingRenderer();
	
	@Override
	public void render(GameContainer gameContainer, Graphics g, DrawingContext drawingContext, Object renderingParameter) {
		
		WritingTextBoxState textBoxState = (WritingTextBoxState)renderingParameter;
		
		DrawingContext dialogBoxDrawingContext = drawingContext;
		DrawingContext dialogBoxContentDrawingContext = new CenteredDrawingContext(dialogBoxDrawingContext, 5);
		
		g.setColor(Color.white);
		g.fillRoundRect(dialogBoxDrawingContext.x(), dialogBoxDrawingContext.y(), dialogBoxDrawingContext.w(), dialogBoxDrawingContext.h(), 5);
		
		g.setColor(new Color(0, 0, 205));
		g.fillRect(dialogBoxContentDrawingContext.x(), dialogBoxContentDrawingContext.y(), dialogBoxContentDrawingContext.w(), dialogBoxContentDrawingContext.h());
		DrawingContext d = dialogBoxContentDrawingContext; //Just as a shortcut variable
		
		if (textBoxState.completeBoxDisplayed()) {
			Animation animation;
			if (textBoxState.hasMoreBoxes()) {
				animation = ApplicationContext.instance().getAnimations().get("nextDialogBox");
			} else {
				animation = ApplicationContext.instance().getAnimations().get("closeDialogBox");
			}
			int size = 24;
			animation.draw(d.x() + d.w() - size, d.y() + d.h() - size, size, size);
		}
		
		DrawingContext textDrawingContext = DrawingContextUtils.writingContentDcFromDialogBoxDc(dialogBoxDrawingContext);
		writingRenderer.render(gameContainer, g, textDrawingContext, textBoxState);
		
		
	}
}
