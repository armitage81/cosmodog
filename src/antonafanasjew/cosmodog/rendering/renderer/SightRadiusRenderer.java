package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Set;

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
import antonafanasjew.cosmodog.view.transitions.EnemyAttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

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
		
		int scaledTileLength = (int) (tileLength * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileLength));
		int y = -(int) ((camY % scaledTileLength));
		
		int tileNoX = camX / scaledTileLength;
		int tileNoY = camY / scaledTileLength;

		int tilesW = (int) (cam.viewCopy().width()) / scaledTileLength + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileLength + 2;
		 
		Set<Position> sightMarkers = Sets.newHashSet();
		Set<Position> alertMarkers = Sets.newHashSet();

		Position position = Position.fromCoordinates(tileNoX, tileNoY);

		Set<Enemy> enemies = map.visibleEnemies(position, tilesW, tilesH, 2);
		
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


			FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();


			if (fightPhaseTransition != null) {
				if (fightPhaseTransition.getEnemy().equals(enemy)) {

					float completion = fightPhaseTransition.getCompletion();

					if (fightPhaseTransition instanceof EnemyAttackingFightPhaseTransition) {

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

					Position tilePosition = Position.fromCoordinates(i, j);
					if (visiblePositions.contains(tilePosition)) {
						if (playerInSightRange) {
							alertMarkers.add(Position.fromCoordinates(i, j));
						} else {
							sightMarkers.add(Position.fromCoordinates(i, j));
						}
					}
				}
			}
		}

		sightMarkers.removeAll(alertMarkers);

		for (Position alertMarker : alertMarkers) {
			Animation sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusAlertedMarker");
			
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			sightRadiusMarkerAnimation.draw((alertMarker.getX() - tileNoX) * tileLength, (alertMarker.getY() - tileNoY) * tileLength);
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
		}
		
		for (Position sightMarker : sightMarkers) {
			Animation sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusMarker");
			
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			sightRadiusMarkerAnimation.draw((sightMarker.getX() - tileNoX) * tileLength, (sightMarker.getY() - tileNoY) * tileLength);
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
		}
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
