package antonafanasjew.cosmodog.rendering.renderer.player;

import antonafanasjew.cosmodog.actions.fight.*;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.actions.teleportation.TeleportationAction;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
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
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

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
		Animation playerAnimation = PlayerRendererUtils.playerAnimation(playerAppearanceType, playerActionType, player.getDirection());
		Animation playerWeaponAnimation = PlayerRendererUtils.weaponAnimation(playerAppearanceType, playerActionType, player.getDirection());
		
		Vector offsetFromTile = PlayerRendererUtils.offsetFromTile();
		
		graphics.translate(camTilePosition.offsetX(), camTilePosition.offsetY());
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

		Optional<Portal> optExitPortal = PlayerRendererUtils.exitPortal();
		if (optExitPortal.isPresent()) {



			Vector exitCoordinatesRelatedToCam = Cam
					.positionVectorRelatedToCamTilePosition(
							DirectionType.facedAdjacentPosition(optExitPortal.get().position, optExitPortal.get().directionType),
							camTilePosition
					)
			;

			CrossTileMotion playerMotion = PlayerRendererUtils.playerMotion();
			int index = (int)Math.abs((playerMotion.getCrossTileOffsetX() + playerMotion.getCrossTileOffsetY()) * playerAnimation.getFrameCount());

			float verticalOffsetForEntering = 0;

			Animation enteringPortalAnimation;
			if (playerMotion.getCrossTileOffsetX() > 0) {
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalRight");
			} else if (playerMotion.getCrossTileOffsetX() < 0) {
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalLeft");
			} else if (playerMotion.getCrossTileOffsetY() > 0) {
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalDown");
			} else {
				verticalOffsetForEntering = -Math.abs((playerMotion.getCrossTileOffsetY()) * tileLength);
				enteringPortalAnimation = ApplicationContext.instance().getAnimations().get("playerEnteringPortalUp");
			}
			enteringPortalAnimation.getImage(index).draw(playerCoordinatesRelatedToCam.getX(), playerCoordinatesRelatedToCam.getY() + verticalOffsetForEntering);

			float verticalOffsetForExiting = 0;

			Animation exitingPortalAnimation;
			if (optExitPortal.get().directionType == DirectionType.RIGHT) {
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalRight");
			} else if (optExitPortal.get().directionType == DirectionType.LEFT) {
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalLeft");
			} else if (optExitPortal.get().directionType == DirectionType.DOWN) {
				float ratio = Math.abs(playerMotion.getCrossTileOffsetX()) + Math.abs(playerMotion.getCrossTileOffsetY());
				verticalOffsetForExiting = ratio * tileLength - tileLength;
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalDown");
			} else {
				exitingPortalAnimation = ApplicationContext.instance().getAnimations().get("playerExitingPortalUp");
			}
			exitingPortalAnimation.getImage(index).draw(exitCoordinatesRelatedToCam.getX(), exitCoordinatesRelatedToCam.getY() + verticalOffsetForExiting);

		} else {
			playerAnimation.draw(playerCoordinatesRelatedToCam.getX() + offsetFromTile.getX(), playerCoordinatesRelatedToCam.getY() + offsetFromTile.getY());
		}

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
