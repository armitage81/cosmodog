package antonafanasjew.cosmodog;

import antonafanasjew.cosmodog.globals.ResolutionHolder;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.DrawingContextUtils;

public class DrawingContextProvider1280x720 extends AbstractDrawingContextProvider {
	
	public static final float SCENE_WIDTH = 1280;
	public static final float SCENE_HEIGHT = 720;
	
	public static final float GAME_LOG_FRAME_WIDTH = 864;
	public static final float GAME_LOG_FRAME_HEIGHT = 572;
	public static final float GAME_LOG_TEXT_WIDTH = 800;
	public static final float GAME_LOG_TEXT_HEIGHT = 440;
	
	public DrawingContextProvider1280x720() {
		
		this.gameContainerDrawingContext = new SimpleDrawingContext(
				null, 
				0, 
				0, 
				1280, 
				720
		);
		
		float mapW = SCENE_WIDTH;
		float mapH = SCENE_HEIGHT;
		float mapOffsetX = 0;
		float mapOffsetY = (gameContainerDrawingContext.h() - SCENE_HEIGHT) / 2.0f;
		
		sceneDrawingContext = new SimpleDrawingContext(
				gameContainerDrawingContext, 
				mapOffsetX, 
				mapOffsetY, 
				SCENE_WIDTH, 
				SCENE_HEIGHT
		);
		
		dialogBoxDrawingContext = new TileDrawingContext(sceneDrawingContext, 1, 5, 0, 4);
		
		leftColumnDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 0, 0, mapOffsetX, gameContainerDrawingContext.h());
		
		rightColumnDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, mapOffsetX + mapW, 0, gameContainerDrawingContext.w() - mapOffsetX - mapW, gameContainerDrawingContext.h() );
		
		topBarDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 0, 0, gameContainerDrawingContext.w(), gameContainerDrawingContext.h() - mapOffsetY  - mapH);
		
		bottomBarDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 0, mapOffsetY + mapH, gameContainerDrawingContext.w(), gameContainerDrawingContext.h() - mapOffsetY - mapH);
		
		infobitsDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 582, 28, 56, 52);
		infobitsDrawingContext = DrawingContextUtils.difResFromRef(infobitsDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());

		//Offset to the right, because text is not centered properly for some reason. 
		infobitsDrawingContext = new SimpleDrawingContext(infobitsDrawingContext, 7, 0, infobitsDrawingContext.w() - 7, infobitsDrawingContext.h());
		
		timeDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1026, 28, 56, 52);
		timeDrawingContext = DrawingContextUtils.difResFromRef(timeDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		geigerCounterDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1111, 28, 56, 52);
		geigerCounterDrawingContext = DrawingContextUtils.difResFromRef(geigerCounterDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		supplyTrackerDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1196, 28, 56, 52);
		supplyTrackerDrawingContext = DrawingContextUtils.difResFromRef(supplyTrackerDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		lifeDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 28, 28, 524, 52);
		lifeDrawingContext = DrawingContextUtils.difResFromRef(lifeDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		humanLifeDrawingContext = new TileDrawingContext(lifeDrawingContext, 1, 3, 0, 0);
		humanLifeDrawingContext = new CenteredDrawingContext(humanLifeDrawingContext, 2);
		
		robustnessDrawingContext = new TileDrawingContext(lifeDrawingContext, 1, 3, 0, 1);
		robustnessDrawingContext = new CenteredDrawingContext(robustnessDrawingContext, 2);
		
		fuelDrawingContext = new TileDrawingContext(lifeDrawingContext, 1, 3, 0, 2);
		fuelDrawingContext = new CenteredDrawingContext(fuelDrawingContext, 2);
		
		vitalDataDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 28, 640, 524, 52);
		vitalDataDrawingContext = DrawingContextUtils.difResFromRef(vitalDataDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		inGameMenuHeaderDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 33, 33, 1213, 61);
		inGameMenuHeaderDrawingContext = DrawingContextUtils.difResFromRef(inGameMenuHeaderDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		DrawingContext inGameMenuContentDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 33, 144, 1213, 431);
		inGameMenuContentDrawingContext = DrawingContextUtils.difResFromRef(inGameMenuContentDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		DrawingContext inGameMenuFooterDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 33, 625, 1213, 61);
		inGameMenuFooterDrawingContext = DrawingContextUtils.difResFromRef(inGameMenuFooterDrawingContext, ResolutionHolder.get().getWidth(), ResolutionHolder.get().getHeight());
		
		cutsceneDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		cutsceneTextDrawingContext = new TileDrawingContext(cutsceneDrawingContext, 1, 7, 0, 0, 1, 6);
		cutsceneControlsDrawingContext = new TileDrawingContext(cutsceneDrawingContext, 1, 7, 0, 6, 1, 1);

		gameIntroTextFrameDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		gameIntroTextDrawingContext = new TileDrawingContext(gameIntroTextFrameDrawingContext, 1, 7, 0, 0, 1, 6);
		gameIntroControlsDrawingContext = new TileDrawingContext(gameIntroTextFrameDrawingContext, 1, 7, 0, 6, 1, 1);
		
		gameLogDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, GAME_LOG_FRAME_WIDTH, GAME_LOG_FRAME_HEIGHT);
		
		gameLogHeaderDrawingContext = new SimpleDrawingContext(gameLogDrawingContext, 25, 14, GAME_LOG_TEXT_WIDTH, 34);
		gameLogContentDrawingContext = new SimpleDrawingContext(gameLogDrawingContext, 25, 60, GAME_LOG_TEXT_WIDTH, GAME_LOG_TEXT_HEIGHT);
		gameLogControlsDrawingContext = new SimpleDrawingContext(gameLogDrawingContext, 25, 525, GAME_LOG_TEXT_WIDTH, 35);
		
		textFrameDrawingContext = new CenteredDrawingContext(sceneDrawingContext, 500, 400);
		textFrameHeaderDrawingContext = new SimpleDrawingContext(textFrameDrawingContext, 50, 14, 400, 44);
		textFrameContentDrawingContext = new SimpleDrawingContext(textFrameDrawingContext, 50, 100, 400, 200);
		textFrameControlsDrawingContext = new SimpleDrawingContext(textFrameDrawingContext, 50, 340, 400, 44);
		textFrameControlsDrawingContext = new CenteredDrawingContext(textFrameControlsDrawingContext, 30);
		
		debugConsoleHeaderDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 0);
		debugConsoleContentDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		debugConsoleControlsDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);
		
		startScreenLogoDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		startScreenLogoDrawingContext = new CenteredDrawingContext(startScreenLogoDrawingContext, 640, 192);
		startScreenMenuDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 10, 10, 6, 7, 4, 2);
		startScreenReferencesDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 10, 10, 0, 9, 10, 1);

		secretFoundMessageDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 0);
		
	}
	
}
