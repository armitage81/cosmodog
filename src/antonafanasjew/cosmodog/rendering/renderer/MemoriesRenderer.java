package antonafanasjew.cosmodog.rendering.renderer;

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
import antonafanasjew.cosmodog.view.transitions.MonolithTransition;
import antonafanasjew.cosmodog.view.transitions.MonolithTransition.ActionPhase;

public class MemoriesRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		long referenceTime = System.currentTimeMillis();
		
		DrawingContext dc = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext controlsDc = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneControlsDrawingContext();
		
		Book openBook = ApplicationContextUtils.getCosmodogGame().getOpenBook();
		
		if (openBook == null) {
			return;
		}
		
		MonolithTransition transition = ApplicationContextUtils.getCosmodogGame().getMonolithTransition();
		
		if (transition != null) {
			
			
			graphics.setColor(Color.black);
			graphics.fillRect(
					dc.x(),
					dc.y(),
					dc.w(),
					dc.h());
			
			String animationId = "cutsceneMonolith";
			Animation cutsceneBackground = ApplicationContext.instance().getAnimations().get(animationId);
			
			cutsceneBackground.draw(
					dc.x(), 
					dc.y(), 
					dc.w(), 
					dc.h()
			);
			
			ActionPhase phase = transition.phase;
			float phaseCompletion = transition.phaseCompletion();
			
			if (phase == ActionPhase.MONOLITH_FADES_IN) {
				
				float textPageOpacity = (float)((1 - phaseCompletion) * MonolithTransition.MAX_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
			}
			
			if (phase == ActionPhase.MONOLITH) {
				
			}
			
			if (phase == ActionPhase.PICTURE_FADES) {

				float textPageOpacity = (float)(phaseCompletion * MonolithTransition.MAX_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						dc.x(), 
						dc.y(), 
						dc.w(), 
						dc.h()
				);
				
			}
			
			if (phase == ActionPhase.TEXT) {
				graphics.setColor(new Color(0f, 0f, 0f, MonolithTransition.MAX_PICTURE_OPACITY));
				
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
