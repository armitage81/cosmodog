package antonafanasjew.cosmodog.actions.mechanism;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Bollard;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Reflector;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class TurningReflectorClockwiseAction extends FixedLengthAsyncAction {

    private final Reflector reflector;

    public TurningReflectorClockwiseAction(int duration, Reflector reflector) {
        super(duration);
        this.reflector = reflector;
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

        if (actionPercentage < 0.2) {
            reflector.setVisualState(Reflector.VISUAL_STATE_PHASE1);
        } else if (actionPercentage < 0.4) {
            reflector.setVisualState(Reflector.VISUAL_STATE_PHASE2);
        } else if (actionPercentage < 0.6) {
            reflector.setVisualState(Reflector.VISUAL_STATE_PHASE3);
        } else if (actionPercentage < 0.8) {
            reflector.setVisualState(Reflector.VISUAL_STATE_PHASE4);
        } else {
            reflector.setVisualState(Reflector.VISUAL_STATE_PHASE5);
        }

    }

    @Override
    public void onEnd() {
        reflector.setVisualState(Reflector.VISUAL_STATE_IDLE);
        reflector.switchToNextState();
    }

}
