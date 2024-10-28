package antonafanasjew.cosmodog.rendering.renderer;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.ArtilleryAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.EnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.PlayerAttackActionPhase;
import antonafanasjew.cosmodog.actions.generic.FadingAction;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.actions.respawn.RespawnAction;
import antonafanasjew.cosmodog.actions.teleportation.TeleportationAction;
import antonafanasjew.cosmodog.util.*;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction;
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
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;

import java.util.Optional;

public class PlayerRenderer extends AbstractRenderer {

	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		int tileLength = TileUtils.tileLengthSupplier.get();

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		
		//Do not render the player if the worm is already eating him ;)
		WormAttackAction wormAttackAction = (WormAttackAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.WORM_ATTACK);
		if (wormAttackAction != null) {
			float completionRate = wormAttackAction.getCompletionRate();
			if (completionRate >= 0.5) {
				return;
			}
		}

		RespawnAction respawnAction = (RespawnAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.RESPAWNING);
		if (respawnAction != null) {
			Optional<AsyncAction> phase = respawnAction.getPhaseRegistry().currentPhase();

			if (phase.isEmpty() && !respawnAction.isShowPlayerWhenFadingOut()) {
				return;
			}

			if (phase.isPresent() && phase.get() instanceof FadingAction fadingPhase) {
				if (fadingPhase.isFadingInNotFadingOut() && !respawnAction.isShowPlayerWhenFadingIn()) {
					return;
				};
				if (!fadingPhase.isFadingInNotFadingOut() && !respawnAction.isShowPlayerWhenFadingOut()) {
					return;
				};
            }
		}
		
		
		Player player = cosmodogGame.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		
		Cam cam = cosmodogGame.getCam();
		
		int scaledTileLength = (int) (tileLength * cam.getZoomFactor());

		int camX = (int) cam.viewCopy().x();
		int camY = (int) cam.viewCopy().y();

		int x = -(int) ((camX % scaledTileLength));
		int y = -(int) ((camY % scaledTileLength));

		int tileNoX = camX / scaledTileLength;
		int tileNoY = camY / scaledTileLength;

		MovementAction movementAction = (MovementAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT);
		CrossTileMotion playerMotion = null;
		if (movementAction != null) {
			playerMotion = movementAction.getActorMotions().get(player);
		}

		Optional<AbstractFightActionPhase> optFightPhase = TransitionUtils.currentFightPhase();
		
		Object action = cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MINE_EXPLOSION);
		MineExplosionAction mineExplosionAction = null;
		if (action instanceof MineExplosionAction) {
			mineExplosionAction = (MineExplosionAction)action;
		}
		
		Object radiationDamageAction = cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.RADIATION_DAMAGE);
		Object shockDamageAction = cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.SHOCK_DAMAGE);

		MovementAttemptAction movementAttemptAction = (MovementAttemptAction) cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MOVEMENT_ATTEMPT);

		TeleportationAction.TeleportationState teleportationState = null;
		TeleportationAction teleportationAction = (TeleportationAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.TELEPORTATION);
		if (teleportationAction != null) {
			teleportationState = teleportationAction.getProperty("state");
		}

		boolean playerIsBeingTeleportedAndInvisible = teleportationState != null && teleportationState.beingTeleported && !teleportationState.characterVisible;
		boolean playerIsInVehicle = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean playerIsInPlatform = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM) != null;
		boolean playerIsOnBoat = hasBoat(player) && isWaterTile(map, player, playerMotion);
		
		boolean playerIsOnPlatform = PlayerMovementCache.getInstance().isPlayerOnPlatform();
		
		boolean playerIsInHighGrass = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_PLANTS, map, player, playerMotion);
		boolean playerIsInSnow = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_SNOW, map, player, playerMotion);
		boolean playerHasSki = player.getInventory().get(InventoryItemType.SKI) != null;
		boolean playerIsOnSki = playerIsInSnow && playerHasSki && !playerIsOnPlatform && !playerIsInPlatform;
		boolean playerIsInSoftGroundType = RenderingUtils.isActorOnSoftGroundType(map, player, playerMotion);
		boolean playerIsMoving = playerMotion != null;
		boolean playerIsFighting = optFightPhase.isPresent() && optFightPhase.get() instanceof PlayerAttackActionPhase;
		boolean playerIsAttemptingBlockedPassage = movementAttemptAction != null;
		boolean playerIsTakingDamage = false;
		
		if (shockDamageAction != null) {
			playerIsTakingDamage = true;
		} if (radiationDamageAction != null) {
			playerIsTakingDamage = true;
		} else if (mineExplosionAction != null) {
			playerIsTakingDamage = true;
		} else if (optFightPhase.isPresent()) {
			if (optFightPhase.get() instanceof EnemyAttackActionPhase) {
				if (optFightPhase.get() instanceof ArtilleryAttackActionPhase) {
					playerIsTakingDamage = ((ArtilleryAttackActionPhase)optFightPhase.get()).playerTakingDamage();
				} else {
					playerIsTakingDamage = true;
				}
			}
		}

		boolean playerIsHoldingUpItem = cosmodogGame.getCurrentlyFoundTool() != null;
		
		ActorAppearanceType playerAppearanceType;
		
		if (playerIsBeingTeleportedAndInvisible) {
			playerAppearanceType = ActorAppearanceType.ISTELEPORTING;
		} else if (playerIsOnBoat) {
			playerAppearanceType = ActorAppearanceType.ONBOAT;
		} else if (playerIsInPlatform) {
			playerAppearanceType = ActorAppearanceType.INPLATFORM;
		} else if (playerIsInVehicle) {
			playerAppearanceType = ActorAppearanceType.INVEHICLE;
		} else if (playerIsOnPlatform) {
			playerAppearanceType = ActorAppearanceType.DEFAULT;
		} else if (playerIsInHighGrass) {
			playerAppearanceType = ActorAppearanceType.INHIGHGRASS;
		} else if (playerIsOnSki) {
			playerAppearanceType = ActorAppearanceType.ONSKI;
		} else if (playerIsInSoftGroundType) {
			playerAppearanceType = ActorAppearanceType.NOFEET;
		} else {
			playerAppearanceType = ActorAppearanceType.DEFAULT;
		}
		
		PlayerActionType playerActionType;
		
		if (playerIsMoving) {
			playerActionType = PlayerActionType.ANIMATE;
		} else if (playerIsTakingDamage) {
			playerActionType = PlayerActionType.TAKINGDAMAGE;
		} else if (playerIsHoldingUpItem) {
			playerActionType = PlayerActionType.HOLDING_UP_ITEM;
		} else {
			playerActionType = PlayerActionType.INANIMATE;
		}
		
		String animationKey;
		

		animationKey = Mappings.playerAnimationId(playerAppearanceType, playerActionType, player.getDirection());
		
		Animation playerAnimation = applicationContext.getAnimations().get(animationKey);
		
		Animation playerWeaponAnimation = null;
		
		Arsenal arsenal = player.getArsenal();
		WeaponType weaponType = arsenal.getSelectedWeaponType();
		
		if (weaponType != WeaponType.FISTS) {
			String animationPrefix = Mappings.WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.get(weaponType);
			String weaponAnimationId = animationKey.replace("player", "player" + animationPrefix);
			weaponAnimationId = weaponAnimationId.replace("OnSki", "Default");
			playerWeaponAnimation = applicationContext.getAnimations().get(weaponAnimationId);
		}
		
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
		
		graphics.translate(x, y);
		graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());
		playerAnimation.draw((player.getPosition().getX() - tileNoX) * tileLength + pieceOffsetX, (player.getPosition().getY() - tileNoY) * tileLength + pieceOffsetY);
		if (playerWeaponAnimation != null) {
			playerWeaponAnimation.draw((player.getPosition().getX() - tileNoX) * tileLength + pieceOffsetX, (player.getPosition().getY() - tileNoY) * tileLength + pieceOffsetY);
		}
		
		if (playerIsHoldingUpItem) {
			CollectibleTool tool = cosmodogGame.getCurrentlyFoundTool();
			if (tool != null) {
				String animationId = Mappings.collectibleToolToAnimationId(tool);
				Animation foundToolAnimation = ApplicationContext.instance().getAnimations().get(animationId);
				if (foundToolAnimation != null) {
					foundToolAnimation.draw((player.getPosition().getX() - tileNoX) * tileLength + pieceOffsetX, (player.getPosition().getY() - tileNoY - 1) * tileLength + pieceOffsetY);
				}
			}
		}
		
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
		graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
		
	}

	private boolean hasBoat(Player player) {
		InventoryItem boat= player.getInventory().get(InventoryItemType.BOAT);
		return boat != null;
	}
	
	private boolean isWaterTile(CosmodogMap map, Player player, CrossTileMotion playerMotion) {
		//int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		//return tileId == Tiles.WATER_TILE_ID;
		
		boolean retVal = false;
		
		if (playerMotion == null) {
			int tileId = map.getTileId(player.getPosition(), Layers.LAYER_META_COLLISIONS);
			retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_WATER);
		} else {
			int startTileId = map.getTileId(playerMotion.getlastMidwayPosition(), Layers.LAYER_META_COLLISIONS);
			int targetTileId = map.getTileId(playerMotion.getTargetPosition(), Layers.LAYER_META_COLLISIONS);
			
			boolean startTileIdIsWaterTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, startTileId).equals(TileType.COLLISION_WATER);
			boolean targetTileIdIsWaterTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, targetTileId).equals(TileType.COLLISION_WATER);
			
			if (startTileIdIsWaterTile && targetTileIdIsWaterTile) {
				retVal = true;
			} else if (!startTileIdIsWaterTile && !targetTileIdIsWaterTile) {
				retVal = false;
			} else if (startTileIdIsWaterTile) {
				float transitionalOffset = playerMotion.getCrossTileOffsetX() + playerMotion.getCrossTileOffsetY();
				retVal = transitionalOffset > -0.25 && transitionalOffset < 0.25;
			} else {
				float transitionalOffset = playerMotion.getCrossTileOffsetX() + playerMotion.getCrossTileOffsetY();
				retVal = transitionalOffset > 0.5 || transitionalOffset < -0.5;
			}
		}
		
		return retVal;
		
	}
	


	
	
	
	
}
