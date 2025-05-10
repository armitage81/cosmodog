package antonafanasjew.cosmodog.actions.mechanism;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.OneWayBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Reflector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class SwitchingOneWayBollardAction extends FixedLengthAsyncAction {

    public static final int DURATION = 100;

    private final OneWayBollard oneWayBollard;

    public SwitchingOneWayBollardAction(int duration, OneWayBollard oneWayBollard) {
        super(duration);
        this.oneWayBollard = oneWayBollard;
    }

    @Override
    public void onTrigger() {
        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS).play();
    }

    @Override
    public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {

    }

    @Override
    public void onEnd() {
        oneWayBollard.switchToNextState();
    }

}
