package antonafanasjew.cosmodog.rendering.renderer.player;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.camera.FadingAction;
import antonafanasjew.cosmodog.actions.death.RespawnAction;
import antonafanasjew.cosmodog.actions.death.WormAttackAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.MineExplosionAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.RadiationDamageAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.ShockDamageAction;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.ArtilleryAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.EnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.PlayerAttackActionPhase;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.actions.teleportation.TeleportationAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.ActorAppearanceType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.RenderingUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;

import java.util.List;
import java.util.Optional;

public class PlayerRendererUtils {
    public static boolean beingEatenByWorm() {

        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();

        WormAttackAction wormAttackAction = (WormAttackAction)game
                .getActionRegistry()
                .getRegisteredAction(AsyncActionType.WORM_ATTACK)
        ;

        if (wormAttackAction != null) {
            float completionRate = wormAttackAction.getCompletionRate();
            return completionRate >= 0.5;
        }

        return false;
    }

    public static boolean hiddenWhileRespawning() {

        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();

        RespawnAction respawnAction = (RespawnAction)game
                .getActionRegistry()
                .getRegisteredAction(AsyncActionType.RESPAWNING)
        ;

        if (respawnAction != null) {
            Optional<AsyncAction> phase = respawnAction.getPhaseRegistry().currentPhase();

            if (phase.isEmpty() && !respawnAction.isShowPlayerWhenFadingOut()) {
                return true;
            }

            if (phase.isPresent() && phase.get() instanceof FadingAction fadingPhase) {
                if (fadingPhase.isFadingInNotFadingOut() && !respawnAction.isShowPlayerWhenFadingIn()) {
                    return true;
                };
                if (!fadingPhase.isFadingInNotFadingOut() && !respawnAction.isShowPlayerWhenFadingOut()) {
                    return true;
                };
            }
        }

        return false;
    }

    public static CrossTileMotion playerMotion() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        Player player = game.getPlayer();

        MovementAction movementAction = (MovementAction)game
                .getActionRegistry()
                .getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class)
        ;

        CrossTileMotion playerMotion = null;
        if (movementAction != null) {
            playerMotion = movementAction.getActorMotions().get(player);
        }

        return playerMotion;
    }

    public static boolean beingInvisibleWhileTeleporting() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();

        Optional<TeleportationAction.TeleportationState> optTeleportationState = game
                .getActionRegistry()
                .attributeForCurrentActionOfGivenType(
                        AsyncActionType.TELEPORTATION,
                        "state"
                );

        return optTeleportationState.isPresent() && optTeleportationState.get().beingTeleported && !optTeleportationState.get().characterVisible;
    }

    public static boolean takingDamage() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        Optional<AbstractFightActionPhase> optFightPhase = game.getActionRegistry().currentFightPhase();
        Optional<ShockDamageAction> optShockDamageAction = game.getActionRegistry().currentActionOfGivenType(AsyncActionType.SHOCK_DAMAGE, ShockDamageAction.class);
        Optional<RadiationDamageAction> optRadiationDamageAction = game.getActionRegistry().currentActionOfGivenType(AsyncActionType.RADIATION_DAMAGE, RadiationDamageAction.class);
        Optional<MineExplosionAction> optMineExplosionAction = game.getActionRegistry().currentMineExplosionAction();

        boolean playerIsTakingDamage = false;

        if (optShockDamageAction.isPresent()) {
            playerIsTakingDamage = true;
        } if (optRadiationDamageAction.isPresent()) {
            playerIsTakingDamage = true;
        } else if (optMineExplosionAction.isPresent()) {
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
        return playerIsTakingDamage;
    }

    public static boolean fighting() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        Optional<AbstractFightActionPhase> optFightPhase = game.getActionRegistry().currentFightPhase();
        return optFightPhase.isPresent() && optFightPhase.get() instanceof PlayerAttackActionPhase;
    }

    public static Animation playerAnimation(ActorAppearanceType appearanceType, PlayerActionType actionType, DirectionType directionType) {
        String animationKey = Mappings.playerAnimationId(appearanceType, actionType, directionType);
        return ApplicationContext.instance().getAnimations().get(animationKey);
    }

    public static Animation weaponAnimation(ActorAppearanceType appearanceType, PlayerActionType actionType, DirectionType directionType) {
        String animationKey = Mappings.playerAnimationId(appearanceType, actionType, directionType);
        ApplicationContext applicationContext = ApplicationContext.instance();
        Player player = applicationContext.getCosmodog().getCosmodogGame().getPlayer();

        Animation playerWeaponAnimation = null;
        Arsenal arsenal = player.getArsenal();
        WeaponType weaponType = arsenal.getSelectedWeaponType();

        if (weaponType != WeaponType.FISTS) {
            String animationPrefix = Mappings.WEAPON_TYPE_2_PLAYER_WEAPON_ANIMATION_PREFIX.get(weaponType);
            String weaponAnimationId = animationKey.replace("player", "player" + animationPrefix);
            weaponAnimationId = weaponAnimationId.replace("OnSki", "Default");
            playerWeaponAnimation = applicationContext.getAnimations().get(weaponAnimationId);
        }

        return playerWeaponAnimation;
    }

    public static ActorAppearanceType appearanceType() {

        Player player = ApplicationContextUtils.getPlayer();
        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().mapOfPlayerLocation();
        CrossTileMotion playerMotion = PlayerRendererUtils.playerMotion();

        boolean playerIsBeingTeleportedAndInvisible = PlayerRendererUtils.beingInvisibleWhileTeleporting();
        boolean playerIsInVehicle = player.getInventory().hasVehicle();
        boolean playerIsOnBoat = player.getInventory().hasItem(InventoryItemType.BOAT) && isWaterTile(map, player, playerMotion);
        boolean playerIsInPlatform = player.getInventory().hasPlatform();
        boolean playerIsOnPlatform = PlayerMovementCache.getInstance().isPlayerOnPlatform();
        boolean playerIsInHighGrass = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_PLANTS, map, player, playerMotion);
        boolean playerIsInSnow = RenderingUtils.isActorOnGroundTypeTile(TileType.GROUND_TYPE_SNOW, map, player, playerMotion);
        boolean playerHasSki = player.getInventory().get(InventoryItemType.SKI) != null;
        boolean playerIsOnSki = playerIsInSnow && playerHasSki && !playerIsOnPlatform && !playerIsInPlatform;
        boolean playerIsInSoftGroundType = RenderingUtils.isActorOnSoftGroundType(map, player, playerMotion);

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
        return playerAppearanceType;
    }

    private static boolean isWaterTile(CosmodogMap map, Player player, CrossTileMotion playerMotion) {
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

    public static PlayerActionType actionType() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();

        CrossTileMotion playerMotion = PlayerRendererUtils.playerMotion();
        boolean playerIsMoving = playerMotion != null;
        boolean playerIsTakingDamage = PlayerRendererUtils.takingDamage();
        boolean playerIsHoldingUpItem = game.getCurrentlyFoundTool() != null;

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

        return playerActionType;
    }

    public static Vector offsetFromTile() {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        Cam cam = game.getCam();
        Player player = ApplicationContextUtils.getPlayer();


        int tileLength = TileUtils.tileLengthSupplier.get();
        CrossTileMotion playerMotion = PlayerRendererUtils.playerMotion();
        boolean playerIsMoving = playerMotion != null;
        boolean playerIsFighting = PlayerRendererUtils.fighting();

        Optional<MovementAttemptAction> optMovementAttemptAction = game
                .getActionRegistry()
                .currentActionOfGivenType(
                        AsyncActionType.MOVEMENT_ATTEMPT,
                        MovementAttemptAction.class
                )
                ;

        boolean playerIsAttemptingBlockedPassage = optMovementAttemptAction.isPresent();

        float pieceOffsetX = 0;
        float pieceOffsetY = 0;

        if (playerIsMoving) {
            pieceOffsetX = tileLength * playerMotion.getCrossTileOffsetX();
            pieceOffsetY = tileLength * playerMotion.getCrossTileOffsetY();
        }

        if (playerIsFighting) {
            Optional<AbstractFightActionPhase> optFightPhase = game.getActionRegistry().currentFightPhase();
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

            float completion = optMovementAttemptAction.get().getCompletionRate();

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

        return Vector.empty().add(pieceOffsetX, pieceOffsetY);
    }

    public static Optional<Portal> exitPortal() {

        Player player = ApplicationContextUtils.getPlayer();
        Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
        Cam.CamTilePosition camTilePosition = cam.camTilePosition();

        CrossTileMotion playerMotion = PlayerRendererUtils.playerMotion();

        boolean playerIsUsingPortal = playerMotion != null && playerMotion.isContainsTeleportation();

        Portal exitPortal = null;
        if (playerIsUsingPortal) {
            CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
            List<Portal> portals = game.getPortals();
            Portal firstPortal = portals.getFirst();
            boolean playerFacesFirstPortal = firstPortal.position.equals(DirectionType.facedAdjacentPosition(player.getPosition(), player.getDirection()));
            playerFacesFirstPortal &= DirectionType.reverse(player.getDirection()) == firstPortal.directionType;
            if (playerFacesFirstPortal) {
                exitPortal = portals.getLast();
            } else {
                exitPortal = portals.getFirst();
            }
        }
        return Optional.ofNullable(exitPortal);
    }
}
