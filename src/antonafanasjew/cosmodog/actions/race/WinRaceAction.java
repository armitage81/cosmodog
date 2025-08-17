package antonafanasjew.cosmodog.actions.race;

import antonafanasjew.cosmodog.GameProgress;
import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.actions.popup.PlayJingleAction;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.actions.popup.WaitAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

import java.util.HashMap;
import java.util.Map;

public class WinRaceAction extends PhaseBasedAction {

    private final Race race;

    /**
     * In case you wonder, why the race puzzle is not set to 'solved' here: This already happened in the FinishLine class.
     */
    public WinRaceAction(Race race) {
        this.race = race;
    }

    @Override
    protected void onTriggerInternal() {
        getPhaseRegistry().registerPhase("jingle", new PlayJingleAction(5000, MusicResources.MUSIC_WON));
        Player player = ApplicationContextUtils.getPlayer();
        player.getInventory().remove(InventoryItemType.VEHICLE);
    }

    @Override
    public void onEnd() {
        ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction("Congratulations! You won the race and mastered the car."));
        ApplicationContextUtils.getCosmodogGame().getInterfaceActionRegistry().registerAction(AsyncActionType.MODAL_WINDOW, new PopUpNotificationAction("From now on, food and water will be refilled when entering a car and won't deplete while in it."));

        String rewardPropertyName = race.getRewardPropertyName();
        if (rewardPropertyName != null) {
            ApplicationContextUtils.getPlayer().getGameProgress().getProgressProperties().put(rewardPropertyName, "true");
        }

    }
}
