package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.narration.DialogWithAlisaNarrationAction;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;

public class CutsceneRenderer extends AbstractRenderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		long referenceTime = System.currentTimeMillis();
		
		DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext controlsDc = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneControlsDrawingContext();
		
		Book openBook = ApplicationContextUtils.getCosmodogGame().getOpenBook();
		
		if (openBook == null) {
			return;
		}

		DialogWithAlisaNarrationAction dialogWithAlisaNarrationAction =
				ApplicationContextUtils
						.getCosmodogGame()
						.getInterfaceActionRegistry()
						.getRegisteredAction(AsyncActionType.MODAL_WINDOW, DialogWithAlisaNarrationAction.class);
		
		if (dialogWithAlisaNarrationAction != null) {
			
			graphics.setColor(Color.black);
			graphics.fillRect(
					dc.x(),
					dc.y(),
					dc.w(),
					dc.h());

			Animation cutsceneBackground = ApplicationContext.instance().getAnimations().get("cutsceneAlisa");
			
			DialogWithAlisaNarrationAction.ActionPhase phase = dialogWithAlisaNarrationAction.phase;
			float phaseCompletion = dialogWithAlisaNarrationAction.phaseCompletion();
			
			if (phase == DialogWithAlisaNarrationAction.ActionPhase.ARM_APPEARS) {
				
				float xOffset = -(dc.w() * (1 - phaseCompletion));
				
				cutsceneBackground.draw(
						dc.x() + xOffset, 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
			}
			
			if (phase == DialogWithAlisaNarrationAction.ActionPhase.DEVICE_TURNS_ON) {
				
				cutsceneBackground.draw(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
			}
			
			if (phase == DialogWithAlisaNarrationAction.ActionPhase.PICTURE_FADES) {
				
				cutsceneBackground.draw(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
				float textPageOpacity = (float)(phaseCompletion * DialogWithAlisaNarrationAction.MAX_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
			}
			
			if (phase == DialogWithAlisaNarrationAction.ActionPhase.TEXT) {
				
				cutsceneBackground.draw(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
				
				graphics.setColor(new Color(0f, 0f, 0f, DialogWithAlisaNarrationAction.MAX_PICTURE_OPACITY));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
				
				TextBookRendererUtils.renderDynamicTextPage(gameContainer, graphics, openBook);
				
				
				boolean renderHint = openBook.dynamicPageComplete(referenceTime);
				boolean renderBlinkingHint = (referenceTime / 250 % 2) == 1;
				if (renderHint && renderBlinkingHint) {
					FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.ControlsHint);
					Book controlHint = TextPageConstraints.fromDc(controlsDc).textToBook("Press [ENTER]", fontRefToFontTypeMap);
					TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, controlHint);
				}
			}
		}
		
		firstLoop = false;
	}

}
