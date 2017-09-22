package antonafanasjew.cosmodog.model.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.FontType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.rendering.renderer.ArsenalInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.ArtilleryGrenadeRenderer;
import antonafanasjew.cosmodog.rendering.renderer.BirdsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.CloudRenderer;
import antonafanasjew.cosmodog.rendering.renderer.CutsceneRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DayTimeFilterRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DialogBoxRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DyingPlayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DynamicPiecesRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DynamicPiecesRenderer.DynamicPiecesRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer.EffectsRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.GameLogRenderer;
import antonafanasjew.cosmodog.rendering.renderer.GeigerCounterViewRenderer;
import antonafanasjew.cosmodog.rendering.renderer.InGameMenuRenderer;
import antonafanasjew.cosmodog.rendering.renderer.InfobitsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.LifeInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MapLayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MarkedTileRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MineExplosionRenderer;
import antonafanasjew.cosmodog.rendering.renderer.NpcRenderer;
import antonafanasjew.cosmodog.rendering.renderer.OnScreenNotificationRenderer;
import antonafanasjew.cosmodog.rendering.renderer.OverheadNotificationRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PiecesRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PlayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.SightRadiusRenderer;
import antonafanasjew.cosmodog.rendering.renderer.SupplyTrackerViewRenderer;
import antonafanasjew.cosmodog.rendering.renderer.TextFrameRenderer;
import antonafanasjew.cosmodog.rendering.renderer.TimeRenderer;
import antonafanasjew.cosmodog.rendering.renderer.VitalDataInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.WormAttackRenderer;
import antonafanasjew.cosmodog.rendering.renderer.WormSnowSparkRenderer;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.BottomLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TipsLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TopLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.NotOnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.OnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.pieces.OccupiedPlatformRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.TextBookRenderer.TextBookRendererParameter;
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.GameEventUtils;
import antonafanasjew.cosmodog.util.ImageUtils;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public class GameState extends BasicGameState {

	private boolean firstUpdate;
	
	private ApplicationContext applicationContext = ApplicationContext.instance();
	
	
	private DrawingContext gameContainerDrawingContext;
	private DrawingContext middleColumnDrawingContext;
	private DrawingContext geigerCounterDrawingContext;
	private DrawingContext supplyTrackerDrawingContext;
	private DrawingContext timeDrawingContext;
	private DrawingContext infobitsDrawingContext;
	
	private DrawingContext lifeDrawingContext;
	private DrawingContext vitalDataDrawingContext;
	private DrawingContext mapDrawingContext;
	
	private AbstractRenderer dynamicPiecesRenderer;
	private AbstractRenderer cloudRenderer;
	private AbstractRenderer birdsRenderer;
	private Renderer vitalDataInterfaceRenderer;
	private Renderer lifeInterfaceRenderer;
	private AbstractRenderer mapRenderer;
	private AbstractRenderer platformRenderer;
	private AbstractRenderer playerRenderer;
	private AbstractRenderer wormAttackRenderer;
	private AbstractRenderer mineExplosionRenderer;
	private AbstractRenderer artilleryGrenadeRenderer;
	private AbstractRenderer wormSnowSparkRenderer;
	private AbstractRenderer npcRenderer;
	private AbstractRenderer northPiecesRenderer;
	private AbstractRenderer southPiecesRenderer;
	private AbstractRenderer platformPiecesRenderer;
	private AbstractRenderer effectsRenderer;
	private AbstractRenderer overheadNotificationRenderer;
	private Renderer daytimeColorFilterRenderer;
	private Renderer markedTileRenderer;
	private AbstractRenderer sightRadiusRenderer;
	private Renderer arsenalInterfaceRenderer;
	private Renderer dyingPlayerRenderer;
	private AbstractRenderer onScreenNotificationRenderer;
	private Renderer timeRenderer;
	private Renderer infobitsRenderer;
	
	private MapLayerRendererPredicate bottomLayersPredicate;
	private MapLayerRendererPredicate tipsLayersPredicate;
	private MapLayerRendererPredicate topsLayersPredicate;
	
	private DialogBoxRenderer dialogBoxRenderer;
	private TextFrameRenderer textFrameRenderer;
	private GameLogRenderer gameLogRenderer;
	private CutsceneRenderer cutsceneRenderer;
	private InGameMenuRenderer inGameMenuRenderer;
	private GeigerCounterViewRenderer geigerCounterViewRenderer;
	private SupplyTrackerViewRenderer supplyTrackerViewRenderer;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		gc.setVSync(true);
		gc.setShowFPS(false);

		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());

		// leftColumnDrawingContext = new
		// TileDrawingContext(gameContainerDrawingContext, 5, 1, 0, 0);
		middleColumnDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 8, 0, 0, 1, 8);
		
		mapDrawingContext = new TileDrawingContext(middleColumnDrawingContext, 5, 1, 0, 0, 5, 1);
		

		infobitsDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 582, 28, 56, 52);
		timeDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1026, 28, 56, 52);
		geigerCounterDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1111, 28, 56, 52);
		supplyTrackerDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 1196, 28, 56, 52);
		
		
		lifeDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 28, 28, 524, 52);
		
		
		vitalDataDrawingContext = new SimpleDrawingContext(gameContainerDrawingContext, 28, 640, 524, 52);
		
				
		//Update the global dialog drawing context variable in the application context.
		ApplicationContext.instance().setDialogBoxDrawingContext(new TileDrawingContext(mapDrawingContext, 1, 5, 0, 4));
		
		
		dynamicPiecesRenderer = new DynamicPiecesRenderer();
		
		cloudRenderer = new CloudRenderer(applicationContext.getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS));
		
		birdsRenderer = new BirdsRenderer();
		
		lifeInterfaceRenderer = new LifeInterfaceRenderer();
		
		mapRenderer = new MapLayerRenderer();
		platformRenderer = new OccupiedPlatformRenderer();
		playerRenderer = new PlayerRenderer();
		wormAttackRenderer = new WormAttackRenderer();
		mineExplosionRenderer = new MineExplosionRenderer();
		artilleryGrenadeRenderer = new ArtilleryGrenadeRenderer();
		wormSnowSparkRenderer = new WormSnowSparkRenderer();
		npcRenderer = new NpcRenderer();
		northPiecesRenderer = new PiecesRenderer(true, false);
		southPiecesRenderer = new PiecesRenderer(false, true);
		platformPiecesRenderer = new PiecesRenderer(true, true);
		effectsRenderer = new EffectsRenderer();
		overheadNotificationRenderer = new OverheadNotificationRenderer();
		daytimeColorFilterRenderer = new DayTimeFilterRenderer();
		markedTileRenderer = new MarkedTileRenderer();
		sightRadiusRenderer = new SightRadiusRenderer();
		arsenalInterfaceRenderer = new ArsenalInterfaceRenderer();
		dyingPlayerRenderer = new DyingPlayerRenderer();
		vitalDataInterfaceRenderer = new VitalDataInterfaceRenderer();
		
		dialogBoxRenderer = new DialogBoxRenderer();
		textFrameRenderer = new TextFrameRenderer();
		gameLogRenderer = new GameLogRenderer();
		cutsceneRenderer = new CutsceneRenderer();
		inGameMenuRenderer = new InGameMenuRenderer();
		geigerCounterViewRenderer = new GeigerCounterViewRenderer();
		supplyTrackerViewRenderer = new SupplyTrackerViewRenderer();
		timeRenderer = new TimeRenderer();
		infobitsRenderer = new InfobitsRenderer();
		
		bottomLayersPredicate = new BottomLayersRenderingPredicate();
		tipsLayersPredicate = new TipsLayersRenderingPredicate();
		topsLayersPredicate = new TopLayersRenderingPredicate();
		
		onScreenNotificationRenderer = new OnScreenNotificationRenderer();
		
	}

	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		if (cosmodog.getGameLifeCycle().isStartNewGame()) {

			CustomTiledMap customTiledMap = applicationContext.getCustomTiledMap();
			
    		CosmodogGame cosmodogGame;
    		
    		try {
				cosmodogGame = InitializationUtils.initializeCosmodogGame(game, customTiledMap, "Armitage");
				cosmodogGame.setGameName(cosmodog.getGameLifeCycle().getGameName());
				CosmodogMap map = cosmodogGame.getMap();		
				Rectangle scene = Rectangle.fromSize((float) (map.getWidth() * map.getTileWidth()), (float) (map.getHeight() * map.getTileHeight()));

				try {
					cosmodogGame.setCam(new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, scene, mapDrawingContext.x(), mapDrawingContext.y(), mapDrawingContext.w(), mapDrawingContext.h()));
				} catch (CamPositioningException e) {
					Log.error("Camera positioning could not be established", e);
				}
				
				Player player = cosmodogGame.getPlayer();
				Cam cam = cosmodogGame.getCam();
				cam.zoom(Constants.DEFAULT_CAM_ZOOM_FACTOR);
				cam.focusOnPiece(map, 0, 0, player);
				
			} catch (TiledMapIoException e1) {
				throw new SlickException("Error while reading the cosmodog map", e1);
			}
    		
    		applicationContext.getCosmodog().setCosmodogGame(cosmodogGame);

    		//Check for the rules of the new game event
    		GameEventUtils.throwEvent(new GameEventNewGame());
    		
		}
		
		MusicUtils.loopMusic(MusicResources.MUSIC_IN_GAME1);
		
		firstUpdate = true;
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int n) throws SlickException {
		
		if (firstUpdate) {
			n = 0; //We ignore the first update as it can be big because of the initialization.
			gc.getInput().clearKeyPressedRecord();
			firstUpdate = false;
		}
		
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		
		Input input = gc.getInput();

		//Blocking interface actions, like text frames or dialog boxes, have their own modal input handling, so handle the input only in case they are not registered.
		//Also the asyncronous actions from the action registry should be updated only if no blocking interface action is registered.
		if (cosmodogGame.getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.BLOCKING_INTERFACE) == null) {

			if (input.isKeyPressed(Input.KEY_0)) {
    			sbg.enterState(CosmodogStarter.DEBUG_STATE_ID);
    		}
    
    		if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
    			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_CONTROL).handleInput(gc, sbg, n, applicationContext);
    		} else {
    			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME).handleInput(gc, sbg, n, applicationContext);
    		}

    		cosmodogGame.getChronometer().update(n);
    		cosmodogGame.getCommentsStateUpdater().update(n);
    		
		}
		
		cosmodogGame.getActionRegistry().update(n, gc, sbg);
		cosmodogGame.getInterfaceActionRegistry().update(n, gc, sbg);
		
		//After processing a loop, clear the record of pressed buttons.
		input.clearKeyPressedRecord();
		
	}

	public static int maxTime = 0;

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		//Draw "ground" part of the map
		mapRenderer.render(gc, g, mapDrawingContext, bottomLayersPredicate);
		
		
		
		//Draw "tips" part of the map (Tile that would partially cover the player, like high flowers. Take note, it is still drawn underneath the player, as partial coverage is realized via special sprites)
		mapRenderer.render(gc, g, mapDrawingContext, tipsLayersPredicate);
		
		//Draw ground dynamic pieces
		dynamicPiecesRenderer.render(gc, g, mapDrawingContext, DynamicPiecesRendererParam.BOTTOM);
		
		//Draw ground effects.
		effectsRenderer.render(gc, g, mapDrawingContext, EffectsRendererParam.FOR_GROUND_EFFECTS);

		//Draw mine explosion
		mineExplosionRenderer.render(gc, g, mapDrawingContext);
		
		//Draw the platform here. Note: This renderer handles the occupied platform. The standalone platform is a normal piece and will be rendered with the piece renderers.
		platformRenderer.render(gc, g, mapDrawingContext);
		
		//Draw pieces that are north from the player and will be covered by his shape. (Ignore pieces that are on the platform)
		northPiecesRenderer.render(gc, g, mapDrawingContext, new NotOnPlatformPieceRendererPredicate());
		
		//Draw the player
		playerRenderer.render(gc, g, mapDrawingContext);
		
		//Draw NPC
		npcRenderer.render(gc, g, mapDrawingContext);
		
		//Draw pieces that are south from the player and will not be covered by his shape.  (Ignore pieces that are on the platform)
		southPiecesRenderer.render(gc, g, mapDrawingContext, new NotOnPlatformPieceRendererPredicate());
		
		//Draw pieces that are on the platform.
		platformPiecesRenderer.render(gc, g, mapDrawingContext, new OnPlatformPieceRendererPredicate());
		
		//Draw top dynamic pieces.
		dynamicPiecesRenderer.render(gc, g, mapDrawingContext, DynamicPiecesRendererParam.TOP);
		
		//Draw top parts of the map, f.i. roofs, tops of the pillars and trees. They will cover both player and NPC
		mapRenderer.render(gc, g, mapDrawingContext, topsLayersPredicate);
		
		//Draw attacking worm
		wormAttackRenderer.render(gc, g, mapDrawingContext);
		
		//Draw Snow sparks covering the worm.
		wormSnowSparkRenderer.render(gc, g, mapDrawingContext);
		
		//Draw the sight radius of the enemies.
		sightRadiusRenderer.render(gc, g, mapDrawingContext);
		
		//Draw artillery grenades if it is shooting.
		artilleryGrenadeRenderer.render(gc, g, mapDrawingContext);
		
		//Draw top effects.
		effectsRenderer.render(gc, g, mapDrawingContext, EffectsRendererParam.FOR_TOP_EFFECTS);
		
		//Draw clouds.
		cloudRenderer.render(gc, g, mapDrawingContext);
		
		//Draw birds.
		birdsRenderer.render(gc, g, mapDrawingContext);

		//Draw Daytime mask.
		daytimeColorFilterRenderer.render(gc, g, mapDrawingContext, null);
		
		//Draw marked tiles, e.g. "fuel" sign
		markedTileRenderer.render(gc, g, mapDrawingContext, null);
		
		//Draw overhead notifications, e.g. "blocked" warning.
		overheadNotificationRenderer.render(gc, g, mapDrawingContext);
		
		Image topPanelFrame = ApplicationContext.instance().getImages().get("ui.ingame.frame");
		ImageUtils.renderImage(gc, g, topPanelFrame, gameContainerDrawingContext);
		
		geigerCounterViewRenderer.render(gc, g, geigerCounterDrawingContext, null);
		supplyTrackerViewRenderer.render(gc, g, supplyTrackerDrawingContext, null);
		timeRenderer.render(gc, g, timeDrawingContext, null);
		infobitsRenderer.render(gc, g, infobitsDrawingContext, null);
		
		arsenalInterfaceRenderer.render(gc, g, gameContainerDrawingContext, null);
		
		lifeInterfaceRenderer.render(gc, g, lifeDrawingContext, null);
		
		
		vitalDataInterfaceRenderer.render(gc, g, vitalDataDrawingContext, null);

		
		
		dyingPlayerRenderer.render(gc, g, mapDrawingContext, null);
	
		DrawingContext dialogBoxDrawingContext = ApplicationContext.instance().getDialogBoxDrawingContext();
				
		if (cosmodogGame.getWritingTextBoxState() != null) {
			g.setColor(new Color(0,0,0,0.75f));
			g.fillRect(0, 0, gameContainerDrawingContext.w(), gameContainerDrawingContext.h());
			dialogBoxRenderer.render(gc, g, dialogBoxDrawingContext, cosmodogGame.getWritingTextBoxState());
		}

		if (cosmodogGame.getTextFrame() != null) {
			String text = cosmodogGame.getTextFrame().getText();
			TextBookRendererParameter param = TextBookRendererParameter.instance(text, FontType.PopUp, TextBookRendererParameter.ALIGN_CENTER, TextBookRendererParameter.ALIGN_CENTER, 0);
			textFrameRenderer.render(gc, g, new CenteredDrawingContext(mapDrawingContext, 500, 400), param);
		}
		
		if (cosmodogGame.getOpenGameLog() != null) {
			String category = cosmodogGame.getOpenGameLog().getGameLog().getCategory();
			if (category.equals("memories") || category.equals("cutscenes")) {
				cutsceneRenderer.render(gc, g, new CenteredDrawingContext(gameContainerDrawingContext, Constants.GAME_LOG_FRAME_WIDTH, Constants.GAME_LOG_FRAME_HEIGHT), null);
			} else {
				gameLogRenderer.render(gc, g, new CenteredDrawingContext(gameContainerDrawingContext, Constants.GAME_LOG_FRAME_WIDTH, Constants.GAME_LOG_FRAME_HEIGHT), null);
			}
		}
		
		//Draws onscreen notifications
		onScreenNotificationRenderer.render(gc, g, mapDrawingContext);
		
		inGameMenuRenderer.render(gc, g, mapDrawingContext, null);
		

		
	}


	@Override
	public int getID() {
		return CosmodogStarter.GAME_STATE_ID;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		//Stop ambient sounds
		
		ApplicationContextUtils.getCosmodogGame().getAmbientSoundRegistry().clear();
	}
}
