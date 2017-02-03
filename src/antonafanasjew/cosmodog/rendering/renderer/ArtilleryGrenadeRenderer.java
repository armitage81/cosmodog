package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TransitionUtils;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition.GrenadeTransition;

public class ArtilleryGrenadeRenderer extends AbstractRenderer {

	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

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

		Player player = ApplicationContextUtils.getPlayer();

		int playerPosX = player.getPositionX();
		int playerPosY = player.getPositionY();

		for (Enemy enemy : map.nearbyEnemies(player.getPositionX(), player.getPositionY(), 20)) {

			// We are only interested in ranged units here.
			if (enemy.getUnitType().isRangedUnit() == false) {
				continue;
			}

			int enemyPosX = enemy.getPositionX();
			int enemyPosY = enemy.getPositionY();

			FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();

			boolean enemyIsFighting = fightPhaseTransition != null && fightPhaseTransition.getEnemy().equals(enemy); 
			boolean enemyIsShooting = enemyIsFighting && fightPhaseTransition instanceof ArtilleryAttackingFightPhaseTransition;

			if (!enemyIsShooting) {
				continue;
			}

			// At this point we know that there is a ranged attack action
			// currently going on where the attacker is the enemy and the
			// defender is the player.

			ArtilleryAttackingFightPhaseTransition artilleryAttackingFightPhaseTransition = (ArtilleryAttackingFightPhaseTransition) fightPhaseTransition;
			List<GrenadeTransition> grenadeTransitions = artilleryAttackingFightPhaseTransition.grenadeTransitions();

			Animation risingGrenade = ApplicationContext.instance().getAnimations().get("artilleryGrenadeUp");
			Animation fallingGrenade = ApplicationContext.instance().getAnimations().get("artilleryGrenadeDown");

			float animationWidth = risingGrenade.getWidth();
			float animationHeight = risingGrenade.getHeight();

			float enemyX1 = (enemyPosX - tileNoX) * tileWidth;
			float enemyX2 = enemyX1 + tileWidth;
			float enemyY1 = (enemyPosY - tileNoY) * tileHeight;

			int leftRightOffset = 4;
			
			float risingLeftGrenadeX1 = enemyX1 - (leftRightOffset);
			float risingRightGrenadeX1 = enemyX2 - animationWidth + (leftRightOffset);
			float risingGrenadeY1Max = enemyY1 - animationHeight;
			float risingGrenadeY1Min = drawingContext.y() - animationHeight;
			float maxVerticalRisingGrenadeDistance = risingGrenadeY1Max - risingGrenadeY1Min;

			float playerX1 = (playerPosX - tileNoX) * tileWidth;
			float playerX2 = playerX1 + tileWidth;
			float playerY1 = (playerPosY - tileNoY) * tileHeight;

			float fallingLeftGrenadeX1 = playerX1 - (leftRightOffset);
			float fallingRightGrenadeX1 = playerX2 - animationWidth + (leftRightOffset);
			float fallingGrenadeY1Max = playerY1 + tileHeight - animationHeight;
			float fallingGrenadeY1Min = drawingContext.y() - animationHeight;
			float maxVerticalfallingGrenadeDistance = fallingGrenadeY1Max - fallingGrenadeY1Min;

			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

			for (int i = 0; i < grenadeTransitions.size(); i++) {
				GrenadeTransition gt = grenadeTransitions.get(i);
				float relativeHeight = gt.relativeHeight;
				float grenadeY1;
				float grenadeX1;
				Animation animation;

				if (gt.risingNotFalling) {
					grenadeY1 = risingGrenadeY1Max - (maxVerticalRisingGrenadeDistance * relativeHeight);
					grenadeX1 = gt.leftNotRight ? risingLeftGrenadeX1 : risingRightGrenadeX1;
					animation = risingGrenade;
				} else {
					grenadeY1 = fallingGrenadeY1Max - (maxVerticalfallingGrenadeDistance * relativeHeight);
					grenadeX1 = gt.leftNotRight ? fallingLeftGrenadeX1 : fallingRightGrenadeX1;
					animation = fallingGrenade;
				}

				animation.draw(grenadeX1, grenadeY1, animation.getWidth(), animation.getHeight());

			}

			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);

		}

	}

}
