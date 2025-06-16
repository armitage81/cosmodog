package antonafanasjew.cosmodog.actions.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.model.portals.Ray;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class FailPortalAction extends FixedLengthAsyncAction {
    public FailPortalAction(int duration) {
        super(duration);
    }

    @Override
    public void onTrigger() {
        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_PORTALS_FAILED).play();
    }
}
