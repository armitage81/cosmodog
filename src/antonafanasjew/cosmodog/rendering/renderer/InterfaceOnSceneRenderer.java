package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.dying.DyingAction;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;
import antonafanasjew.cosmodog.rules.actions.async.AbstractNarrationAction;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;

public class InterfaceOnSceneRenderer implements Renderer {

	private Renderer sceneRenderer = new SceneRenderer();
	private Renderer lifeInterfaceRenderer = new LifeInterfaceRenderer();
	private Renderer arsenalInterfaceRenderer = new ArsenalInterfaceRenderer();
	private Renderer vitalDataInterfaceRenderer = new VitalDataInterfaceRenderer();
	private Renderer geigerCounterViewRenderer = new GeigerCounterViewRenderer();
	private Renderer supplyTrackerViewRenderer = new SupplyTrackerViewRenderer();
	private Renderer timeRenderer = new TimeRenderer();
	private Renderer infobitsRenderer = new InfobitsRenderer();
	private Renderer textFrameRenderer = new TextFrameRenderer();
	private Renderer onScreenNotificationRenderer = new OnScreenNotificationRenderer();
	private Renderer gameLogRenderer = new GameLogRenderer();
	private Renderer cutsceneRenderer = new CutsceneRenderer();
	private Renderer memoriesRenderer = new MemoriesRenderer();
	private Renderer endingRenderer = new EndingRenderer();
		
	@Override
	public void render(GameContainer gc, Graphics g, Object renderingParameter) {

		DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
		DrawingContext leftColumnDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().leftColumnDrawingContext();
		DrawingContext rightColumnDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().rightColumnDrawingContext();
		DrawingContext topBarDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().topBarDrawingContext();
		DrawingContext bottomBarDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().bottomBarDrawingContext();
		DrawingContext supplyTrackerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().supplyTrackerDrawingContext();
		DrawingContext timeDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().timeDrawingContext();
		DrawingContext infobitsDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().infobitsDrawingContext();
		DrawingContext lifeDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().lifeDrawingContext();
		
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		//Draws the scene
		sceneRenderer.render(gc, g, null);
		
		//Draw the bars around the scene. They are needed to compensate for high resolution without stretching the visible scene or make it bigger.
		g.setColor(Color.black);
		g.fillRect(leftColumnDrawingContext.x(), leftColumnDrawingContext.y(), leftColumnDrawingContext.w(), leftColumnDrawingContext.h());
		g.fillRect(rightColumnDrawingContext.x(), rightColumnDrawingContext.y(), rightColumnDrawingContext.w(), rightColumnDrawingContext.h());
		g.fillRect(topBarDrawingContext.x(), topBarDrawingContext.y(), topBarDrawingContext.w(), topBarDrawingContext.h());
		g.fillRect(bottomBarDrawingContext.x(), bottomBarDrawingContext.y(), bottomBarDrawingContext.w(), bottomBarDrawingContext.h());
		
		//Draw the interface frame
		if (Features.getInstance().featureOn(Features.FEATURE_INTERFACE)) {
			Image topPanelFrame = ApplicationContext.instance().getImages().get("ui.ingame.frame");
			ImageUtils.renderImage(gc, g, topPanelFrame, gameContainerDrawingContext);
		}
		
		//Draw the Geiger counter interface display
		geigerCounterViewRenderer.render(gc, g, null);
		
		//Draw the supply tracker interface display
		supplyTrackerViewRenderer.render(gc, g, null);
		
		//Draw time interface display
		timeRenderer.render(gc, g, null);
		
		//Draw number of collected infobits interface display
		infobitsRenderer.render(gc, g, null);

		//Draw collected/selected weapons interface display
		arsenalInterfaceRenderer.render(gc, g, null);
		
		//Draw life bar interface
		lifeInterfaceRenderer.render(gc, g, null);

		//Draw water and food bars interface
		vitalDataInterfaceRenderer.render(gc, g, null);
		
		//Draw an optional text frame if opened.
		if (cosmodogGame.getTextFrame() != null) {
			String text = cosmodogGame.getTextFrame().getText();
			TextBookRendererParameter param = TextBookRendererParameter.instance(text, FontType.PopUp, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER, 0);
			textFrameRenderer.render(gc, g, param);
		}
		
				
		if (cosmodogGame.getOpenGameLog() != null) {
			String category = cosmodogGame.getOpenGameLog().getGameLog().getCategory();
			String id = cosmodogGame.getOpenGameLog().getGameLog().getIdInCategory();
			if (category.equals("cutscenes")) {
				if (id.equals("decision")) {
					endingRenderer.render(gc, g, null);
				} else {
					cutsceneRenderer.render(gc, g, null);
				}
			} else if (category.equals("memories")|| category.equals("maryharper")) {
				memoriesRenderer.render(gc, g, null);
			} else {
				gameLogRenderer.render(gc, g, null);
			}
		}

		//Draws onscreen notifications
		onScreenNotificationRenderer.render(gc, g, null);

	}

}
