package antonafanasjew.cosmodog.actions.letterplateriddle;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.actions.generic.FadingAction;
import antonafanasjew.cosmodog.actions.notification.OverheadNotificationAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class WrongSequenceAction extends PhaseBasedAction {

    public static final int FADING_OUT_DURATION = 3000;
    public static final int FADING_IN_DURATION = 1000;

    @Override
    public void onTrigger() {

        Player player = ApplicationContextUtils.getPlayer();
        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_HIT).play();
        OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> Something is wrong.");
        OverheadNotificationAction.registerOverheadNotification(player, "<font:critical> I must start anew.");

        FadingAction fadingOutAction = new FadingAction(FADING_OUT_DURATION, false);

        FixedLengthAsyncAction respawnAction = new FixedLengthAsyncAction(1) {
            @Override
            public void onEnd() {
                Player player = ApplicationContextUtils.getPlayer();
                player.setPositionX(218);
                player.setPositionY(191);
                ApplicationContextUtils.getCosmodogGame().getCam().focusOnPiece(ApplicationContextUtils.getCosmodogMap(), 0, 0, player);
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
