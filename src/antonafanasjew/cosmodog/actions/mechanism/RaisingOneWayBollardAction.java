package antonafanasjew.cosmodog.actions.mechanism;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.AutoBollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Bollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.OneWayBollard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class RaisingOneWayBollardAction extends FixedLengthAsyncAction {

    public static final int DURATION = 100;

    private final OneWayBollard oneWayBollard;

    public RaisingOneWayBollardAction(int duration, OneWayBollard oneWayBollard) {
        super(duration);
        this.oneWayBollard = oneWayBollard;
    }

    @Override
    public void onTrigger() {
        ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_SECRET_DOOR_HYDRAULICS).play();
    }

    @Override
    public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
        float actionPercentage = (float)after / (float)getDuration();

        if (actionPercentage > 1.0f) {
            actionPercentage = 1.0f;
        }

        if (actionPercentage < 0.333) {
            oneWayBollard.setVisualState(Bollard.VISUAL_STATE_OPENING_PHASE3);
        }

        if (actionPercentage >= 0.333 && actionPercentage < 0.666) {
            oneWayBollard.setVisualState(Bollard.VISUAL_STATE_OPENING_PHASE2);
        }

        if (actionPercentage >= 0.666 && actionPercentage < 1) {
            oneWayBollard.setVisualState(Bollard.VISUAL_STATE_OPENING_PHASE1);
        }

        if (actionPercentage >= 1) {
            oneWayBollard.setVisualState(Bollard.VISUAL_STATE_CLOSED);
        }
    }

    @Override
    public void onEnd() {
        oneWayBollard.setVisualState(Bollard.VISUAL_STATE_CLOSED);
        oneWayBollard.setOpen(false);
    }

}
