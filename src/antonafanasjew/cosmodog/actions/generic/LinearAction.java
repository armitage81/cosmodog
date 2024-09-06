package antonafanasjew.cosmodog.actions.generic;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class LinearAction extends CompletionRateBasedAction {

    public LinearAction(int duration) {
        super(duration);
    }

    @Override
    public void onUpdate(float completionRate) {
        getTransition().setValue(completionRate);
    }
}
