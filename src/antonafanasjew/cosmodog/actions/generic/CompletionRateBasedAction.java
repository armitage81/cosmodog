package antonafanasjew.cosmodog.actions.generic;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.io.Serial;

public abstract class CompletionRateBasedAction extends FixedLengthAsyncAction {

    @Serial
    private static final long serialVersionUID = -1032910058049587274L;

    private CompletionRateBasedTransition transition;

    public CompletionRateBasedTransition getTransition() {
        return transition;
    }

    public CompletionRateBasedAction(int duration) {
        super(duration);
        transition = CompletionRateBasedTransition.instance();
    }

    @Override
    public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
        float completionRate = Math.min(((float)after) / getDuration(), 2);
        onUpdate(completionRate);
    }

    protected abstract void onUpdate(float completionRate);

    @Override
    public void afterUnregistration() {
        transition = null;
    }

}
