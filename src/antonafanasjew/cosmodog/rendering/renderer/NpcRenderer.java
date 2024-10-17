package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.topology.Position;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.ActorAppearanceType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.NpcActionType;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.EnemiesUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.RenderingUtils;
import antonafanasjew.cosmodog.util.TransitionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.EnemyAttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.EnemyDestructionFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;

import com.google.common.collect.Maps;

/**
 * Renders the NPC's on the map.
 */
public class NpcRenderer extends AbstractRenderer {
	
	//The following two maps are used to render enemies whose animations take more than one tile.
	//Note that offsets are relative to the tile size. -1.0 means -tileWidth, e.g -16, 2.0 means 2 * tileWidth, e.g. 32 
	private static final Map<UnitType, Float> ENEMY_TYPE_2_X_OFFSET = Maps.newHashMap();
	private static final Map<UnitType, Float> ENEMY_TYPE_2_Y_OFFSET = Maps.newHashMap();
	
	static {
		ENEMY_TYPE_2_Y_OFFSET.put(UnitType.GUARDIAN, -1.0f);
	}
	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
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
		
		Position tilePosition = Position.fromCoordinates(tileNoX, tileNoY);

		Set<Enemy> enemies = map.visibleEnemies(tilePosition, tilesW, tilesH, 2);
		
		for (Enemy enemy : enemies) {
			
			Position enemyPosition = enemy.getPosition();

			float pieceOffsetX = 0.0f;
			float pieceOffsetY = 0.0f;
						
			ActorTransition enemyTransition = cosmodogGame.getActorTransitionRegistry().get(enemy);
			
			
			FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();
			
			
			boolean enemyIsInHighGrass = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_PLANTS, map, enemy, enemyTransition);
			boolean enemyIsInSoftGroundType = RenderingUtils.isActorOnSoftGroundType(map, enemy, enemyTransition);
			
			boolean enemyIsMoving = enemyTransition != null;
			boolean enemyIsFighting = fightPhaseTransition != null && fightPhaseTransition.getEnemy().equals(enemy); //This just checks that the enemy in the loop is the one that fights and not an idle one.
			boolean enemyIsShooting = enemyIsFighting && fightPhaseTransition instanceof EnemyAttackingFightPhaseTransition;
			boolean enemyIsExploding = enemyIsFighting && fightPhaseTransition instanceof EnemyDestructionFightPhaseTransition;

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

			ActorAppearanceType enemyAppearanceType = ActorAppearanceType.DEFAULT;
			
			if (enemyIsInHighGrass) {
				enemyAppearanceType = ActorAppearanceType.INHIGHGRASS;
			} else if (enemyIsInSoftGroundType) {
				enemyAppearanceType = ActorAppearanceType.NOFEET;
			}
			
			
			DirectionType enemyDirection = enemy.getDirection();
			
			if (enemyActionType == NpcActionType.ANIMATE) {
				enemyDirection = enemyTransition.getTransitionalDirection();				
			}
			
			String animationId = Mappings.npcAnimationId(enemy.getUnitType(), enemyDirection, enemyActionType, enemyAppearanceType);
			Animation enemyAnimation = ApplicationContext.instance().getAnimations().get(animationId);
			
			
			if (enemyActionType == NpcActionType.ANIMATE) {
				enemyPosition = enemyTransition.getTransitionalPosition();
				pieceOffsetX = tileWidth * enemyTransition.getTransitionalOffsetX();
				pieceOffsetY = tileHeight * enemyTransition.getTransitionalOffsetY();
			} 
			
			if (enemyActionType == NpcActionType.SHOOTING) {
					
				float completion = fightPhaseTransition.getCompletion();

				float fightOffset = 0.0f;
				
				if (!enemy.getUnitType().isRangedUnit()) {
				
					if (completion > 0.5f) {
						completion = 1.0f - completion;
					}
					
					fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;

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
				float completion = fightPhaseTransition.getCompletion();
				int animationFrame = (int)(enemyAnimation.getFrameCount() * completion);
				enemyAnimation.setCurrentFrame(animationFrame);
			}
			
			
			boolean enemyDamaged = enemy.getActualLife() * 2 < enemy.getActualMaxLife();
			boolean enemyRobotic = enemy.getUnitType().isRobotic();
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			if (enemyAnimation == null) {
				throw new NullPointerException("Animation " + animationId + " does not exist.");
			}
			
			float animationSizeCorrectionOffsetX = 0.0f;
			float animationSizeCorrectionOffsetY = 0.0f;
			
			if (ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetX = ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) * tileWidth;
			}
			
			if (ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetY = ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) * tileHeight;
			}

			
			enemyAnimation.draw((enemyPosition.getX() - tileNoX) * tileWidth + pieceOffsetX + animationSizeCorrectionOffsetX, (enemyPosition.getY() - tileNoY) * tileHeight + pieceOffsetY + animationSizeCorrectionOffsetY);
			
			if (enemyDamaged && enemyRobotic) {
				int smokeOffsetX = 8;
				int smokeOffsetY = -8;
				Animation smokeAnimation = ApplicationContext.instance().getAnimations().get("smoke");
				smokeAnimation.draw((enemyPosition.getX() - tileNoX) * tileWidth + pieceOffsetX  + animationSizeCorrectionOffsetX + smokeOffsetX, (enemyPosition.getY() - tileNoY) * tileHeight + pieceOffsetY + animationSizeCorrectionOffsetY + smokeOffsetY);
			}
			
			if (enemyIsExploding && enemyRobotic) {
				Animation explosionAnimation = ApplicationContext.instance().getAnimations().get("explosion");
				float completion = fightPhaseTransition.getCompletion();
				int animationFrame = (int)(explosionAnimation.getFrameCount() * completion);
				explosionAnimation.setCurrentFrame(animationFrame);
				explosionAnimation.draw((enemyPosition.getX() - tileNoX) * tileWidth - tileWidth, (enemyPosition.getY() - tileNoY) * tileHeight -tileHeight);
			}
						
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
			
			
		}
		

		//This is another rendering loop to render the overhead markers over the enemies.
		for (Enemy enemy : enemies) {
			
			Position enemyPosition = enemy.getPosition();

			float pieceOffsetX = 0.0f;
			float pieceOffsetY = 0.0f;
						
			ActorTransition enemyTransition = cosmodogGame.getActorTransitionRegistry().get(enemy);
			
			
			FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();
			
			
			boolean enemyIsMoving = enemyTransition != null;
			boolean enemyIsFighting = fightPhaseTransition != null && fightPhaseTransition.getEnemy().equals(enemy); //This just checks that the enemy in the loop is the one that fights and not an idle one.
			boolean enemyIsShooting = enemyIsFighting && fightPhaseTransition instanceof EnemyAttackingFightPhaseTransition;
			boolean enemyIsExploding = enemyIsFighting && fightPhaseTransition instanceof EnemyDestructionFightPhaseTransition;

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
						
			if (enemyActionType == NpcActionType.ANIMATE) {
				enemyPosition = enemyTransition.getTransitionalPosition();
				pieceOffsetX = tileWidth * enemyTransition.getTransitionalOffsetX();
				pieceOffsetY = tileHeight * enemyTransition.getTransitionalOffsetY();
			} 
			
			if (enemyActionType == NpcActionType.SHOOTING) {
					
				float completion = fightPhaseTransition.getCompletion();

				float fightOffset = 0.0f;
				
				if (!enemy.getUnitType().isRangedUnit()) {
				
					if (completion > 0.5f) {
						completion = 1.0f - completion;
					}
					
					fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;

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
						
			
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			float animationSizeCorrectionOffsetX = 0.0f;
			float animationSizeCorrectionOffsetY = 0.0f;
			
			if (ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetX = ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) * tileWidth;
			}
			
			if (ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetY = ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) * tileHeight;
			}
		
			//Render enemy overhead markers
			if (!enemyIsExploding) {
				if (!EnemiesUtils.enemyActive(enemy)) {
					float signWidth = 32 / cam.getZoomFactor();
					float signHeight = 32 / cam.getZoomFactor();
					float signOffsetX = (tileWidth - signWidth) / 2;
					float signOffseetY = -8;
					Animation sleepingAnimation = ApplicationContext.instance().getAnimations().get("enemySleeping");
					sleepingAnimation.draw((enemyPosition.getX() - tileNoX) * tileWidth + pieceOffsetX + signOffsetX + animationSizeCorrectionOffsetX, (enemyPosition.getY() - tileNoY) * tileHeight + pieceOffsetY + signOffseetY + animationSizeCorrectionOffsetY, signWidth, signHeight);
				} else if (enemy.getAlertLevel() > 0 && !enemyIsExploding) {
					float signWidth = 8;
					float signHeight = 8;
					float signOffsetX = (tileWidth - signWidth) / 2;
					float signOffsetY = -signHeight;
					Animation alertedAnimation = ApplicationContext.instance().getAnimations().get("enemyAlerted");
					alertedAnimation.draw((enemyPosition.getX() - tileNoX) * tileWidth + pieceOffsetX + signOffsetX + animationSizeCorrectionOffsetX, (enemyPosition.getY() - tileNoY) * tileHeight + pieceOffsetY + signOffsetY + animationSizeCorrectionOffsetY, signWidth, signHeight);
				}
			}
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
			
			
		}	
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}
	
}
