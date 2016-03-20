package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;

public class DrawingContextUtils {
	
	/**
	 * This needs to be calculated centrally as both WritingTextBox and WritingRenderer have to use
	 * synchronized drawing contexts (otherwise, the text will go over the bounds of the dialog box, actually a design flow)
	 */
	public static final DrawingContext writingContentDcFromDialogBoxDc(DrawingContext dialogBoxDc) {
		return new CenteredDrawingContext(dialogBoxDc, 15);
	}
	
	/**
	 * This needs to be calculated centrally as both WritingTextBox and WritingRenderer have to use
	 * synchronized drawing contexts (otherwise, the text will go over the bounds of the dialog box, actually a design flow)
	 */
	public static final DrawingContext writingContentDcFromTutorialBoxDc(DrawingContext tutorialBoxDc) {
		return new CenteredDrawingContext(tutorialBoxDc, 15);
	}
	
}
