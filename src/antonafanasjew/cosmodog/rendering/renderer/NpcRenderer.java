package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import antonafanasjew.cosmodog.SpriteSheets;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.EnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.EnemyDestructionActionPhase;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.geometry.Oscillations;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.sight.VisibilityCalculatorForPlayer;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.*;
import org.newdawn.slick.*;

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
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;

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
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		Cam cam = cosmodogGame.getCam();

		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		
		Position tilePosition = Position.fromCoordinates(camTilePosition.tileX(), camTilePosition.tileY(), map.getMapDescriptor());

		Set<Enemy> enemies = map.visibleEnemies(tilePosition, camTilePosition.widthInTiles(), camTilePosition.heightInTiles(), 2);

		Optional<AbstractFightActionPhase> optFightPhase = cosmodogGame.getActionRegistry().currentFightPhase();

		MovementAction movementAction = (MovementAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class);

		Player player = ApplicationContextUtils.getPlayer();

		for (Enemy enemy : enemies) {

			Position enemyPosition = enemy.getPosition();

			boolean visible = VisibilityCalculatorForPlayer.instance().visible(player, map, cosmodogGame.getPlanetaryCalendar(), enemyPosition);

			float pieceOffsetX = 0.0f;
			float pieceOffsetY = 0.0f;
						
			CrossTileMotion enemyMotion = null;

			if (movementAction != null) {
				enemyMotion = movementAction.getActorMotions().get(enemy);
			}

			boolean enemyIsInHighGrass = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_PLANTS, map, enemy, enemyMotion);
			boolean enemyIsInSoftGroundType = RenderingUtils.isActorOnSoftGroundType(map, enemy, enemyMotion);
			
			boolean enemyIsMoving = enemyMotion != null;
			boolean enemyIsFighting = optFightPhase.isPresent() && ((Enemy)optFightPhase.get().getProperties().get("enemy")).equals(enemy); //This just checks that the enemy in the loop is the one that fights and not an idle one.
			boolean enemyIsShooting = enemyIsFighting && optFightPhase.get() instanceof EnemyAttackActionPhase;
			boolean enemyIsExploding = enemyIsFighting && optFightPhase.get() instanceof EnemyDestructionActionPhase;

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
				enemyDirection = enemyMotion.getMotionDirection();
			}
			
			String animationId = Mappings.npcAnimationId(enemy.getUnitType(), enemyDirection, enemyActionType, enemyAppearanceType);
			Animation enemyAnimation = ApplicationContext.instance().getAnimations().get(animationId);
			
			
			if (enemyActionType == NpcActionType.ANIMATE) {
				enemyPosition = enemyMotion.getlastMidwayPosition();
				pieceOffsetX = tileLength * enemyMotion.getCrossTileOffsetX();
				pieceOffsetY = tileLength * enemyMotion.getCrossTileOffsetY();
			} 
			
			if (enemyActionType == NpcActionType.SHOOTING) {
					
				float completion = optFightPhase.get().getCompletionRate();

				float fightOffset = 0.0f;
				
				if (!enemy.getUnitType().isRangedUnit()) {
				
					if (completion > 0.5f) {
						completion = 1.0f - completion;
					}
					
					fightOffset = (tileLength * cam.getZoomFactor()) / 10.0f * completion;

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
				float completion = optFightPhase.get().getCompletionRate();
				int animationFrame = (int)(enemyAnimation.getFrameCount() * completion);
				enemyAnimation.setCurrentFrame(animationFrame);
			}
			
			
			boolean enemyDamaged = enemy.getActualLife() * 2 < enemy.getActualMaxLife();
			boolean enemyRobotic = enemy.getUnitType().isRobotic();
			graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			if (enemyAnimation == null) {
				throw new NullPointerException("Animation " + animationId + " does not exist.");
			}
			
			float animationSizeCorrectionOffsetX = 0.0f;
			float animationSizeCorrectionOffsetY = 0.0f;
			
			if (ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetX = ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) * tileLength;
			}
			
			if (ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetY = ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) * tileLength;
			}

			Vector npcPositionVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(enemyPosition, camTilePosition);

			if (visible) {
				enemyAnimation.draw(npcPositionVectorRelatedToCam.getX() + pieceOffsetX + animationSizeCorrectionOffsetX, npcPositionVectorRelatedToCam.getY() + pieceOffsetY + animationSizeCorrectionOffsetY);

				if (enemyDamaged && enemyRobotic) {
					int smokeOffsetX = 8;
					int smokeOffsetY = -8;
					Animation smokeAnimation = ApplicationContext.instance().getAnimations().get("smoke");
					smokeAnimation.draw(npcPositionVectorRelatedToCam.getX() + pieceOffsetX + animationSizeCorrectionOffsetX + smokeOffsetX, npcPositionVectorRelatedToCam.getY() + pieceOffsetY + animationSizeCorrectionOffsetY + smokeOffsetY);
				}

				if (enemyIsExploding && enemyRobotic) {
					Animation explosionAnimation = ApplicationContext.instance().getAnimations().get("explosion");
					float completion = optFightPhase.get().getCompletionRate();
					int animationFrame = (int) (explosionAnimation.getFrameCount() * completion);
					explosionAnimation.setCurrentFrame(animationFrame);
					explosionAnimation.draw(npcPositionVectorRelatedToCam.getX() - tileLength, npcPositionVectorRelatedToCam.getY() - tileLength);
				}
			}
			else if (player.getInventory().hasItem(InventoryItemType.MOTION_TRACKER)) {

				long timestamp = System.currentTimeMillis();
				float opacity = Oscillations.oscillation(timestamp, 0.5f, 1f, 500, 0);

				Image image = ApplicationContext.instance().getSpriteSheets().get(SpriteSheets.SPRITESHEET_SYMBOLS).getSprite(5, 0);
				image.draw(npcPositionVectorRelatedToCam.getX() + pieceOffsetX + animationSizeCorrectionOffsetX, npcPositionVectorRelatedToCam.getY() + pieceOffsetY + animationSizeCorrectionOffsetY, new Color(1, 1, 1, opacity));
			}

			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());

		}
		

		//This is another rendering loop to render the overhead markers over the enemies.
		for (Enemy enemy : enemies) {

			Position enemyPosition = enemy.getPosition();

			if (!VisibilityCalculatorForPlayer.instance().visible(player, map, cosmodogGame.getPlanetaryCalendar(), enemy.getPosition())) {
				continue;
			};

			float pieceOffsetX = 0.0f;
			float pieceOffsetY = 0.0f;

			CrossTileMotion enemyMotion = null;

			if (movementAction != null) {
				enemyMotion = movementAction.getActorMotions().get(enemy);
			}

			boolean enemyIsMoving = enemyMotion != null;
			boolean enemyIsFighting = optFightPhase.isPresent() && ((Enemy)optFightPhase.get().getProperties().get("enemy")).equals(enemy); //This just checks that the enemy in the loop is the one that fights and not an idle one.
			boolean enemyIsShooting = enemyIsFighting && optFightPhase.get() instanceof EnemyAttackActionPhase;
			boolean enemyIsExploding = enemyIsFighting && optFightPhase.get() instanceof EnemyDestructionActionPhase;

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
				enemyPosition = enemyMotion.getlastMidwayPosition();
				pieceOffsetX = tileLength * enemyMotion.getCrossTileOffsetX();
				pieceOffsetY = tileLength * enemyMotion.getCrossTileOffsetY();
			} 
			
			if (enemyActionType == NpcActionType.SHOOTING) {
					
				float completion = optFightPhase.get().getCompletionRate();

				float fightOffset = 0.0f;
				
				if (!enemy.getUnitType().isRangedUnit()) {
				
					if (completion > 0.5f) {
						completion = 1.0f - completion;
					}
					
					fightOffset = (tileLength * cam.getZoomFactor()) / 10.0f * completion;

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
						
			
			graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
			
			float animationSizeCorrectionOffsetX = 0.0f;
			float animationSizeCorrectionOffsetY = 0.0f;
			
			if (ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetX = ENEMY_TYPE_2_X_OFFSET.get(enemy.getUnitType()) * tileLength;
			}
			
			if (ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) != null) {
				animationSizeCorrectionOffsetY = ENEMY_TYPE_2_Y_OFFSET.get(enemy.getUnitType()) * tileLength;
			}

			Vector npcPositionVectorRelatedToCam = Cam.positionVectorRelatedToCamTilePosition(enemyPosition, camTilePosition);

			//Render enemy overhead markers
			if (!enemyIsExploding) {
				if (!EnemiesUtils.enemyActive(enemy)) {
					float signWidth = 32 / cam.getZoomFactor();
					float signHeight = 32 / cam.getZoomFactor();
					float signOffsetX = (tileLength - signWidth) / 2;
					float signOffseetY = -8;
					Animation sleepingAnimation = ApplicationContext.instance().getAnimations().get("enemySleeping");
					sleepingAnimation.draw(npcPositionVectorRelatedToCam.getX() + pieceOffsetX + signOffsetX + animationSizeCorrectionOffsetX, npcPositionVectorRelatedToCam.getY() + pieceOffsetY + signOffseetY + animationSizeCorrectionOffsetY, signWidth, signHeight);
				} else if (enemy.getAlertLevel() > 0) {
					float signWidth = 8;
					float signHeight = 8;
					float signOffsetX = (tileLength - signWidth) / 2;
					float signOffsetY = -signHeight;
					Animation alertedAnimation = ApplicationContext.instance().getAnimations().get("enemyAlerted");
					alertedAnimation.draw(npcPositionVectorRelatedToCam.getX() + pieceOffsetX + signOffsetX + animationSizeCorrectionOffsetX, npcPositionVectorRelatedToCam.getY() + pieceOffsetY + signOffsetY + animationSizeCorrectionOffsetY, signWidth, signHeight);
				}
			}
			
			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
			
			
		}	
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}
	
}
