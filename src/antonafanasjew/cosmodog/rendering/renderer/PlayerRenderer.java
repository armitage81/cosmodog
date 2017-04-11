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
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.PlayerAppearanceType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ArsenalInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.inventory.PlatformInventoryItem;
import antonafanasjew.cosmodog.model.inventory.VehicleInventoryItem;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.Mappings;
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
	protected void renderFromZero(GameContainer gameContainer, Graphics graphics, DrawingContext drawingContext, Object renderingParameter) {

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
		
		MineExplosionAction mineExplosionAction = (MineExplosionAction)cosmodogGame.getActionRegistry().getRegisteredAction(AsyncActionType.MINE_EXPLOSION);
		
		
		MovementAttemptTransition movementAttemptTransition = cosmodogGame.getMovementAttemptTransition();
		TeleportationTransition teleportationTransition = cosmodogGame.getTeleportationTransition();
		
		boolean playerIsBeingTeleportedAndInvisible = teleportationTransition.isBeingTeleported && !teleportationTransition.characterVisible; 
		boolean playerIsInVehicle = (VehicleInventoryItem)player.getInventory().get(InventoryItemType.VEHICLE) != null;
		boolean playerIsInPlatform = (PlatformInventoryItem)player.getInventory().get(InventoryItemType.PLATFORM) != null;
		boolean playerIsOnBoat = hasBoat(player) && isWaterTile(map, player, playerTransition);
		
		boolean playerIsOnPlatform = PlayerMovementCache.getInstance().isPlayerOnPlatform();
		
		boolean playerIsInHighGrass = isPlayerOnGroundTypeTile(TileType.GROUND_TYPE_PLANTS, map, player, playerTransition);
		boolean playerIsInSnow = isPlayerOnGroundTypeTile(TileType.GROUND_TYPE_SNOW, map, player, playerTransition);
		boolean playerHasSki = player.getInventory().get(InventoryItemType.SKI) != null;
		boolean playerIsOnSki = playerIsInSnow && playerHasSki;
		boolean playerIsInSoftGroundType = isPlayerOnSoftGroundType(map, player, playerTransition);
		boolean playerIsMoving = playerTransition != null;
		boolean playerIsFighting = fightPhaseTransition != null && fightPhaseTransition instanceof PlayerAttackingFightPhaseTransition;
		boolean playerIsAttemptingBlockedPassage = movementAttemptTransition != null;
		boolean playerIsTakingDamage = false;
		if (mineExplosionAction != null) {
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

		
		PlayerAppearanceType playerAppearanceType;
		
		if (playerIsBeingTeleportedAndInvisible) {
			playerAppearanceType = PlayerAppearanceType.ISTELEPORTING;
		} else if (playerIsOnBoat) {
			playerAppearanceType = PlayerAppearanceType.ONBOAT;
		} else if (playerIsInPlatform) {
			playerAppearanceType = PlayerAppearanceType.INPLATFORM;
		} else if (playerIsInVehicle) {
			playerAppearanceType = PlayerAppearanceType.INVEHICLE;
		} else if (playerIsOnPlatform) {
			playerAppearanceType = PlayerAppearanceType.DEFAULT;
		} else if (playerIsInHighGrass) {
			playerAppearanceType = PlayerAppearanceType.INHIGHGRASS;
		} else if (playerIsOnSki) {
			playerAppearanceType = PlayerAppearanceType.ONSKI;
		} else if (playerIsInSoftGroundType) {
			playerAppearanceType = PlayerAppearanceType.NOFEET;
		} else {
			playerAppearanceType = PlayerAppearanceType.DEFAULT;
		}
		
		PlayerActionType playerActionType;
		
		if (playerIsMoving) {
			playerActionType = PlayerActionType.ANIMATE;
		} else if (playerIsTakingDamage) {
			playerActionType = PlayerActionType.TAKINGDAMAGE;
		} else {
			playerActionType = PlayerActionType.INANIMATE;
		}
		
		String animationKey;
		

		animationKey = Mappings.playerAnimationId(playerAppearanceType, playerActionType, player.getDirection());
		
		Animation playerAnimation = applicationContext.getAnimations().get(animationKey);
		
		Animation playerWeaponAnimation = null;
		
		ArsenalInventoryItem arsenal = (ArsenalInventoryItem)player.getInventory().get(InventoryItemType.ARSENAL);
		WeaponType weaponType = arsenal.getSelectedWeaponType();
		
		if (weaponType != WeaponType.FISTS) {
			String animationPrefix = Mappings.WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.get(weaponType);
			String weaponAnimationId = animationKey.replace("player", "player" + animationPrefix);
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
		graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
		graphics.translate(-x, -y);
		
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
	
	private boolean isPlayerOnGroundTypeTile(TileType tileType, CosmodogMap map, Player player, ActorTransition playerTransition) {
		boolean retVal = false;
		if (playerTransition == null) {
			int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
			retVal = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, tileId).equals(tileType);
		} else {
			int startTileId = map.getTileId(playerTransition.getTransitionalPosX(), playerTransition.getTransitionalPosY(), Layers.LAYER_META_GROUNDTYPES);
			int targetTileId = map.getTileId(playerTransition.getTargetPosX(), playerTransition.getTargetPosY(), Layers.LAYER_META_GROUNDTYPES);
			
			boolean startTileIdIsHighGrassTile = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, startTileId).equals(tileType);
			boolean targetTileIdIsHighGrassTile = TileType.getByLayerAndTileId(Layers.LAYER_META_GROUNDTYPES, targetTileId).equals(tileType);
			
			
			if (startTileIdIsHighGrassTile && targetTileIdIsHighGrassTile) {
				retVal = true;
			} else if (!startTileIdIsHighGrassTile && !targetTileIdIsHighGrassTile) {
				retVal = false;
			} else if (startTileIdIsHighGrassTile && !targetTileIdIsHighGrassTile) {
				float transitionalOffset = playerTransition.getTransitionalOffsetX() + playerTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > -0.25 && transitionalOffset < 0.25;
			} else if (!startTileIdIsHighGrassTile && targetTileIdIsHighGrassTile) {
				float transitionalOffset = playerTransition.getTransitionalOffsetX() + playerTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > 0.5 || transitionalOffset < -0.5;
			}
		}
		
		return retVal;
	}

	private boolean tileIdRepresentsSoftGroundType(int tileId) {
		return
		TileType.GROUND_TYPE_SNOW.getTileId() == tileId
		||
		TileType.GROUND_TYPE_GRASS.getTileId() == tileId
		||
		TileType.GROUND_TYPE_SAND.getTileId() == tileId
		||
		TileType.GROUND_TYPE_SWAMP.getTileId() == tileId;
	}
	
	private boolean isPlayerOnSoftGroundType(CosmodogMap map, Player player, ActorTransition playerTransition) {
		boolean retVal = false;
		if (playerTransition == null) {
			int tileId = map.getTileId(player.getPositionX(), player.getPositionY(), Layers.LAYER_META_GROUNDTYPES);
			retVal = tileIdRepresentsSoftGroundType(tileId);
			
		} else {
			
			int startTileId = map.getTileId(playerTransition.getTransitionalPosX(), playerTransition.getTransitionalPosY(), Layers.LAYER_META_GROUNDTYPES);
			boolean startTileSoft = tileIdRepresentsSoftGroundType(startTileId);
			
			int targetTileId = map.getTileId(playerTransition.getTargetPosX(), playerTransition.getTargetPosY(), Layers.LAYER_META_GROUNDTYPES);
			boolean targetTileSoft = tileIdRepresentsSoftGroundType(targetTileId);
			
			
			if (startTileSoft && targetTileSoft) {
				retVal = true;
			} else if (!startTileSoft && !targetTileSoft) {
				retVal = false;
			} else if (startTileSoft && !targetTileSoft) {
				float transitionalOffset = playerTransition.getTransitionalOffsetX() + playerTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > -0.25 && transitionalOffset < 0.25;
			} else if (!startTileSoft && targetTileSoft) {
				float transitionalOffset = playerTransition.getTransitionalOffsetX() + playerTransition.getTransitionalOffsetY();
				retVal = transitionalOffset > 0.5 || transitionalOffset < -0.5;
			}
		}
		
		return retVal;
	}
	
}
