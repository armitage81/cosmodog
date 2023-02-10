package antonafanasjew.cosmodog.rendering.renderer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.cutscenes.MineExplosionAction;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction;
import antonafanasjew.cosmodog.actions.cutscenes.WormAttackAction.WormAttackTransition;
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
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.RenderingUtils;
import antonafanasjew.cosmodog.util.TransitionUtils;
import antonafanasjew.cosmodog.view.transitions.ActorTransition;
import antonafanasjew.cosmodog.view.transitions.EnemyAttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.FightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.MovementAttemptTransition;
import antonafanasjew.cosmodog.view.transitions.TeleportationTransition;
import antonafanasjew.cosmodog.view.transitions.impl.ArtilleryAttackingFightPhaseTransition;
import antonafanasjew.cosmodog.view.transitions.impl.PlayerAttackingFightPhaseTransition;

public class PlayerRenderer extends AbstractRenderer {

	
	@Override
	public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

		DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
		
		graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());
		
		ApplicationContext applicationContext = ApplicationContext.instance();
		Cosmodog cosmodog = applicationContext.getCosmodog();
		CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();

		
		//Do not render the player if the worm is already eating him ;)
		WormAttackAction wormAttackAction = (WormAttackAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.WORM_ATTACK);
		if (wormAttackAction != null) {
			WormAttackTransition wormAttackTransition = wormAttackAction.getTransition();
			if (wormAttackTransition != null) {
				float wormAttackTransitionPercentage = wormAttackTransition.percentage;
				if (wormAttackTransitionPercentage >= 0.5) {
					return;
				}
			}
		}
		
		
		Player player = cosmodogGame.getPlayer();
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

		ActorTransition playerTransition = cosmodogGame.getActorTransitionRegistry().get(player);
		
		FightPhaseTransition fightPhaseTransition = TransitionUtils.currentFightPhaseTransition();
		
		Object action = cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MINE_EXPLOSION);
		MineExplosionAction mineExplosionAction = null;
		if (action instanceof MineExplosionAction) {
			mineExplosionAction = (MineExplosionAction)action;
		}
		
		Object radiationDamageAction = cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.RADIATION_DAMAGE);
		Object shockDamageAction = cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.SHOCK_DAMAGE);
		
		MovementAttemptTransition movementAttemptTransition = cosmodogGame.getMovementAttemptTransition();
		TeleportationTransition teleportationTransition = cosmodogGame.getTeleportationTransition();
		
		boolean playerIsBeingTeleportedAndInvisible = teleportationTransition.isBeingTeleported && !teleportationTransition.characterVisible; 
		boolean playerIsInVehicle = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean playerIsInPlatform = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM) != null;
		boolean playerIsOnBoat = hasBoat(player) && isWaterTile(map, player, playerTransition);
		
		boolean playerIsOnPlatform = PlayerMovementCache.getInstance().isPlayerOnPlatform();
		
		boolean playerIsInHighGrass = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_PLANTS, map, player, playerTransition);
		boolean playerIsInSnow = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_SNOW, map, player, playerTransition);
		boolean playerHasSki = player.getInventory().get(InventoryItemType.SKI) != null;
		boolean playerIsOnSki = playerIsInSnow && playerHasSki && !playerIsOnPlatform && !playerIsInPlatform;
		boolean playerIsInSoftGroundType = RenderingUtils.isActorOnSoftGroundType(map, player, playerTransition);
		boolean playerIsMoving = playerTransition != null;
		boolean playerIsFighting = fightPhaseTransition != null && fightPhaseTransition instanceof PlayerAttackingFightPhaseTransition;
		boolean playerIsAttemptingBlockedPassage = movementAttemptTransition != null;
		boolean playerIsTakingDamage = false;
		
		if (shockDamageAction != null) {
			playerIsTakingDamage = true;
		} if (radiationDamageAction != null) {
			playerIsTakingDamage = true;
		} else if (mineExplosionAction != null) {
			playerIsTakingDamage = true;
		} else if (fightPhaseTransition != null) {
			if (fightPhaseTransition instanceof EnemyAttackingFightPhaseTransition) {
				if (fightPhaseTransition instanceof ArtilleryAttackingFightPhaseTransition) {
					playerIsTakingDamage = ((ArtilleryAttackingFightPhaseTransition)fightPhaseTransition).playerTakingDamage();
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
			pieceOffsetX = tileWidth * playerTransition.getTransitionalOffsetX();
			pieceOffsetY = tileHeight * playerTransition.getTransitionalOffsetY();
		}
		
		if (playerIsFighting) {
								
			float completion = fightPhaseTransition.getCompletion();

			float fightOffset = 0.0f;
				
			if (completion > 0.5f) {
				completion = 1.0f - completion;
			}
			
			fightOffset = (tileWidth * cam.getZoomFactor()) / 10.0f * completion;
			
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
				
			movementAttemptOffset = (tileWidth * cam.getZoomFactor()) / 16.0f * completion;
				
			
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
		playerAnimation.draw((player.getPositionX() - tileNoX) * tileWidth + pieceOffsetX, (player.getPositionY() - tileNoY) * tileHeight + pieceOffsetY);
		if (playerWeaponAnimation != null) {
			playerWeaponAnimation.draw((player.getPositionX() - tileNoX) * tileWidth + pieceOffsetX, (player.getPositionY() - tileNoY) * tileHeight + pieceOffsetY);
		}
		
		if (playerIsHoldingUpItem) {
			CollectibleTool tool = cosmodogGame.getCurrentlyFoundTool();
			if (tool != null) {
				String animationId = Mappings.collectibleToolToAnimationId(tool);
				Animation foundToolAnimation = ApplicationContext.instance().getAnimations().get(animationId);
				if (foundToolAnimation != null) {
					foundToolAnimation.draw((player.getPositionX() - tileNoX) * tileWidth + pieceOffsetX, (player.getPositionY() - tileNoY - 1) * tileHeight + pieceOffsetY);
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
	
	private boolean isWaterTile(CosmodogMap map, Player player, ActorTransition playerTransition) {
		//int tileId = map.getTileId(tileX, tileY, Layers.LAYER_META_COLLISIONS);
		//return tileId == Tiles.WATER_TILE_ID;
		
		boolean retVal = false;
		
		if (playerTransition == null) {
			int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_COLLISIONS);
			retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, tileId).equals(TileType.COLLISION_WATER);
		} else {
			int startTileId = map.getTileId(playerTransition.getTransitionalPosX(), playerTransition.getTransitionalPosY(), Layers.LAYER_META_COLLISIONS);
			int targetTileId = map.getTileId(playerTransition.getTargetPosX(), playerTransition.getTargetPosY(), Layers.LAYER_META_COLLISIONS);
			
			boolean startTileIdIsWaterTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, startTileId).equals(TileType.COLLISION_WATER);
			boolean targetTileIdIsWaterTile = TileType.getByLayerAndTileId(Layers.LAYER_META_COLLISIONS, targetTileId).equals(TileType.COLLISION_WATER);
			
			if (startTileIdIsWaterTile && targetTileIdIsWaterTile) {
				retVal = true;
			} else if (!startTileIdIsWaterTile && !targetTileIdIsWaterTile) {
				retVal = false;
			} else if (startTileIdIsWaterTile && !targetTileIdIsWaterTile) {
				float transitionalOffset = playerTransition.getTransitionalOffsetX() + playerTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > -0.25 && transitionalOffset < 0.25;
			} else if (!startTileIdIsWaterTile && targetTileIdIsWaterTile) {
				float transitionalOffset = playerTransition.getTransitionalOffsetX() + playerTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > 0.5 || transitionalOffset < -0.5;
			}
		}
		
		return retVal;
		
	}
	


	
	
	
	
}
