package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.DynamicPiecesRenderer.DynamicPiecesRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer.EffectsRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.BottomLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TipsLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.maprendererpredicates.TopLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.NotOnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.piecerendererpredicates.OnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.renderer.pieces.OccupiedPlatformRenderer;

/**
 * Renders the scene, that is the game map and everything on it.
 */
public class SceneRenderer implements Renderer {

	private MapLayerRenderer mapRenderer = new MapLayerRenderer();
	
	private MapLayerRendererPredicate bottomLayersPredicate = new BottomLayersRenderingPredicate();
	private MapLayerRendererPredicate tipsLayersPredicate = new TipsLayersRenderingPredicate();
	private MapLayerRendererPredicate topsLayersPredicate = new TopLayersRenderingPredicate();
	
	private AbstractRenderer dynamicPiecesRenderer = new DynamicPiecesRenderer();
	private AbstractRenderer mineExplosionRenderer = new MineExplosionRenderer();
	private AbstractRenderer platformRenderer = new OccupiedPlatformRenderer();
	private AbstractRenderer northPiecesRenderer = new PiecesRenderer(true, false);
	private AbstractRenderer southPiecesRenderer = new PiecesRenderer(false, true);
	private AbstractRenderer playerRenderer = new PlayerRenderer();
	private AbstractRenderer npcRenderer = new NpcRenderer();
	private AbstractRenderer platformPiecesRenderer = new PiecesRenderer(true, true);

	private AbstractRenderer radiationRenderer = new RadiationRenderer();

	private AbstractRenderer cloudRenderer = new CloudRenderer(ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS));
	private AbstractRenderer birdsRenderer = new BirdsRenderer();
	private AbstractRenderer snowflakesRenderer = new SnowflakesRenderer();
	private AbstractRenderer wormAttackRenderer = new WormAttackRenderer();
	private AbstractRenderer artilleryGrenadeRenderer = new ArtilleryGrenadeRenderer();
	private AbstractRenderer wormSnowSparkRenderer = new WormSnowSparkRenderer();
	private AbstractRenderer effectsRenderer = new EffectsRenderer();
	private AbstractRenderer overheadNotificationRenderer = new OverheadNotificationRenderer();
	private Renderer daytimeColorFilterRenderer = new DayTimeFilterRenderer();
	private AbstractRenderer sightRadiusRenderer = new SightRadiusRenderer();
	
	@Override
	public void render(GameContainer gc, Graphics g, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		//Draw "ground" part of the map
		mapRenderer.render(gc, g, bottomLayersPredicate);
		
		//Draw "tips" part of the map (Tile that would partially cover the player, like high flowers. Take note, it is still drawn underneath the player, as partial coverage is realized via special sprites)
		mapRenderer.render(gc, g, tipsLayersPredicate);
		
		//Draw ground dynamic pieces
		dynamicPiecesRenderer.render(gc, g, DynamicPiecesRendererParam.BOTTOM);
		
		//Draw ground effects.
		effectsRenderer.render(gc, g, EffectsRendererParam.FOR_GROUND_EFFECTS);

		//Draw mine explosion
		mineExplosionRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw the platform here. Note: This renderer handles the occupied platform. The standalone platform is a normal piece and will be rendered with the piece renderers.
		platformRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw pieces that are north from the player and will be covered by his shape. (Ignore pieces that are on the platform)
		northPiecesRenderer.render(gc, g, new NotOnPlatformPieceRendererPredicate());
		
		//Draw the player
		playerRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw NPC
		npcRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw pieces that are south from the player and will not be covered by his shape.  (Ignore pieces that are on the platform)
		southPiecesRenderer.render(gc, g, new NotOnPlatformPieceRendererPredicate());
		
		//Draw pieces that are on the platform.
		platformPiecesRenderer.render(gc, g, new OnPlatformPieceRendererPredicate());
		
		//Draw top dynamic pieces.
		dynamicPiecesRenderer.render(gc, g, DynamicPiecesRendererParam.TOP);
		
		//Draw top parts of the map, f.i. roofs, tops of the pillars and trees. They will cover both player and NPC
		mapRenderer.render(gc, g, topsLayersPredicate);
		
		//Draw attacking worm
		wormAttackRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw Snow sparks covering the worm.
		wormSnowSparkRenderer.render(gc, g, sceneDrawingContext);

		//Draw the radiation clouds.
		radiationRenderer.render(gc, g, sceneDrawingContext);

		//Draw the sight radius of the enemies.
		sightRadiusRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw artillery grenades if it is shooting.
		artilleryGrenadeRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw top effects.
		effectsRenderer.render(gc, g, EffectsRendererParam.FOR_TOP_EFFECTS);
		
		//Draw clouds.
		cloudRenderer.render(gc, g, sceneDrawingContext);
		
		//Draw birds.
		birdsRenderer.render(gc, g, sceneDrawingContext);

		//Draw snowflakes.
		snowflakesRenderer.render(gc, g, sceneDrawingContext);

		//Draw Daytime mask.
		daytimeColorFilterRenderer.render(gc, g, null);
		
		//Draw overhead notifications, e.g. "blocked" warning.
		overheadNotificationRenderer.render(gc, g, sceneDrawingContext);
	}

}
