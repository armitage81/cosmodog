package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Set;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.sight.Sight;
import antonafanasjew.cosmodog.sight.SightModifier;
import antonafanasjew.cosmodog.sight.VisibilityCalculator;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.EnemiesUtils;
import antonafanasjew.cosmodog.util.TransitionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.EnemyAttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

import com.google.common.collect.Sets;

public class SightRadiusRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		Player player = ApplicationContextUtils.getPlayer();
		
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.getCosmodogMap();
		
		Cam cam = cosmodogGame.getCam();
		
		int tileWidth = map.getTileWidth();
		int tileHeight = map.getTileHeight();

		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));
		
		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;

		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		 
		Set<Position> sightMarkers = Sets.newHashSet();
		Set<Position> alertMarkers = Sets.newHashSet();
		
		Set<Enemy> enemies = map.visibleEnemies(tileNoX, tileNoY, tilesW, tilesH, 2);
		
		EnemiesUtils.removeInactiveUnits(enemies);
				
		for (Enemy enemy : enemies) {

			float movementOffsetX = 0;
			float movementOffsetY = 0;
			
			ActorTransition enemyTransition = cosmodogGame.getActorTransitionRegistry().get(enemy);
			
			FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();
			

			float fightOffsetX = 0.0f;
			float fightOffsetY = 0.0f;
			
			if (enemyTransition != null) {
				movementOffsetX = tileWidth * enemyTransition.getTransitionalOffsetX();
				movementOffsetY = tileHeight * enemyTransition.getTransitionalOffsetY();
				
			}
			
			if (fightPhaseTransition != null) {
				if (fightPhaseTransition.getEnemy().equals(enemy)) {
					
					float completion = fightPhaseTransition.getCompletion();
					float fightOffset = 0.0f;
					
					if (fightPhaseTransition instanceof EnemyAttackingFightPhaseTransition) {
						
						if (completion > 0.5f) {
							completion = 1.0f - completion;
						}
						fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;
						
					}
					
					if (enemy.getDirection() == DirectionType.DOWN) {
						fightOffsetY = fightOffset;
					}
					
					if (enemy.getDirection() == DirectionType.UP) {
						fightOffsetY = -fightOffset;
					}
					
					if (enemy.getDirection() == DirectionType.RIGHT) {
						fightOffsetX = fightOffset;
					}
					
					if (enemy.getDirection() == DirectionType.LEFT) {
						fightOffsetX = -fightOffset;
					}
						
				}
			}
			
			SightModifier sightModifier = cosmodog.getSightModifier();
			PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
			
			
			boolean playerInSightRange = false;
			
			Set<Sight> sights = enemy.getSights();

			for (Sight sight : sights) {
				Sight modifiedSight = sightModifier.modifySight(sight, planetaryCalendar);
				VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(modifiedSight, enemy, tileWidth, tileHeight);
				playerInSightRange = visibilityCalculator.visible(player, tileWidth, tileHeight);
				if (playerInSightRange) {
					break;
				}
			}
			
			for (Sight sight : sights) {
			
				Sight modifiedSight = sightModifier.modifySight(sight, planetaryCalendar);
				float sightDistance = modifiedSight.getDistance();
				int sightDistanceInTiles = (int)(sightDistance / map.getTileWidth()); //Works only for quadratic tiles.
				
				VisibilityCalculator visibilityCalculator = VisibilityCalculator.create(modifiedSight, enemy, tileWidth, tileHeight);
				
				for (int i = enemy.getPositionX() - sightDistanceInTiles; i <= enemy.getPositionX() + sightDistanceInTiles; i++) {
					for (int j = enemy.getPositionY() - sightDistanceInTiles; j <= enemy.getPositionY() + sightDistanceInTiles; j++) {
						
						if (i == enemy.getPositionX() && j == enemy.getPositionY()) {
							continue;
						}
						
						if (i < 0 || i >= map.getWidth() || j < 0 || j >= map.getHeight()) {
							continue;
						}
						
						Piece tilePiece = new Piece();
						tilePiece.setPositionX(i);
						tilePiece.setPositionY(j);
						
						boolean tileVisible = visibilityCalculator.visible(tilePiece, tileWidth, tileHeight);
						
						if (tileVisible) {
	
							if (playerInSightRange) {
								alertMarkers.add(Position.fromCoordinates(i, j));
							} else {
								sightMarkers.add(Position.fromCoordinates(i, j));
							}
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
			
			sightRadiusMarkerAnimation.draw((alertMarker.getX() - tileNoX) * tileWidth, (alertMarker.getY() - tileNoY) * tileHeight);
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
		}
		
		for (Position sightMarker : sightMarkers) {
			Animation sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusMarker");
			
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			sightRadiusMarkerAnimation.draw((sightMarker.getX() - tileNoX) * tileWidth, (sightMarker.getY() - tileNoY) * tileHeight);
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
		}
		
		
		
		
	}

}
