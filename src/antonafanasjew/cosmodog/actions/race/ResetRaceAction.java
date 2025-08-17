package antonafanasjew.cosmodog.actions.race;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.death.RespawnAction;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public class ResetRaceAction extends PhaseBasedAction {

    private final Race race;

    public ResetRaceAction(Race race) {
        this.race = race;
    }

    @Override
    protected void onTriggerInternal() {
        if (race.isStarted() && !race.isSolved()) {
            Player player = ApplicationContextUtils.getPlayer();
            race.cancel();
            player.getInventory().remove(InventoryItemType.VEHICLE);
            ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.RESPAWNING, new RespawnAction(race.getRespawnPosition(), true, true));

        }
    }

    @Override
    public void onEnd() {
        MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);
    }
}
