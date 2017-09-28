package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.rendering.context.AbstractDrawingContext;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;

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

	
	public static final DrawingContext difResFromRef(DrawingContext dc, float newResWidth, float newResHeight) {
		return difRes(dc, Constants.REFERENCE_RESOLUTION_WIDTH, Constants.REFERENCE_RESOLUTION_HEIGHT, newResWidth, newResHeight);
	}
	
	/**
	 * This method transforms a drawing context to another drawing context to keep its relative position in case of different resolution.
	 * It is useful for transformation of statically set interface drawing contexts that need to keep their position in case of a different resolution.
	 */
	public static final DrawingContext difRes(DrawingContext dc, float resWidth, float resHeight, float newResWidth, float newResHeight) {
				
		if (resWidth == newResWidth && resHeight == newResHeight) {
			return dc;
		}
		
		if (dc instanceof AbstractDrawingContext) {
			AbstractDrawingContext adc = (AbstractDrawingContext)dc;
			if (adc.isRoot() == false) {
				//recursion
				DrawingContext parentDc = adc.getParentDrawingContext();
				parentDc = difRes(parentDc, resWidth, resHeight, newResWidth, newResHeight);
				adc.setParentDrawingContext(parentDc);
			}
		}
		
		float newResWidthToResWidth = newResWidth / resWidth;
		float newResHeightToResHeight = newResHeight / resHeight;
		
		return new SimpleDrawingContext(null, dc.x() * newResWidthToResWidth, dc.y() * newResHeightToResHeight, dc.w() * newResWidthToResWidth, dc.h() * newResHeightToResHeight);
		
	}
	
}
