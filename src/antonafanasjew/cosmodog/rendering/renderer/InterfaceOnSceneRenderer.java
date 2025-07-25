package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.narration.DialogWithAlisaNarrationAction;
import antonafanasjew.cosmodog.actions.narration.EndingNarrationAction;
import antonafanasjew.cosmodog.actions.narration.MonolithNarrationAction;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.globals.FontProvider.FontTypeName;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.textbook.FontRefToFontTypeMap;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextPageConstraints;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;
import antonafanasjew.cosmodog.rendering.renderer.textbook.placement.Book;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.ImageUtils;

public class InterfaceOnSceneRenderer extends AbstractRenderer {

	private final Renderer sceneRenderer = new SceneRenderer();
	private final Renderer lifeInterfaceRenderer = new LifeInterfaceRenderer();
	private final Renderer arsenalInterfaceRenderer = new ArsenalInterfaceRenderer();
	private final Renderer vitalDataInterfaceRenderer = ConditionalRenderer.instanceWithResourceConsumptionActiveConditionAndAlternative(
			new VitalDataInterfaceRenderer(),
			new VitalDataInterfaceInSpaceRenderer()) ;
	private final Renderer geigerCounterViewRenderer = new GeigerCounterViewRenderer();
	private final Renderer supplyTrackerViewRenderer = new SupplyTrackerViewRenderer();
	private final Renderer timeRenderer = ConditionalRenderer.instanceWithDayNightActiveConditionAndAlternative(
			new TimeRenderer(),
			new TimeInSpaceRenderer());

	private final Renderer infobitsRenderer = new InfobitsRenderer();
	private final Renderer textFrameRenderer = new TextFrameRenderer();
	private final Renderer onScreenNotificationRenderer = new OnScreenNotificationRenderer();
	private final Renderer gameHintRenderer = new GameHintRenderer();
	private final Renderer gameLogRenderer = new GameLogRenderer();
	private final Renderer cutsceneRenderer = new CutsceneRenderer();
	private final Renderer memoriesRenderer = new MemoriesRenderer();
	private final Renderer endingRenderer = new EndingRenderer();
	private final Renderer positionDebugInfoRenderer = new PositionDebugInfoRenderer();
		
	@Override
	public void renderInternally(GameContainer gc, Graphics g, Object renderingParameter) {

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

		//We draw everything only if there are no fullscreen cutscenes playing
		if (cosmodogGame.getOpenBook() == null) {

			DrawingContext gameContainerDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().gameContainerDrawingContext();
			DrawingContext leftColumnDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().leftColumnDrawingContext();
			DrawingContext rightColumnDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().rightColumnDrawingContext();
			DrawingContext topBarDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().topBarDrawingContext();
			DrawingContext bottomBarDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().bottomBarDrawingContext();


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
				FontRefToFontTypeMap fontRefToFontTypeMap = FontRefToFontTypeMap.forOneFontTypeName(FontTypeName.Informational);

				//Take care. This dc must match the one in the text frame renderer.
				DrawingContext textFrameContentDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().textFrameContentDrawingContext();
				Book textBook = TextPageConstraints.fromDc(textFrameContentDrawingContext).textToBook(text, fontRefToFontTypeMap);

				TextBookRendererParameter param = TextBookRendererParameter.instance(textBook, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER);
				textFrameRenderer.render(gc, g, param);
			}

			//Draws onscreen notifications
			onScreenNotificationRenderer.render(gc, g, null);
			gameHintRenderer.render(gc, g, null);
			positionDebugInfoRenderer.render(gc, g, null);

		} else {

			MonolithNarrationAction monolithNarrationAction =
					ApplicationContextUtils
							.getCosmodogGame()
							.getInterfaceActionRegistry()
							.getRegisteredAction(AsyncActionType.MODAL_WINDOW, MonolithNarrationAction.class);

			DialogWithAlisaNarrationAction dialogWithAlisaNarrationAction =
					ApplicationContextUtils
							.getCosmodogGame()
							.getInterfaceActionRegistry()
							.getRegisteredAction(AsyncActionType.MODAL_WINDOW, DialogWithAlisaNarrationAction.class);

			EndingNarrationAction endingNarrationAction =
					ApplicationContextUtils
							.getCosmodogGame()
							.getInterfaceActionRegistry()
							.getRegisteredAction(AsyncActionType.MODAL_WINDOW, EndingNarrationAction.class);


			if (endingNarrationAction != null) {
				endingRenderer.render(gc, g, null);
			} else if (dialogWithAlisaNarrationAction != null) {
				cutsceneRenderer.render(gc, g, null);
			} else if (monolithNarrationAction != null) {
				memoriesRenderer.render(gc, g, null);
			} else {
				gameLogRenderer.render(gc, g, null);
			}
		}

	}

}
