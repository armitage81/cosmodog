package antonafanasjew.cosmodog;

import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.util.DrawingContextUtils;

public class DrawingContextProvider1920x1080 extends AbstractDrawingContextProvider {
	
	private static final int RESOLUTION_WIDTH = 1920;
	private static final int RESOLUTION_HEIGHT = 1080;
	
	public static final float GAME_LOG_FRAME_WIDTH = 1200;
	public static final float GAME_LOG_FRAME_HEIGHT = 700;
	
	public static final float GAME_LOG_TEXT_WIDTH = 1040;
	public static final float GAME_LOG_TEXT_HEIGHT = 500;
	
	public static final float CUTSCENE_FRAME_WIDTH = 1600;
	public static final float CUTSCENE_FRAME_HEIGHT = 700;
	
	public static final float CUTSCENE_TEXT_WIDTH = 1500;
	public static final float CUTSCENE_TEXT_HEIGHT = 650;
	
	public static final float POPUP_FRAME_WIDTH = 500;
	public static final float POPUP_FRAME_HEIGHT = 400;
	
	public static final float POPUP_TEXT_WIDTH = 400;
	public static final float POPUP_TEXT_HEIGHT = 200;
	
	public DrawingContextProvider1920x1080() {
		
		this.gameContainerDrawingContext = new SimpleDrawingContext(
				null, 
				0, 
				0, 
				RESOLUTION_WIDTH, 
				RESOLUTION_HEIGHT
		);
		
		float mapW = RESOLUTION_WIDTH;
		float mapH = RESOLUTION_HEIGHT;
		float mapOffsetX = 0;
		float mapOffsetY = (gameContainerDrawingContext.h() - RESOLUTION_HEIGHT) / 2.0f;
		
		sceneDrawingContext = new SimpleDrawingContext(
				gameContainerDrawingContext, 
				mapOffsetX, 
				mapOffsetY, 
				RESOLUTION_WIDTH, 
				RESOLUTION_HEIGHT
		);

		dialogBoxDrawingContext = new TileDrawingContext(sceneDrawingContext, 1, 5, 0, 4);
		
		leftColumnDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 0, 0, mapOffsetX, gameContainerDrawingContext.h());
		
		rightColumnDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, mapOffsetX + mapW, 0, gameContainerDrawingContext.w() - mapOffsetX - mapW, gameContainerDrawingContext.h() );
		
		topBarDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 0, 0, gameContainerDrawingContext.w(), gameContainerDrawingContext.h() - mapOffsetY  - mapH);
		
		bottomBarDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 0, mapOffsetY + mapH, gameContainerDrawingContext.w(), gameContainerDrawingContext.h() - mapOffsetY - mapH);
		
		infobitsDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 582, 28, 56, 52);
		infobitsDrawingContext = DrawingContextUtils.difResFromRef(infobitsDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		timeDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1026, 28, 56, 52);
		timeDrawingContext = DrawingContextUtils.difResFromRef(timeDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		
		geigerCounterDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1111, 28, 56, 52);
		geigerCounterDrawingContext = DrawingContextUtils.difResFromRef(geigerCounterDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		
		supplyTrackerDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1196, 28, 56, 52);
		supplyTrackerDrawingContext = DrawingContextUtils.difResFromRef(supplyTrackerDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		lifeDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 28, 28, 524, 52);
		lifeDrawingContext = DrawingContextUtils.difResFromRef(lifeDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);

		humanLifeDrawingContext = new TileDrawingContext(lifeDrawingContext, 1, 3, 0, 0);
		humanLifeDrawingContext = new CenteredDrawingContext(humanLifeDrawingContext, 2);
		
		robustnessDrawingContext = new TileDrawingContext(lifeDrawingContext, 1, 3, 0, 1);
		robustnessDrawingContext = new CenteredDrawingContext(robustnessDrawingContext, 2);
		
		fuelDrawingContext = new TileDrawingContext(lifeDrawingContext, 1, 3, 0, 2);
		fuelDrawingContext = new CenteredDrawingContext(fuelDrawingContext, 2);
		
		vitalDataDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 28, 640, 524, 52);
		vitalDataDrawingContext = DrawingContextUtils.difResFromRef(vitalDataDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		
		inGameMenuHeaderDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 33, 33, 1213, 61);
		inGameMenuHeaderDrawingContext = DrawingContextUtils.difResFromRef(inGameMenuHeaderDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		
		inGameMenuContentDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 33, 144, 1213, 431);
		inGameMenuContentDrawingContext = DrawingContextUtils.difResFromRef(inGameMenuContentDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		
		inGameMenuFooterDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 33, 625, 1213, 61);
		inGameMenuFooterDrawingContext = DrawingContextUtils.difResFromRef(inGameMenuFooterDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		
		cutsceneDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		cutsceneTextDrawingContext = new TileDrawingContext(cutsceneDrawingContext, 1, 1, 0, 0);
		cutsceneControlsDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 9);
		
		gameIntroTextFrameDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		gameIntroTextDrawingContext = new TileDrawingContext(gameIntroTextFrameDrawingContext, 1, 1, 0, 0);
		gameIntroControlsDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 9, 1, 1);
		
		gameOutroTextFrameDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, 1000, 500);
		gameOutroTextDrawingContext = new TileDrawingContext(gameOutroTextFrameDrawingContext, 1, 1, 0, 0);
		gameOutroControlsDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 10, 0, 9, 1, 1);
		
		gameLogDrawingContext = new CenteredDrawingContext(gameContainerDrawingContext, GAME_LOG_FRAME_WIDTH, GAME_LOG_FRAME_HEIGHT);
		gameLogHeaderDrawingContext = new SimpleDrawingContext(gameLogDrawingContext, (GAME_LOG_FRAME_WIDTH - GAME_LOG_TEXT_WIDTH) / 2, 20, GAME_LOG_TEXT_WIDTH, 34);
		gameLogContentDrawingContext = new SimpleDrawingContext(gameLogDrawingContext, (GAME_LOG_FRAME_WIDTH - GAME_LOG_TEXT_WIDTH) / 2, 100, GAME_LOG_TEXT_WIDTH, GAME_LOG_TEXT_HEIGHT);
		gameLogControlsDrawingContext = new SimpleDrawingContext(gameLogDrawingContext, (GAME_LOG_FRAME_WIDTH - GAME_LOG_TEXT_WIDTH) / 2, 630, GAME_LOG_TEXT_WIDTH, 35);
		
		textFrameDrawingContext = new CenteredDrawingContext(sceneDrawingContext, POPUP_FRAME_WIDTH, POPUP_FRAME_HEIGHT);
		textFrameHeaderDrawingContext = new SimpleDrawingContext(textFrameDrawingContext, (POPUP_FRAME_WIDTH - POPUP_TEXT_WIDTH) / 2, 14, POPUP_TEXT_WIDTH, 35);
		textFrameContentDrawingContext = new SimpleDrawingContext(textFrameDrawingContext, (POPUP_FRAME_WIDTH - POPUP_TEXT_WIDTH) / 2, 100, POPUP_TEXT_WIDTH, 200);
		textFrameControlsDrawingContext = new SimpleDrawingContext(textFrameDrawingContext, (POPUP_FRAME_WIDTH - POPUP_TEXT_WIDTH) / 2, 346, POPUP_TEXT_WIDTH, 32);
		
		debugConsoleHeaderDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 0);
		debugConsoleContentDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 1);
		debugConsoleControlsDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 2);

		startScreenLogoDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 4, 0,0,1,3);
		startScreenLogoDrawingContext = new CenteredDrawingContext(startScreenLogoDrawingContext, 1280, 384);
		startScreenMenuDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 10, 10, 6, 7, 4, 2);
		startScreenReferencesDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 10, 10, 0, 9, 10, 1);
		
		logOverviewDrawingContext = new SimpleDrawingContext(null, 12 + 33, 13 + 144, 397, 406);
		logOverviewDrawingContext = DrawingContextUtils.difResFromRef(logOverviewDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		logOverviewDrawingContext = new CenteredDrawingContext(logOverviewDrawingContext, 10);
		
		logContentDrawingContext = new SimpleDrawingContext(null, 441 + 33, 13 + 144, 759, 406);
		logContentDrawingContext = DrawingContextUtils.difResFromRef(logContentDrawingContext, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		logContentDrawingContext = new CenteredDrawingContext(logContentDrawingContext, 10);
		
		secretFoundMessageDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 3, 0, 0);
		
		logPlayerHeaderDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 0);
		logPlayerTitleDrawingContext = new TileDrawingContext(logPlayerHeaderDrawingContext, 1, 2, 0, 0);
		logPlayerControlsHintDrawingContext = new TileDrawingContext(logPlayerHeaderDrawingContext, 1, 2, 0, 1);
		logPlayerTextDrawingContext = new TileDrawingContext(logContentDrawingContext, 1, 7, 0, 1, 1, 6);

		gameHintDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 6, 0, 1);
		
	}
	
}
