package antonafanasjew.cosmodog.actions.race;

import antonafanasjew.cosmodog.MusicResources;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.MusicUtils;

public class CancelRaceAction extends PhaseBasedAction {

    private final Race race;

    public CancelRaceAction(Race race) {
        this.race = race;
    }

    @Override
    protected void onTriggerInternal() {
        if (race.isStarted() && !race.isSolved()) {
            race.cancel();
            ApplicationContextUtils.getPlayer().getInventory().remove(InventoryItemType.VEHICLE);
        }
    }

    @Override
    public void onEnd() {
        MusicUtils.loopMusic(MusicResources.MUSIC_SOUNDTRACK);
    }
}
