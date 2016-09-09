package antonafanasjew.cosmodog.model.states;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CosmodogStarter;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.InputHandlerType;
import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.CenteredDrawingContext;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.context.SimpleDrawingContext;
import antonafanasjew.cosmodog.rendering.context.TileDrawingContext;
import antonafanasjew.cosmodog.rendering.decoration.BirdsDecoration;
import antonafanasjew.cosmodog.rendering.decoration.CloudsDecoration;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.rendering.renderer.ArsenalInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.BirdsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.CloudRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DayTimeFilterRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DialogBoxRenderer;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer.EffectsRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.GameProgressInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.LifeInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MapLayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MarkedTileRenderer;
import antonafanasjew.cosmodog.rendering.renderer.NpcRenderer;
import antonafanasjew.cosmodog.rendering.renderer.OverheadNotificationRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PiecesRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PlayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.RightInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.SightRadiusRenderer;
import antonafanasjew.cosmodog.rendering.renderer.TextFrameRenderer;
import antonafanasjew.cosmodog.rendering.renderer.VitalDataInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.WeaponTooltipRenderer;
import antonafanasjew.cosmodog.rendering.renderer.WritingRenderer;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.BottomLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TipsLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TopLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.NotOnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.OnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.pieces.OccupiedPlatformRenderer;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

import com.google.common.collect.Lists;

public class GameState extends BasicGameState {

	private boolean firstUpdate;
	
	private ApplicationContext applicationContext = ApplicationContext.instance();
	
	
	private DrawingContext gameContainerDrawingContext;
	private DrawingContext middleColumnDrawingContext;
	private DrawingContext topDrawingContext;
	
	private DrawingContext weaponTooltipsDrawingContext;
	private DrawingContext lifeDrawingContext;
	private DrawingContext arsenalDrawingContext;
	private DrawingContext bottomDrawingContext;
	private DrawingContext vitalDataDrawingContext;
	private DrawingContext gameProgressDrawingContext;
	private DrawingContext mapDrawingContext;
	private DrawingContext rightColumnDrawingContext;
	
	
	private CloudsDecoration cloudsDeco;
	private List<BirdsDecoration> birdsDecos = Lists.newArrayList();
	
	private AbstractRenderer cloudRenderer;
	private AbstractRenderer birdsRenderer;
	private Renderer vitalDataInterfaceRenderer;
	private Renderer gameProgressInterfaceRenderer;
	private Renderer lifeInterfaceRenderer;
	private AbstractRenderer mapRenderer;
	private AbstractRenderer platformRenderer;
	private AbstractRenderer playerRenderer;
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
	private Renderer weaponTooltipRenderer;
	private Renderer rightInterfaceRenderer;
	
	private MapLayerRendererPredicate bottomLayersPredicate;
	private MapLayerRendererPredicate tipsLayersPredicate;
	private MapLayerRendererPredicate topsLayersPredicate;
	
	private DialogBoxRenderer dialogBoxRenderer;
	private WritingRenderer commentsRenderer;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		gc.setVSync(true);
		gc.setShowFPS(false);
		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());

		// leftColumnDrawingContext = new
		// TileDrawingContext(gameContainerDrawingContext, 5, 1, 0, 0);
		middleColumnDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 8, 0, 0, 1, 8);
		
		mapDrawingContext = new TileDrawingContext(middleColumnDrawingContext, 5, 1, 0, 0, 4, 1);
		topDrawingContext = new TileDrawingContext(mapDrawingContext, 1, 9, 0, 0);
		
		lifeDrawingContext = new TileDrawingContext(topDrawingContext, 2, 1, 0, 0);
		arsenalDrawingContext = new TileDrawingContext(topDrawingContext, 2, 1, 1, 0);
		
		weaponTooltipsDrawingContext = new TileDrawingContext(mapDrawingContext, 2, 9, 1, 1, 1, 8);
		
		bottomDrawingContext = new TileDrawingContext(mapDrawingContext, 1, 12, 0, 11);
		vitalDataDrawingContext = new TileDrawingContext(bottomDrawingContext, 2, 1, 0, 0);
		gameProgressDrawingContext = new TileDrawingContext(bottomDrawingContext, 2, 1, 1, 0);
		
		
		rightColumnDrawingContext = new TileDrawingContext(middleColumnDrawingContext, 5, 1, 4, 0);
		
		//Update the global dialog drawing context variable in the application context.
		ApplicationContext.instance().setDialogBoxDrawingContext(new TileDrawingContext(mapDrawingContext, 1, 5, 0, 4));
		
		cloudRenderer = new CloudRenderer(applicationContext.getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS));
		
		birdsRenderer = new BirdsRenderer();
		
		lifeInterfaceRenderer = new LifeInterfaceRenderer();
		
		mapRenderer = new MapLayerRenderer();
		platformRenderer = new OccupiedPlatformRenderer();
		playerRenderer = new PlayerRenderer();
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
		weaponTooltipRenderer = new WeaponTooltipRenderer();
		vitalDataInterfaceRenderer = new VitalDataInterfaceRenderer();
		gameProgressInterfaceRenderer = new GameProgressInterfaceRenderer();
		
		rightInterfaceRenderer = new RightInterfaceRenderer();
		
		commentsRenderer = new WritingRenderer(false, new Color(0, 0, 1, 0.5f));
		dialogBoxRenderer = new DialogBoxRenderer();
		
		bottomLayersPredicate = new BottomLayersRenderingPredicate();
		tipsLayersPredicate = new TipsLayersRenderingPredicate();
		topsLayersPredicate = new TopLayersRenderingPredicate();
		
	}

	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		
		if (cosmodog.getGameLifeCycle().isStartNewGame()) {

			CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
			CustomTiledMap customTiledMap = applicationContext.getCustomTiledMap();
			
    		CosmodogGame cosmodogGame;
    		
    		try {
				cosmodogGame = InitializationUtils.initializeCosmodogGame(game, tiledMap, customTiledMap, "Armitage");
			} catch (TiledMapIoException e1) {
				throw new SlickException("Error while reading the cosmodog map", e1);
			}
    		
    		applicationContext.getCosmodog().setCosmodogGame(cosmodogGame);

    		Rectangle scene = Rectangle.fromSize((float) (tiledMap.getWidth() * tiledMap.getTileWidth()), (float) (tiledMap.getHeight() * tiledMap.getTileHeight()));

    		try {
    			cosmodogGame.setCam(new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, scene, mapDrawingContext.x(), mapDrawingContext.y(), mapDrawingContext.w(), mapDrawingContext.h()));
    		} catch (CamPositioningException e) {
    			Log.error("Camera positioning could not be established", e);
    		}

    		cloudsDeco = new CloudsDecoration(scene, 1000, 25, 25, 240, 240);
    		
    		birdsDecos.add(new BirdsDecoration(cosmodogGame.getCam(), 2, 20, 100f, 1.5f));
    		birdsDecos.add(new BirdsDecoration(cosmodogGame.getCam(), 2, 20, 100f, 2));
    		
    		Player player = cosmodogGame.getPlayer();
    		Cam cam = cosmodogGame.getCam();
    		cam.zoom(Constants.DEFAULT_CAM_ZOOM_FACTOR);
    		cam.focusOnPiece(tiledMap, 0, 0, player);

    		//Check for the rules of the new game event
			RuleBook ruleBook = cosmodogGame.getRuleBook();
			List<Rule> rulesSortedByPriority = ruleBook.getRulesSortedByPriority();
			for (Rule rule : rulesSortedByPriority) {
				rule.apply(new GameEventNewGame());
			}
    		
    		
    		
    		firstUpdate = true;
    		
		}
		
		//Do this to consume the key input.
		container.getInput().isKeyPressed(Input.KEY_RETURN);
		
		
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
    		cosmodogGame.getActionRegistry().update(n, gc, sbg);
    		
		}
		
		cosmodogGame.getInterfaceActionRegistry().update(n, gc, sbg);

		
		cloudsDeco.update(n);
		
		for (BirdsDecoration birdsDeco : birdsDecos) {
			birdsDeco.update(n);
		}

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
		
		//Draw ground effects.
		effectsRenderer.render(gc, g, mapDrawingContext, EffectsRendererParam.FOR_GROUND_EFFECTS);
		
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
		
		//Draw top parts of the map, f.i. roofs, tops of the pillars and trees. They will cover both player and NPC
		mapRenderer.render(gc, g, mapDrawingContext, topsLayersPredicate);
		
		//Draw the sight radius of the enemies.
		sightRadiusRenderer.render(gc, g, mapDrawingContext);
		
		//Draw top effects.
		effectsRenderer.render(gc, g, mapDrawingContext, EffectsRendererParam.FOR_TOP_EFFECTS);
		
		//Draw clouds.
		cloudRenderer.render(gc, g, mapDrawingContext, cloudsDeco.getCloudRectangles());
		
		//Draw birds.
		for (BirdsDecoration birdDeco : birdsDecos) {
			birdsRenderer.render(gc, g, mapDrawingContext, birdDeco);
		}

		//Draw Daytime mask.
		daytimeColorFilterRenderer.render(gc, g, mapDrawingContext, null);
		
		//Draw marked tiles, e.g. "fuel" sign
		markedTileRenderer.render(gc, g, mapDrawingContext, null);
		
		//Draw overhead notifications, e.g. "blocked" warning.
		overheadNotificationRenderer.render(gc, g, mapDrawingContext);
		
		
		lifeInterfaceRenderer.render(gc, g, lifeDrawingContext, null);
		arsenalInterfaceRenderer.render(gc, g, arsenalDrawingContext, null);
		
		vitalDataInterfaceRenderer.render(gc, g, vitalDataDrawingContext, null);
		gameProgressInterfaceRenderer.render(gc, g, gameProgressDrawingContext, null);
		weaponTooltipRenderer.render(gc, g, weaponTooltipsDrawingContext, null);
		// leftInterfaceRenderer.render(gc, g, leftColumnDrawingContext);
		rightInterfaceRenderer.render(gc, g, rightColumnDrawingContext, null);
	
		DrawingContext dialogBoxDrawingContext = ApplicationContext.instance().getDialogBoxDrawingContext();
		DrawingContext commentsWritingDrawingContext = DrawingContextUtils.writingContentDcFromDialogBoxDc(dialogBoxDrawingContext);
		
		WritingTextBoxState commentTextBoxState = cosmodogGame.getCommentsStateUpdater().getState();
		if (commentTextBoxState != null) {
			commentsRenderer.render(gc, g, commentsWritingDrawingContext, commentTextBoxState);
		}
		
		if (cosmodogGame.getWritingTextBoxState() != null) {
			g.setColor(new Color(0,0,0,0.75f));
			g.fillRect(0, 0, gameContainerDrawingContext.w(), gameContainerDrawingContext.h());
			dialogBoxRenderer.render(gc, g, dialogBoxDrawingContext, cosmodogGame.getWritingTextBoxState());
		}

		TextFrameRenderer tfr = new TextFrameRenderer();
		tfr.render(gc, g, new CenteredDrawingContext(mapDrawingContext, 250, 150), null);
		
	}


	@Override
	public int getID() {
		return CosmodogStarter.GAME_STATE_ID;
	}

}
