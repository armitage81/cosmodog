package antonafanasjew.cosmodog.actions.generic;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class FadingAction extends FixedLengthAsyncAction {

    private final boolean fadingInNotFadingOut;

    public FadingAction(int duration, boolean fadingInNotFadingOut) {
        super(duration);
        this.fadingInNotFadingOut = fadingInNotFadingOut;
        this.getProperties().put("value", 0f);
    }

    public boolean isFadingInNotFadingOut() {
        return fadingInNotFadingOut;
    }

    @Override
    public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {

        double value = getCompletionRate();
        value -= 0.5;
        value *= -10;
        value = Math.pow(Math.E, value);
        value += 1;
        value = 1 / value;

        this.getProperties().put("value", (float)value);

    }
}
