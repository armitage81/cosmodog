package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.calendar.PlanetaryCalendar;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.sight.SightRadiusCalculator;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

public class SightRadiusRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		CustomTiledMap tiledMap = applicationContext.getCustomTiledMap();
		Player player = ApplicationContextUtils.getPlayer();
		
		Cosmodog cosmodog = ApplicationContextUtils.getCosmodog();
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap cosmodogMap = ApplicationContextUtils.getCosmodogMap();
		
		Cam cam = cosmodogGame.getCam();
		
		int tileWidth = tiledMap.getTileWidth();
		int tileHeight = tiledMap.getTileHeight();

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
		
		for (Enemy enemy : cosmodogMap.visibleEnemies(tileNoX, tileNoY, tilesW, tilesH, 2)) {

			float movementOffsetX = 0;
			float movementOffsetY = 0;
			
			ActorTransition enemyTransition = cosmodogGame.getActorTransitionRegistry().get(enemy);
			FightPhaseTransition fightPhaseTransition = cosmodogGame.getFightPhaseTransition();
			

			float fightOffsetX = 0.0f;
			float fightOffsetY = 0.0f;
			
			if (enemyTransition != null) {
				movementOffsetX = tileWidth * enemyTransition.getTransitionalOffsetX();
				movementOffsetY = tileHeight * enemyTransition.getTransitionalOffsetY();
				
			}
			
			if (fightPhaseTransition != null) {
				if (fightPhaseTransition.enemy.equals(enemy)) {
					
					float completion = fightPhaseTransition.completion;
					float fightOffset = 0.0f;
					
					if (fightPhaseTransition.playerAttack == false) {
						
						if (!fightPhaseTransition.enemyDestruction) {
							if (completion > 0.5f) {
								completion = 1.0f - completion;
							}
							fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;
						}
						
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
			
			SightRadiusCalculator sightRadiusCalculator = cosmodog.getSightRadiusCalculator();
			PlanetaryCalendar planetaryCalendar = cosmodogGame.getPlanetaryCalendar();
			int sightRadius = sightRadiusCalculator.calculateSightRadius(enemy, planetaryCalendar);
			
			for (int i = enemy.getPositionX() - sightRadius; i <= enemy.getPositionX() + sightRadius; i++) {
				for (int j = enemy.getPositionY() - sightRadius; j <= enemy.getPositionY() + sightRadius; j++) {
					int xDistance = enemy.getPositionX() - i;
					xDistance = xDistance < 0 ? -xDistance : xDistance;
					int yDistance = enemy.getPositionY() - j;
					yDistance = yDistance < 0 ? -yDistance : yDistance;
					int distance = xDistance + yDistance;

					if (distance <= sightRadius) {
						int distanceToPlayer = (int) CosmodogMapUtils.distanceBetweenPositions(Position.fromCoordinates(player.getPositionX(), player.getPositionY()), Position.fromCoordinates(enemy.getPositionX(), enemy.getPositionY()));

						Animation sightRadiusMarkerAnimation;
						if (sightRadius >= distanceToPlayer) {
							sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusAlertedMarker");
						} else {
							sightRadiusMarkerAnimation = ApplicationContext.instance().getAnimations().get("sightRadiusMarker");
						}
						
						graphics.translate(x, y);
						graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
						
						sightRadiusMarkerAnimation.draw((i - tileNoX) * tileWidth + movementOffsetX + fightOffsetX, (j - tileNoY) * tileHeight + movementOffsetY + fightOffsetY);
						
						graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
						graphics.translate(-x, -y);
					}
				}
			}

		}
	}

}
