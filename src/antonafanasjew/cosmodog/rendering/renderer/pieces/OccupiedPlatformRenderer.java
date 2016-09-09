package antonafanasjew.cosmodog.rendering.renderer.pieces;

import java.util.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.MovementAttemptTransition;
import antonafanasjew.cosmodog.view.transitions.TeleportationTransition;

import com.google.common.collect.Maps;

public class OccupiedPlatformRenderer extends AbstractRenderer {

	private Map<DirectionType, String> platformDirection2animationKey = Maps.newHashMap();
	
	public OccupiedPlatformRenderer() {
		platformDirection2animationKey.put(DirectionType.RIGHT, "platformRight");
		platformDirection2animationKey.put(DirectionType.DOWN, "platformDown");
		platformDirection2animationKey.put(DirectionType.LEFT, "platformLeft");
		platformDirection2animationKey.put(DirectionType.UP, "platformUp");
	}
	
	@Override
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {
		
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		CustomTiledMap tiledMap = ApplicationContextUtils.getCustomTiledMap();
		
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
		
		Player player = ApplicationContextUtils.getPlayer();
		
		if (player.getInventory().hasPlatform()) {

			CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
			
			ActorTransition playerTransition = cosmodogGame.getActorTransitionRegistry().get(player);
			FightPhaseTransition fightPhaseTransition = cosmodogGame.getFightPhaseTransition();
			MovementAttemptTransition movementAttemptTransition = cosmodogGame.getMovementAttemptTransition();
			
			boolean playerIsMoving = playerTransition != null;
			boolean playerIsFighting = fightPhaseTransition != null && fightPhaseTransition.enemyDestruction == false;
			boolean playerIsAttemptingBlockedPassage = movementAttemptTransition != null;
			
			PlatformInventoryItem item = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM);
			Platform platform = item.getPlatform();
			DirectionType direction = platform.getDirection();
			String animationKey = platformDirection2animationKey.get(direction);
			Animation platformAnimation = ApplicationContext.instance().getAnimations().get(animationKey);
			
			
			float pieceOffsetX = 0;
			float pieceOffsetY = 0;
			

			
			if (playerIsMoving) {
				pieceOffsetX = tileWidth * playerTransition.getTransitionalOffsetX();
				pieceOffsetY = tileHeight * playerTransition.getTransitionalOffsetY();
			}
			
			if (playerIsFighting) {
									
				float completion = fightPhaseTransition.completion;

				float fightOffset = 0.0f;
				
				if (fightPhaseTransition.playerAttack) {
					
					if (completion > 0.5f) {
						completion = 1.0f - completion;
					}
					
					fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;
					
				} 
				
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
				
				float completion = movementAttemptTransition.completion;

				float movementAttemptOffset = 0.0f;
				
					
				if (completion > 0.5f) {
					completion = 1.0f - completion;
				}
					
				movementAttemptOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;
					
				
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
			
			graphics.translate(x, y);
			graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

			platformAnimation.draw((player.getPositionX() - tileNoX - 3) * tileWidth + pieceOffsetX, (player.getPositionY() - tileNoY - 3) * tileHeight + pieceOffsetY);			

			graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
			graphics.translate(-x, -y);
			
		}
	}

}
