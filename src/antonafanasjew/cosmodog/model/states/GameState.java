package antonafanasjew.cosmodog.model.states;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
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
import antonafanasjew.cosmodog.rendering.renderer.BirdsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.CloudRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DayTimeFilterRenderer;
import antonafanasjew.cosmodog.rendering.renderer.DialogBoxRenderer;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer.EffectsRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.LeftInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MapLayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.MarkedTileRenderer;
import antonafanasjew.cosmodog.rendering.renderer.NpcRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PiecesRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PlayerNotificationRenderer;
import antonafanasjew.cosmodog.rendering.renderer.PlayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.Renderer;
import antonafanasjew.cosmodog.rendering.renderer.RightInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.SightRadiusRenderer;
import antonafanasjew.cosmodog.rendering.renderer.TextFrameRenderer;
import antonafanasjew.cosmodog.rendering.renderer.TopInterfaceRenderer;
import antonafanasjew.cosmodog.rendering.renderer.WritingRenderer;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.BottomLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TipsLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TopLayersRenderingPredicate;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleBook;
import antonafanasjew.cosmodog.rules.events.GameEventNewGame;
import antonafanasjew.cosmodog.tiledmap.io.TiledMapIoException;
import antonafanasjew.cosmodog.topology.Rectangle;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.DrawingContextUtils;
import antonafanasjew.cosmodog.util.InitializationUtils;
import antonafanasjew.cosmodog.writing.textbox.WritingTextBoxState;

import com.google.common.collect.Lists;

public class GameState extends BasicGameState {

	private boolean firstUpdate;
	
	private ApplicationContext applicationContext = ApplicationContext.instance();
	
	
	private DrawingContext gameContainerDrawingContext;
	// private DrawingContext leftColumnDrawingContext;
	private DrawingContext middleColumnDrawingContext;
	private DrawingContext rightColumnDrawingContext;
	private DrawingContext topDrawingContext;
	private DrawingContext mapDrawingContext;
	private DrawingContext notificationsDrawingContext;
	
	private DrawingContext tutorialDrawingContext;
	
	private CloudsDecoration cloudsDeco;
	private List<BirdsDecoration> birdsDecos = Lists.newArrayList();
	
	private AbstractRenderer cloudRenderer;
	private AbstractRenderer birdsRenderer;
	private Renderer notificationRenderer;
	private Renderer bottomInterfaceRenderer;
	private Renderer topInterfaceRenderer;
	private AbstractRenderer leftInterfaceRenderer;
	private Renderer rightInterfaceRenderer;
	private AbstractRenderer mapRenderer;
	private AbstractRenderer playerRenderer;
	private AbstractRenderer npcRenderer;
	private AbstractRenderer northPiecesRenderer;
	private AbstractRenderer southPiecesRenderer;
	private AbstractRenderer effectsRenderer;
	private AbstractRenderer playerNotificationRenderer;
	private Renderer daytimeColorFilterRenderer;
	private Renderer markedTileRenderer;
	private AbstractRenderer sightRadiusRenderer;
	
	private MapLayerRendererPredicate bottomLayersPredicate;
	private MapLayerRendererPredicate tipsLayersPredicate;
	private MapLayerRendererPredicate topsLayersPredicate;
	
	private DialogBoxRenderer dialogBoxRenderer;
	private WritingRenderer commentsRenderer;
	private WritingRenderer tutorialsRenderer;
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {

		gc.setVSync(true);
		gc.setShowFPS(false);
		
		gameContainerDrawingContext = new SimpleDrawingContext(null, 0, 0, gc.getWidth(), gc.getHeight());

		// leftColumnDrawingContext = new
		// TileDrawingContext(gameContainerDrawingContext, 5, 1, 0, 0);
		topDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 8, 0, 0);
		middleColumnDrawingContext = new TileDrawingContext(gameContainerDrawingContext, 1, 8, 0, 1, 1, 7);
		
		mapDrawingContext = new TileDrawingContext(middleColumnDrawingContext, 5, 1, 0, 0, 4, 1);
		
		notificationsDrawingContext = new TileDrawingContext(mapDrawingContext, 1, 10, 0, 0, 1, 1);

		rightColumnDrawingContext = new TileDrawingContext(middleColumnDrawingContext, 5, 1, 4, 0);
		
		tutorialDrawingContext = new CenteredDrawingContext(mapDrawingContext, mapDrawingContext.w() - 20, mapDrawingContext.h() - 20);
		
		//Update the global dialog drawing context variable in the application context.
		ApplicationContext.instance().setDialogBoxDrawingContext(new TileDrawingContext(mapDrawingContext, 1, 5, 0, 4));
		
		cloudRenderer = new CloudRenderer(applicationContext.getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS));
		
		birdsRenderer = new BirdsRenderer();
		
		topInterfaceRenderer = new TopInterfaceRenderer();
		leftInterfaceRenderer = new LeftInterfaceRenderer();
		rightInterfaceRenderer = new RightInterfaceRenderer();
		
		mapRenderer = new MapLayerRenderer();
		playerRenderer = new PlayerRenderer();
		npcRenderer = new NpcRenderer();
		northPiecesRenderer = new PiecesRenderer(true, false);
		southPiecesRenderer = new PiecesRenderer(false, true);
		effectsRenderer = new EffectsRenderer();
		playerNotificationRenderer = new PlayerNotificationRenderer();
		daytimeColorFilterRenderer = new DayTimeFilterRenderer();
		markedTileRenderer = new MarkedTileRenderer();
		sightRadiusRenderer = new SightRadiusRenderer();
		
		commentsRenderer = new WritingRenderer(false, new Color(0, 0, 1, 0.5f));
		tutorialsRenderer = new WritingRenderer(false, new Color(0, 0, 0, 0.75f));
		dialogBoxRenderer = new DialogBoxRenderer();
		
	}

	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {

		Cosmodog cosmodog = applicationContext.getCosmodog();
		
		if (cosmodog.getGameLifeCycle().isStartNewGame()) {

			TiledMap tiledMap = applicationContext.getTiledMap();
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
    		cam.focusOnPiece(tiledMap, 0, 0, player, Cam.DEFAULT_FOCUS_PADDING);

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
		if (cosmodogGame.getInterfaceActionRegistry().getRegisteredAction(AsyncActionType.BLOCKING_INTERFACE) == null) {

			if (input.isKeyPressed(Input.KEY_0)) {
    			sbg.enterState(CosmodogStarter.DEBUG_STATE_ID);
    		}
    
    		if (input.isKeyDown(Input.KEY_LCONTROL) || input.isKeyDown(Input.KEY_RCONTROL)) {
    			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME_CONTROL).handleInput(gc, sbg, n, applicationContext);
    		} else {
    			cosmodog.getInputHandlers().get(InputHandlerType.INPUT_HANDLER_INGAME).handleInput(gc, sbg, n, applicationContext);
    		}
        		
		}
		
		cosmodogGame.getChronometer().update(n);
		cosmodogGame.getPlayerStatusNotificationList().update(n);
		cosmodogGame.getCommentsStateUpdater().update(n);
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

		//We need to initialize the predicates here as they are depending on the player movement.
		bottomLayersPredicate = new BottomLayersRenderingPredicate();
		tipsLayersPredicate = new TipsLayersRenderingPredicate();
		topsLayersPredicate = new TopLayersRenderingPredicate();
		
		
		mapRenderer.render(gc, g, mapDrawingContext, bottomLayersPredicate);
		mapRenderer.render(gc, g, mapDrawingContext, tipsLayersPredicate);
		
		effectsRenderer.render(gc, g, mapDrawingContext, EffectsRendererParam.FOR_GROUND_EFFECTS);
		
		northPiecesRenderer.render(gc, g, mapDrawingContext);
		
		if (!cosmodogGame.getActionRegistry().isActionRegistered(AsyncActionType.MOVEMENT)) {
			playerRenderer.render(gc, g, mapDrawingContext);
		}
		
		
		if (cosmodogGame.getActionRegistry().isActionRegistered(AsyncActionType.MOVEMENT)) {
			playerRenderer.render(gc, g, mapDrawingContext);
		}
		
		
		npcRenderer.render(gc, g, mapDrawingContext);
		
		southPiecesRenderer.render(gc, g, mapDrawingContext);
		
		mapRenderer.render(gc, g, mapDrawingContext, topsLayersPredicate);
		
		sightRadiusRenderer.render(gc, g, mapDrawingContext);
		
		effectsRenderer.render(gc, g, mapDrawingContext, EffectsRendererParam.FOR_TOP_EFFECTS);			
		cloudRenderer.render(gc, g, mapDrawingContext, cloudsDeco.getCloudRectangles());
		
		for (BirdsDecoration birdDeco : birdsDecos) {
			birdsRenderer.render(gc, g, mapDrawingContext, birdDeco);
		}

		daytimeColorFilterRenderer.render(gc, g, mapDrawingContext, null);
		markedTileRenderer.render(gc, g, mapDrawingContext, null);
		playerNotificationRenderer.render(gc, g, mapDrawingContext);
		topInterfaceRenderer.render(gc, g, topDrawingContext, null);
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
		
//		DrawingContext dc = new TileDrawingContext(mapDrawingContext, 3, 3, 0, 0);
//		//String startMessage = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_'abcdefghijklmnopqrstuvwxyz";
//		String startMessage = "Yul'ka-Kozyul'ka!!! Antoshka-Kartoshka!!!";
//		Map<Character, Letter> letters = ApplicationContext.instance().getCharacterLetters();
//		Letter defaultLetter = letters.get('?');
//		List<Letter> lettersForText = LetterUtils.lettersForText(startMessage, letters, defaultLetter);
//		Rectangle messageBounds = LetterUtils.letterListBounds(lettersForText, 0.0f);
//		DrawingContext centeredDc = new CenteredDrawingContext(dc, messageBounds.getWidth(), messageBounds.getHeight());
//		List<DrawingContext> lettersDcs = LetterUtils.letterLineDrawingContexts(lettersForText, 0.0f, centeredDc);
//		
//		g.scale(2,2);
//		
//		g.fillRect(centeredDc.x(), centeredDc.y(), centeredDc.w(), centeredDc.h());
//		
//		for (int i = 0; i < lettersForText.size(); i++) {
//			Letter letter = lettersForText.get(i);
//			DrawingContext letterDc = lettersDcs.get(i);
//			g.drawImage(letter.getImage(), letterDc.x(), letterDc.y());
//		}
//		
//		g.scale(1/2, 1/2);
		
	}


	@Override
	public int getID() {
		return CosmodogStarter.GAME_STATE_ID;
	}

}
