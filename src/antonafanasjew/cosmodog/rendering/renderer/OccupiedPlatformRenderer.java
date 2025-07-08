package antonafanasjew.cosmodog.rendering.renderer;

import java.util.Map;
import java.util.Optional;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.PlayerAttackActionPhase;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;

import com.google.common.collect.Maps;

public class OccupiedPlatformRenderer extends AbstractRenderer {

	private final Map<DirectionType, String> platformDirection2animationKey = Maps.newHashMap();
	
	public OccupiedPlatformRenderer() {
		platformDirection2animationKey.put(DirectionType.RIGHT, "platformRight");
		platformDirection2animationKey.put(DirectionType.DOWN, "platformDown");
		platformDirection2animationKey.put(DirectionType.LEFT, "platformLeft");
		platformDirection2animationKey.put(DirectionType.UP, "platformUp");
	}
	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();

		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		Cam.CamTilePosition camTilePosition = cam.camTilePosition();
		Vector playerVectorRelativeToCamPosition = Cam.positionVectorRelatedToCamTilePosition(player.getPosition(), camTilePosition);
		
		if (player.getInventory().hasPlatform()) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();

			MovementAction movementAction = (MovementAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class);

			CrossTileMotion playerMotion = null;

			if (movementAction != null) {
				playerMotion = movementAction.getActorMotions().get(player);
			}

			Optional<AbstractFightActionPhase> optFightPhase = cosmodogGame.getActionRegistry().currentFightPhase();
			
			MovementAttemptAction movementAttemptAction = (MovementAttemptAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT_ATTEMPT);
			
			boolean playerIsMoving = playerMotion != null;
			boolean playerIsFighting = optFightPhase.isPresent() && optFightPhase.get() instanceof PlayerAttackActionPhase;
			boolean playerIsAttemptingBlockedPassage = movementAttemptAction != null;
			
			PlatformInventoryItem item = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);
			Platform platform = item.getPlatform();
			DirectionType direction = platform.getDirection();
			String animationKey = platformDirection2animationKey.get(direction);
			Animation platformAnimation = ApplicationContext.instance().getAnimations().get(animationKey);
			
			
			float pieceOffsetX = 0;
			float pieceOffsetY = 0;
			

			
			if (playerIsMoving) {
				pieceOffsetX = tileLength * playerMotion.getCrossTileOffsetX();
				pieceOffsetY = tileLength * playerMotion.getCrossTileOffsetY();
			}
			
			if (playerIsFighting) {
									
				float completion = optFightPhase.get().getCompletionRate();

				float fightOffset = 0.0f;
				
				if (completion > 0.5f) {
					completion = 1.0f - completion;
				}
				
				fightOffset = (tileLength * cam.getZoomFactor()) / 10.0f * completion;
					
				
				if (player.getDirection() == DirectionType.DOWN) {
					pieceOffsetY = fightOffset;
				}
				
				if (player.getDirection() == DirectionType.UP) {
					pieceOffsetY = -fightOffset;
				}
				
				if (player.getDirection() == DirectionType.RIGHT) {
					pieceOffsetX = fightOffset;
				}
				
				if (player.getDirection() == DirectionType.LEFT) {
					pieceOffsetX = -fightOffset;
				}
					
			}
			
			if (playerIsAttemptingBlockedPassage) {
				
				float completion = movementAttemptAction.getCompletionRate();

				float movementAttemptOffset = 0.0f;
				
					
				if (completion > 0.5f) {
					completion = 1.0f - completion;
				}
					
				movementAttemptOffset = (tileLength * cam.getZoomFactor()) / 16.0f * completion;
					
				
				if (player.getDirection() == DirectionType.DOWN) {
					pieceOffsetY = movementAttemptOffset;
				}
				
				if (player.getDirection() == DirectionType.UP) {
					pieceOffsetY = -movementAttemptOffset;
				}
				
				if (player.getDirection() == DirectionType.RIGHT) {
					pieceOffsetX = movementAttemptOffset;
				}
				
				if (player.getDirection() == DirectionType.LEFT) {
					pieceOffsetX = -movementAttemptOffset;
				}
			}
			
			graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

			platformAnimation.draw(playerVectorRelativeToCamPosition.getX() - 3 * tileLength + pieceOffsetX,  playerVectorRelativeToCamPosition.getY() - 3 * tileLength + pieceOffsetY);

			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
			
		}
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
	}

}
