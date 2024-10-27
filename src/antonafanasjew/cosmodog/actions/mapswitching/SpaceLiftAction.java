package antonafanasjew.cosmodog.actions.mapswitching;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.actions.teleportation.ActualTeleportationActionPhase;
import antonafanasjew.cosmodog.actions.teleportation.TeleportEndActionPhase;
import antonafanasjew.cosmodog.actions.teleportation.TeleportStartActionPhase;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class SpaceLiftAction extends PhaseBasedAction {



    @Override
    public boolean hasFinished() {
        return false;
    }
}
