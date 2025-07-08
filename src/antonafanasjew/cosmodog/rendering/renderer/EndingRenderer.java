package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.narration.EndingNarrationAction;
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

public class EndingRenderer extends AbstractRenderer {

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

		EndingNarrationAction endingNarrationAction =
				ApplicationContextUtils
						.getCosmodogGame()
						.getInterfaceActionRegistry()
						.getRegisteredAction(AsyncActionType.MODAL_WINDOW, EndingNarrationAction.class);
		
		if (endingNarrationAction != null) {
			
			graphics.setColor(Color.black);
			graphics.fillRect(
					dc.x(),
					dc.y(),
					dc.w(),
					dc.h());

			Animation endingBackground = ApplicationContext.instance().getAnimations().get("endingBackground");
			
			endingBackground.draw(
					dc.x(), 
					dc.y(), 
					dc.w(), 
					dc.h()
			);
			
			EndingNarrationAction.ActionPhase phase = endingNarrationAction.phase;
			float phaseCompletion = endingNarrationAction.phaseCompletion();
			
			if (phase == EndingNarrationAction.ActionPhase.DARKNESS) {
				graphics.setColor(new Color(0f, 0f, 0f));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
			}
			
			if (phase == EndingNarrationAction.ActionPhase.PICTURE_FADES_IN) {
				
				float textPageOpacity = (float)((1 - phaseCompletion) * EndingNarrationAction.INITIAL_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
			}

            if (phase == EndingNarrationAction.ActionPhase.PICTURE_FADES_OUT) {
				
				float textPageOpacity = (float)(phaseCompletion * EndingNarrationAction.INITIAL_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
			}
			
			if (phase == EndingNarrationAction.ActionPhase.TEXT) {
				
				graphics.setColor(new Color(0f, 0f, 0f, EndingNarrationAction.TEXT_PICTURE_OPACITY));
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
