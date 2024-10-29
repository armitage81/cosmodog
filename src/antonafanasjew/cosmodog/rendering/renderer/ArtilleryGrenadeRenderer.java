package antonafanasjew.cosmodog.rendering.renderer;

import java.util.List;
import java.util.Optional;

import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.ArtilleryAttackActionPhase;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.actions.fight.FightActionUtils;

public class ArtilleryGrenadeRenderer extends AbstractRenderer {

	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		Cam cam = cosmodogGame.getCam();

		int scaledTileWidth = (int) (tileLength * cam.getZoomFactor());
		int scaledTileHeight = (int) (tileLength * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileWidth));
		int y = -(int) ((camY % scaledTileHeight));

		int tileNoX = camX / scaledTileWidth;
		int tileNoY = camY / scaledTileHeight;

		Player player = ApplicationContextUtils.getPlayer();

		for (Enemy enemy : map.nearbyEnemies(player.getPosition(), 20)) {

			// We are only interested in ranged units here.
			if (!enemy.getUnitType().isRangedUnit()) {
				continue;
			}

			Optional<AbstractFightActionPhase> optFightPhase = FightActionUtils.currentFightPhase();

			boolean enemyIsFighting = optFightPhase.isPresent() && ((Enemy)optFightPhase.get().getProperties().get("enemy")).equals(enemy);
			boolean enemyIsShooting = enemyIsFighting && (optFightPhase.get() instanceof ArtilleryAttackActionPhase);

			if (!enemyIsShooting) {
				continue;
			}



			// At this point we know that there is a ranged attack action
			// currently going on where the attacker is the enemy and the
			// defender is the player.

			ArtilleryAttackActionPhase artilleryAttackActionPhase = (ArtilleryAttackActionPhase)optFightPhase.get();

			List<ArtilleryAttackActionPhase.Grenade> grenades = artilleryAttackActionPhase.grenades();

			Animation risingGrenade = ApplicationContext.instance().getAnimations().get("artilleryGrenadeUp");
			Animation fallingGrenade = ApplicationContext.instance().getAnimations().get("artilleryGrenadeDown");

			float animationWidth = risingGrenade.getWidth();
			float animationHeight = risingGrenade.getHeight();

			float enemyX1 = (enemy.getPosition().getX() - tileNoX) * tileLength;
			float enemyX2 = enemyX1 + tileLength;
			float enemyY1 = (enemy.getPosition().getY() - tileNoY) * tileLength;

			int leftRightOffset = 4;
			
			float risingLeftGrenadeX1 = enemyX1 - (leftRightOffset);
			float risingRightGrenadeX1 = enemyX2 - animationWidth + (leftRightOffset);
			float risingGrenadeY1Max = enemyY1 - animationHeight;
			float risingGrenadeY1Min = sceneDrawingContext.y() - animationHeight;
			float maxVerticalRisingGrenadeDistance = risingGrenadeY1Max - risingGrenadeY1Min;

			float playerX1 = (player.getPosition().getX() - tileNoX) * tileLength;
			float playerX2 = playerX1 + tileLength;
			float playerY1 = (player.getPosition().getY() - tileNoY) * tileLength;

			float fallingLeftGrenadeX1 = playerX1 - (leftRightOffset);
			float fallingRightGrenadeX1 = playerX2 - animationWidth + (leftRightOffset);
			float fallingGrenadeY1Max = playerY1 + tileLength - animationHeight;
			float fallingGrenadeY1Min = sceneDrawingContext.y() - animationHeight;
			float maxVerticalfallingGrenadeDistance = fallingGrenadeY1Max - fallingGrenadeY1Min;

			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

            for (ArtilleryAttackActionPhase.Grenade grenade : grenades) {
                float relativeHeight = grenade.relativeHeight;
                float grenadeY1;
                float grenadeX1;
                Animation animation;

                if (grenade.risingNotFalling) {
                    grenadeY1 = risingGrenadeY1Max - (maxVerticalRisingGrenadeDistance * relativeHeight);
                    grenadeX1 = grenade.leftNotRight ? risingLeftGrenadeX1 : risingRightGrenadeX1;
                    animation = risingGrenade;
                } else {
                    grenadeY1 = fallingGrenadeY1Max - (maxVerticalfallingGrenadeDistance * relativeHeight);
                    grenadeX1 = grenade.leftNotRight ? fallingLeftGrenadeX1 : fallingRightGrenadeX1;
                    animation = fallingGrenade;
                }

                animation.draw(grenadeX1, grenadeY1, animation.getWidth(), animation.getHeight());

            }

			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);

		}

		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

}
