package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.NpcActionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.TransitionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

/**
 * Renders the NPC's on the map.
 */
public class NpcRenderer extends AbstractRenderer {
	
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

		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
		
		
		for (Enemy enemy : map.visibleEnemies(tileNoX, tileNoY, tilesW, tilesH, 2)) {
			
			int enemyPosX = enemy.getPositionX();
			int enemyPosY = enemy.getPositionY();
			
			float pieceOffsetX = 0.0f;
			float pieceOffsetY = 0.0f;
						
			ActorTransition enemyTransition = cosmodogGame.getActorTransitionRegistry().get(enemy);
			
			
			FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();
			
			
			boolean enemyIsMoving = enemyTransition != null;
			boolean enemyIsFighting = fightPhaseTransition != null && fightPhaseTransition.enemy.equals(enemy);
			boolean enemyIsShooting = enemyIsFighting && !fightPhaseTransition.enemyDestruction && fightPhaseTransition.playerAttack == false;
			boolean enemyIsExploding = enemyIsFighting && fightPhaseTransition.enemyDestruction;

			NpcActionType enemyActionType;
			
			if (enemyIsMoving) {
				enemyActionType = NpcActionType.ANIMATE;
			} else if (enemyIsShooting) {
				enemyActionType = NpcActionType.SHOOTING;
			} else if (enemyIsExploding) {
				enemyActionType = NpcActionType.EXPLODING;	
			} else {
				enemyActionType = NpcActionType.INANIMATE;
			}
			
			DirectionType enemyDirection = enemy.getDirection();
			
			if (enemyActionType == NpcActionType.ANIMATE) {
				enemyDirection = enemyTransition.getTransitionalDirection();				
			}
			
			String animationId = Mappings.npcAnimationId(enemy.getUnitType(), enemyDirection, enemyActionType);
			Animation enemyAnimation = ApplicationContext.instance().getAnimations().get(animationId);
			
			if (enemyActionType == NpcActionType.ANIMATE) {
				enemyPosX = enemyTransition.getTransitionalPosX();
				enemyPosY = enemyTransition.getTransitionalPosY();
				pieceOffsetX = tileWidth * enemyTransition.getTransitionalOffsetX();
				pieceOffsetY = tileHeight * enemyTransition.getTransitionalOffsetY();
			} 
			
			if (enemyActionType == NpcActionType.SHOOTING) {
					
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
					pieceOffsetY = fightOffset;
				}
				
				if (enemy.getDirection() == DirectionType.UP) {
					pieceOffsetY = -fightOffset;
				}
				
				if (enemy.getDirection() == DirectionType.RIGHT) {
					pieceOffsetX = fightOffset;
				}
				
				if (enemy.getDirection() == DirectionType.LEFT) {
					pieceOffsetX = -fightOffset;
				}
						
			}
			
			if (enemyActionType == NpcActionType.EXPLODING) {
				float completion = fightPhaseTransition.completion;
				int animationFrame = (int)(enemyAnimation.getFrameCount() * completion);
				enemyAnimation.setCurrentFrame(animationFrame);
			}
			
			
			boolean enemyDamaged = enemy.getActualLife() * 2 < enemy.getActualMaxLife();
			boolean enemyRobotic = enemy.getUnitType().isRobotic();
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			enemyAnimation.draw((enemyPosX - tileNoX) * tileWidth + pieceOffsetX, (enemyPosY - tileNoY) * tileHeight + pieceOffsetY);
			
			if (enemyDamaged && enemyRobotic) {
				int smokeOffsetX = 8;
				int smokeOffseetY = -8;
				Animation smokeAnimation = ApplicationContext.instance().getAnimations().get("smoke");
				smokeAnimation.draw((enemyPosX - tileNoX) * tileWidth + pieceOffsetX + smokeOffsetX, (enemyPosY - tileNoY) * tileHeight + pieceOffsetY + smokeOffseetY);
			}
			
			if (enemyIsExploding && enemyRobotic) {
				Animation explosionAnimation = ApplicationContext.instance().getAnimations().get("explosion");
				float completion = fightPhaseTransition.completion;
				int animationFrame = (int)(explosionAnimation.getFrameCount() * completion);
				explosionAnimation.setCurrentFrame(animationFrame);
				explosionAnimation.draw((enemyPosX - tileNoX) * tileWidth - tileWidth, (enemyPosY - tileNoY) * tileHeight -tileHeight);
			}
			
			if (enemy.getAlertLevel() > 0 && !enemyIsExploding) {
				float signWidth = 32 / cam.getZoomFactor();
				float signHeight = 32 / cam.getZoomFactor();
				float signOffsetX = (tileWidth - signWidth) / 2;
				float signOffseetY = -8;
				
				
				
				Animation alertedAnimation = ApplicationContext.instance().getAnimations().get("enemyAlerted");
				alertedAnimation.draw((enemyPosX - tileNoX) * tileWidth + pieceOffsetX + signOffsetX, (enemyPosY - tileNoY) * tileHeight + pieceOffsetY + signOffseetY, signWidth, signHeight);
			}
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
			
			
		}
		

				
		

		
	}
	
}
