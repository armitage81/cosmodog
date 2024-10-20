package antonafanasjew.cosmodog.actions.respawn;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.actions.generic.FadingAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class RespawnAction extends PhaseBasedAction {

    public static final int FADING_OUT_DURATION = 3000;
    public static final int FADING_IN_DURATION = 1000;

    private final Position respawnPosition;

    private final boolean showPlayerWhenFadingOut;
    private final boolean showPlayerWhenFadingIn;

    public RespawnAction(Position respawnPosition, boolean showPlayerWhenFadingOut, boolean showPlayerWhenFadingIn) {
        this.respawnPosition = respawnPosition;
        this.showPlayerWhenFadingOut = showPlayerWhenFadingOut;
        this.showPlayerWhenFadingIn = showPlayerWhenFadingIn;
    }

    public boolean isShowPlayerWhenFadingIn() {
        return showPlayerWhenFadingIn;
    }

    public boolean isShowPlayerWhenFadingOut() {
        return showPlayerWhenFadingOut;
    }

    @Override
    public void onTrigger() {

        Player player = ApplicationContextUtils.getPlayer();

        FadingAction fadingOutAction = new FadingAction(FADING_OUT_DURATION, false);

        FixedLengthAsyncAction respawnAction = new FixedLengthAsyncAction(1) {

            @Override
            public void onTrigger() {
                player.beginRespawn();
            }

            @Override
            public void onEnd() {
                Player player = ApplicationContextUtils.getPlayer();
                player.setPosition(respawnPosition);
                player.endRespawn();
                ApplicationContextUtils.getCosmodogGame().getCam().focusOnPiece(0, 0, player);
            }
        };

        FadingAction fadingInAction = new FadingAction(FADING_IN_DURATION, true);

        getActionPhaseRegistry().registerAction(AsyncActionType.CUTSCENE, fadingOutAction);
        getActionPhaseRegistry().registerAction(AsyncActionType.CUTSCENE, respawnAction);
        getActionPhaseRegistry().registerAction(AsyncActionType.CUTSCENE, fadingInAction);
    }

    @Override
    public boolean hasFinished() {
        return !getActionPhaseRegistry().isActionRegistered(AsyncActionType.CUTSCENE);
    }

}
