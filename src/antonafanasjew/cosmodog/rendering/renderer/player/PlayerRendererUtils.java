package antonafanasjew.cosmodog.rendering.renderer.player;

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.camera.FadingAction;
import antonafanasjew.cosmodog.actions.death.RespawnAction;
import antonafanasjew.cosmodog.actions.death.WormAttackAction;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

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
                .getRegisteredAction(AsyncActionType.MOVEMENT)
        ;

        CrossTileMotion playerMotion = null;
        if (movementAction != null) {
            playerMotion = movementAction.getActorMotions().get(player);
        }

        return playerMotion;
    }
}
