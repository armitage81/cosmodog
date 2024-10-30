package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Optional;
import java.util.Set;

import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.EnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.FightActionUtils;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.*;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Features;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;

public class SightRadiusRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		if (!Features.getInstance().featureOn(Features.FEATURE_SIGHTRADIUS)) {
			return;
		}
		
		Player player = ApplicationContextUtils.getPlayer();
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		 
		Set<Position> sightMarkers = Sets.newHashSet();
		Set<Position> alertMarkers = Sets.newHashSet();

		Position position = Position.fromCoordinates(camTilePosition.tileX(), camTilePosition.tileY(), map.getMapType());

		Set<Enemy> enemies = map.visibleEnemies(position, camTilePosition.widthInTiles(), camTilePosition.heightInTiles(), 2);
		
		EnemiesUtils.removeInactiveUnits(enemies);
				
		Set<TiledObject> roofsOverPlayer = PlayerMovementCache.getInstance().getRoofRegionsOverPlayer();
		Set<TiledObject> roofRemovalBlockersOverPlayer = PlayerMovementCache.getInstance().getRoofRemovalBlockerRegionsOverPlayer();

		if (!roofRemovalBlockersOverPlayer.isEmpty()) {
			return;
		}

		for (Enemy enemy : enemies) {

			Set<TiledObject> roofsOverEnemy = PlayerMovementCache.getInstance().getEnemiesInRangeWithRoofsOverThem().get(enemy);

			if (roofsOverEnemy != null) {
				Set<TiledObject> roofsIntersection = Sets.newHashSet();
				roofsIntersection.addAll(roofsOverEnemy);
				roofsIntersection.retainAll(roofsOverPlayer);
				if (roofsIntersection.isEmpty()) {
					continue;
				}
			}


			Optional<AbstractFightActionPhase> optFightPhase = FightActionUtils.currentFightPhase();


			if (optFightPhase.isPresent()) {

				Enemy fightPhaseEnemy = (Enemy)optFightPhase.get().getProperties().get("enemy");
				if (fightPhaseEnemy.equals(enemy)) {

					float completion = optFightPhase.get().getCompletionRate();

					if (optFightPhase.get() instanceof EnemyAttackActionPhase) {

						if (completion > 0.5f) {
							completion = 1.0f - completion;
						}

					}

				}
			}

			PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();


			VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(enemy.getDefaultVision(), enemy.getNightVision(), enemy.getStealthVision());
			boolean playerInSightRange = visibilityCalculator.visible(enemy, planetaryCalendar, map, player);

			Set<Position> visiblePositions = visibilityCalculator.allVisiblePositions(enemy, planetaryCalendar, map, player);

			int maxDistance = visibilityCalculator.maxVisibilityRange(planetaryCalendar, map, player);

			int enemyPosX = (int)enemy.getPosition().getX();
			int enemyPosY = (int)enemy.getPosition().getY();

			for (int i = enemyPosX - maxDistance; i <= enemyPosX + maxDistance; i++) {

				for (int j = enemyPosY - maxDistance; j <= enemyPosY + maxDistance; j++) {

					if (i == enemyPosX && j == enemyPosY) {
						continue;
					}

					if (i < 0 || i >= map.getMapType().getWidth() || j < 0 || j >= map.getMapType().getHeight()) {
						continue;
					}

					Position tilePosition = Position.fromCoordinates(i, j, enemy.getPosition().getMapType());
					if (visiblePositions.contains(tilePosition)) {
						if (playerInSightRange) {
							alertMarkers.add(Position.fromCoordinates(i, j, enemy.getPosition().getMapType()));
						} else {
							sightMarkers.add(Position.fromCoordinates(i, j, enemy.getPosition().getMapType()));
						}
					}
				}
			}
		}

		sightMarkers.removeAll(alertMarkers);

		for (Position alertMarker : alertMarkers) {

			Vector alertMarkerVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(alertMarker, camTilePosition);

			Animation sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusAlertedMarker");
			
			graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			sightRadiusMarkerAnimation.draw(alertMarkerVectorRelatedToCam.getX(), alertMarkerVectorRelatedToCam.getY());
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		}
		
		for (Position sightMarker : sightMarkers) {

			Vector sightMarkerVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(sightMarker, camTilePosition);

			Animation sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusMarker");
			
			graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			sightRadiusMarkerAnimation.draw(sightMarkerVectorRelatedToCam.getX(), sightMarkerVectorRelatedToCam.getY());
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		}
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
