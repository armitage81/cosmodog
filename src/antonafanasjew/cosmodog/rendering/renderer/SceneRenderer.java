package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.portals.Ray;
import antonafanasjew.cosmodog.rendering.renderer.dynamicpieces.DynamicPiecesRenderer;
import antonafanasjew.cosmodog.rendering.renderer.player.PlayerRenderer;
import antonafanasjew.cosmodog.rendering.renderer.portals.PortalRenderer;
import antonafanasjew.cosmodog.rendering.renderer.portals.RayRenderer;
import antonafanasjew.cosmodog.rendering.renderer.textbook.BackgroundRenderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.dynamicpieces.DynamicPiecesRenderer.DynamicPiecesRendererParam;
import antonafanasjew.cosmodog.rendering.renderer.EffectsRenderer.EffectsRendererParam;
import antonafanasjew.cosmodog.rendering.maprendererpredicates.BottomLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.maprendererpredicates.MapLayerRendererPredicate;
import antonafanasjew.cosmodog.rendering.maprendererpredicates.TipsLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.maprendererpredicates.TopLayersRenderingPredicate;
import antonafanasjew.cosmodog.rendering.piecerendererpredicates.NotOnPlatformPieceRendererPredicate;
import antonafanasjew.cosmodog.rendering.piecerendererpredicates.OnPlatformPieceRendererPredicate;
import profiling.ProfilerUtils;

/**
 * Renders the scene, that is the game map and everything on it.
 */
public class SceneRenderer extends AbstractRenderer {

	private AbstractRenderer backgroundRenderer = ConditionalRenderer.instance(
			new BackgroundRenderer(),
			null,
			() -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapType() == MapType.SPACE
	);

	private AbstractRenderer backgroundCloudRenderer = ConditionalRenderer.instance(
			new BackgroundCloudRenderer(ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS)),
			null,
			() -> ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation().getMapType() == MapType.SPACE
	);

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

	private AbstractRenderer portalRenderer = new PortalRenderer();
	private AbstractRenderer rayRenderer = new RayRenderer();


	private AbstractRenderer cloudRenderer = ConditionalRenderer.instanceWithSkyDecorationsActiveCondition(new CloudRenderer(ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_CLOUDS)));
	private AbstractRenderer birdsRenderer = ConditionalRenderer.instanceWithSkyDecorationsActiveCondition(new BirdsRenderer());
	private AbstractRenderer snowflakesRenderer = new SnowflakesRenderer();
	private AbstractRenderer wormAttackRenderer = new WormAttackRenderer();
	private AbstractRenderer artilleryGrenadeRenderer = new ArtilleryGrenadeRenderer();
	private AbstractRenderer wormSnowSparkRenderer = new WormSnowSparkRenderer();
	private AbstractRenderer effectsRenderer = new EffectsRenderer();
	private AbstractRenderer spaceliftGoingUpRenderer = new SpaceliftGoingUpRenderer();
	private AbstractRenderer spaceliftGoingDownRenderer = new SpaceliftGoingDownRenderer();


	private AbstractRenderer overheadNotificationRenderer = new OverheadNotificationRenderer();
	private Renderer daytimeColorFilterRenderer = ConditionalRenderer.instanceWithDayNightActiveCondition(new DayTimeFilterRenderer());
	private AbstractRenderer sightRadiusRenderer = new SightRadiusRenderer();
	
	@Override
	public void renderInternally(GameContainer gc, Graphics g, Object renderingParameter) {
		
		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();


		ProfilerUtils.runWithProfiling("SceneRenderer.backgrounds", () -> {
			//Skybox renderer. (Everything what is beneath the map, for instance stars beneath the space lab)
			backgroundRenderer.render(gc, g, null);
			//Clouds in the background when in the space station.
			backgroundCloudRenderer.render(gc, g, null);
		});


		ProfilerUtils.runWithProfiling("SceneRenderer.map", () -> {

			//Draw "ground" part of the map
			mapRenderer.render(gc, g, bottomLayersPredicate);

			//Draw "tips" part of the map (Tile that would partially cover the player, like high flowers. Take note, it is still drawn underneath the player, as partial coverage is realized via special sprites)
			mapRenderer.render(gc, g, tipsLayersPredicate);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.dynamicPiecesSouth", () -> {

			//Draw ground dynamic pieces
			dynamicPiecesRenderer.render(gc, g, DynamicPiecesRendererParam.BOTTOM);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.portalsAndRays", () -> {

			//Draw bottom portals
			portalRenderer.render(gc, g, PortalRenderer.PortalRendererParam.BOTTOM);
			//Bottom of the ray, which is the base portal outline
			rayRenderer.render(gc, g, RayRenderer.RayRendererParam.BOTTOM);
		});


		ProfilerUtils.runWithProfiling("SceneRenderer.effects", () -> {
			//Draw ground effects.
			effectsRenderer.render(gc, g, EffectsRendererParam.FOR_GROUND_EFFECTS);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.mineExplosions", () -> {
			//Draw mine explosion
			mineExplosionRenderer.render(gc, g, sceneDrawingContext);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.platform", () -> {
			//Draw the platform here. Note: This renderer handles the occupied platform. The standalone platform is a normal piece and will be rendered with the piece renderers.
			platformRenderer.render(gc, g, sceneDrawingContext);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.dynamicPiecesNorth", () -> {
			//Draw pieces that are north from the player and will be covered by his shape. (Ignore pieces that are on the platform)
			northPiecesRenderer.render(gc, g, new NotOnPlatformPieceRendererPredicate());
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.player", () -> {
			//Draw the player
			playerRenderer.render(gc, g, sceneDrawingContext);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.npc", () -> {
			//Draw NPC
			npcRenderer.render(gc, g, sceneDrawingContext);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.rest", () -> {

			//Draw pieces that are south from the player and will not be covered by his shape.  (Ignore pieces that are on the platform)
			southPiecesRenderer.render(gc, g, new NotOnPlatformPieceRendererPredicate());

			//Draw pieces that are on the platform.
			platformPiecesRenderer.render(gc, g, new OnPlatformPieceRendererPredicate());

			//Draw top dynamic pieces.
			dynamicPiecesRenderer.render(gc, g, DynamicPiecesRendererParam.TOP);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.map", () -> {
			//Draw top parts of the map, f.i. roofs, tops of the pillars and trees. They will cover both player and NPC
			mapRenderer.render(gc, g, topsLayersPredicate);
		});

		ProfilerUtils.runWithProfiling("SceneRenderer.rest", () -> {
			//Draw top portals.
			portalRenderer.render(gc, g, PortalRenderer.PortalRendererParam.TOP);

			//Render space lift animations
			spaceliftGoingUpRenderer.render(gc, g, sceneDrawingContext);
			spaceliftGoingDownRenderer.render(gc, g, sceneDrawingContext);

			//Draw attacking worm
			wormAttackRenderer.render(gc, g, sceneDrawingContext);

			//Draw Snow sparks covering the worm.
			wormSnowSparkRenderer.render(gc, g, sceneDrawingContext);

			//Draw the radiation clouds.
			radiationRenderer.render(gc, g, sceneDrawingContext);

			//Draw portal gun ray.
			rayRenderer.render(gc, g, RayRenderer.RayRendererParam.TOP);


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
		});

	}

}
