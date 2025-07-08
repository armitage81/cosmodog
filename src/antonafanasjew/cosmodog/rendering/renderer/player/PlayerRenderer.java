package antonafanasjew.cosmodog.rendering.renderer.player;

import antonafanasjew.cosmodog.actions.fight.*;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.actions.teleportation.TeleportationAction;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.rendering.renderer.renderingutils.ActorRendererUtils;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.*;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.environmentaldamage.MineExplosionAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.ActorAppearanceType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class PlayerRenderer extends AbstractRenderer {

	
	@Override
	public void renderInternally(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
		Player player = cosmodogGame.getPlayer();
		Cam cam = cosmodogGame.getCam();


		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder
				.get()
				.getDrawingContextProvider()
				.sceneDrawingContext()
		;
		
		graphics.translate(
				sceneDrawingContext.x(),
				sceneDrawingContext.y())
		;
		
		if (PlayerRendererUtils.beingEatenByWorm()) {
			return;
		}

		if (PlayerRendererUtils.hiddenWhileRespawning()) {
			return;
		}
		
		Cam.CamTilePosition camTilePosition = cam.camTilePosition();

		//Example: Player on pos 12/13, cam on pos 10/10, tileLength is 16px => result = 32px/48px
		Vector playerCoordinatesRelatedToCam = Cam
				.positionVectorRelatedToCamTilePosition(
						player.getPosition(),
						camTilePosition
				)
		;


		boolean playerIsHoldingUpItem = cosmodogGame.getCurrentlyFoundTool() != null;


		ActorAppearanceType playerAppearanceType = PlayerRendererUtils.appearanceType();
		PlayerActionType playerActionType = PlayerRendererUtils.actionType();

		Vector offsetFromTile = PlayerRendererUtils.offsetFromTile();
		
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

		CrossTileMotion motion = ActorRendererUtils.actorMotion(player);
		boolean usingPortal = motion != null && motion.isContainsTeleportation();

		if (usingPortal) {

			Vector exitCoordinatesRelatedToCam = Cam
					.positionVectorRelatedToCamTilePosition(
							DirectionType.facedAdjacentPosition(motion.getExitPortal().position, motion.getExitPortal().directionType),
							camTilePosition
					)
			;

			float verticalOffsetForEntering = 0;

			Animation enteringPortalAnimation;
			if (motion.getEntrancePortal().directionType == DirectionType.LEFT) {
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalRight");
			} else if (motion.getEntrancePortal().directionType == DirectionType.RIGHT) {
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalLeft");
			} else if (motion.getEntrancePortal().directionType == DirectionType.UP) {
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalDown");
			} else {
				float ratio = Math.abs(motion.getCrossTileOffsetX()) + Math.abs(motion.getCrossTileOffsetY());
				verticalOffsetForEntering = -(ratio * tileLength);
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalUp");
			}
			int indexEnteringAnimation = (int)Math.abs((motion.getCrossTileOffsetX() + motion.getCrossTileOffsetY()) * enteringPortalAnimation.getFrameCount());
			enteringPortalAnimation.getImage(indexEnteringAnimation).draw(playerCoordinatesRelatedToCam.getX(), playerCoordinatesRelatedToCam.getY() + verticalOffsetForEntering);

			float verticalOffsetForExiting = 0;

			Animation exitingPortalAnimation;
			if (motion.getExitPortal().directionType == DirectionType.RIGHT) {
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalRight");
			} else if (motion.getExitPortal().directionType == DirectionType.LEFT) {
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalLeft");
			} else if (motion.getExitPortal().directionType == DirectionType.DOWN) {
				float ratio = Math.abs(motion.getCrossTileOffsetX()) + Math.abs(motion.getCrossTileOffsetY());
				verticalOffsetForExiting = ratio * tileLength - tileLength;
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalDown");
			} else {
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalUp");
			}
			int indexExitingAnimation = (int)Math.abs((motion.getCrossTileOffsetX() + motion.getCrossTileOffsetY()) * exitingPortalAnimation.getFrameCount());
			exitingPortalAnimation.getImage(indexExitingAnimation).draw(exitCoordinatesRelatedToCam.getX(), exitCoordinatesRelatedToCam.getY() + verticalOffsetForExiting);

		} else {
			Animation playerAnimation = PlayerRendererUtils.playerAnimation(playerAppearanceType, playerActionType, player.getDirection());
			playerAnimation.draw(playerCoordinatesRelatedToCam.getX() + offsetFromTile.getX(), playerCoordinatesRelatedToCam.getY() + offsetFromTile.getY());
		}

		Animation playerWeaponAnimation = PlayerRendererUtils.weaponAnimation(playerAppearanceType, playerActionType, player.getDirection());
		if (playerWeaponAnimation != null) {
			playerWeaponAnimation.draw(playerCoordinatesRelatedToCam.getX() + offsetFromTile.getX(), playerCoordinatesRelatedToCam.getY() + offsetFromTile.getY());
		}
		
		if (playerIsHoldingUpItem) {
			CollectibleTool tool = cosmodogGame.getCurrentlyFoundTool();
			if (tool != null) {
				String animationId = Mappings.collectibleToolToAnimationId(tool);
				Animation foundToolAnimation = ApplicationContext.instance().getAnimations().get(animationId);
				if (foundToolAnimation != null) {
					foundToolAnimation.draw(playerCoordinatesRelatedToCam.getX() + offsetFromTile.getX(), playerCoordinatesRelatedToCam.getY() - tileLength + offsetFromTile.getY());
				}
			}
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-camTilePosition.offsetX(), -camTilePosition.offsetY());
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

}
