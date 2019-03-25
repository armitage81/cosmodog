package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.gamelog.GameLog;
import antonafanasjew.cosmodog.model.gamelog.GameLogState;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TextBookRendererUtils;
import antonafanasjew.cosmodog.view.transitions.EndingTransition;
import antonafanasjew.cosmodog.view.transitions.EndingTransition.ActionPhase;

public class EndingRenderer implements Renderer {

	public static boolean firstLoop = true;
	
	public static class GameLogRenderingParameter {
		public GameLog gameLog;
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext cutsceneTextDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneTextDrawingContext();
		DrawingContext cutsceneControlsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().cutsceneControlsDrawingContext();
		
		GameLogState openGameLog = ApplicationContextUtils.getCosmodogGame().getOpenGameLog();
		EndingTransition transition = ApplicationContextUtils.getCosmodogGame().getEndingTransition();
		
		int page = openGameLog.getCurrentPage();
		GameLog gameLog = openGameLog.getGameLog();

		if (openGameLog != null && transition != null) {
			
			graphics.setColor(Color.black);
			graphics.fillRect(
					gameContainerDrawingContext.x(),
					gameContainerDrawingContext.y(),
					gameContainerDrawingContext.w(),
					gameContainerDrawingContext.h());

			Animation endingBackground = ApplicationContext.instance().getAnimations().get("endingBackground");
			
			endingBackground.draw(
					gameContainerDrawingContext.x(), 
					gameContainerDrawingContext.y(), 
					gameContainerDrawingContext.w(), 
					gameContainerDrawingContext.h()
			);
			
			ActionPhase phase = transition.phase;
			float phaseCompletion = transition.phaseCompletion();
			
			if (phase == ActionPhase.DARKNESS) {
				graphics.setColor(new Color(0f, 0f, 0f));
				graphics.fillRect(
						gameContainerDrawingContext.x(), 
						gameContainerDrawingContext.y(), 
						gameContainerDrawingContext.w(), 
						gameContainerDrawingContext.h()
				);
			}
			
			if (phase == ActionPhase.PICTURE_FADES_IN) {
				
				float textPageOpacity = (float)((1 - phaseCompletion) * EndingTransition.INITIAL_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						gameContainerDrawingContext.x(), 
						gameContainerDrawingContext.y(), 
						gameContainerDrawingContext.w(), 
						gameContainerDrawingContext.h()
				);
				
			}
			
			if (phase == ActionPhase.PICTURE) {

			}
			
			if (phase == ActionPhase.PICTURE_FADES_OUT) {
				
				float textPageOpacity = (float)(phaseCompletion * EndingTransition.INITIAL_PICTURE_OPACITY);
				
				graphics.setColor(new Color(0f, 0f, 0f, textPageOpacity));
				graphics.fillRect(
						gameContainerDrawingContext.x(), 
						gameContainerDrawingContext.y(), 
						gameContainerDrawingContext.w(), 
						gameContainerDrawingContext.h()
				);
				
			}
			
			if (phase == ActionPhase.TEXT) {
				
				graphics.setColor(new Color(0f, 0f, 0f, EndingTransition.TEXT_PICTURE_OPACITY));
				graphics.fillRect(
						gameContainerDrawingContext.x(), 
						gameContainerDrawingContext.y(), 
						gameContainerDrawingContext.w(), 
						gameContainerDrawingContext.h()
				);
				
				TextBookRendererUtils.renderTextPage(gameContainer, graphics, cutsceneTextDrawingContext, gameLog.getLogText(), FontType.CutsceneNarration, page);
				
				boolean renderBlinkingHint = (System.currentTimeMillis() / 250 % 2) == 1;
							
				if (renderBlinkingHint) {
					TextBookRendererUtils.renderCenteredLabel(gameContainer, graphics, cutsceneControlsDrawingContext, "Press [ENTER]", FontType.PopUpInterface, 0);
				}
			}
		}
		
		firstLoop = false;
	}

}
