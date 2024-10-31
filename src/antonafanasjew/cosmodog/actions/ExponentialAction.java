package antonafanasjew.cosmodog.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class ExponentialAction extends FixedLengthAsyncAction {

    public ExponentialAction(int duration) {
        super(duration);
        this.getProperties().put("value", 0f);
    }

    @Override
    public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
        float y = (float) (Math.pow((float) getCompletionRate(), Math.E));
        this.getProperties().put("value", (float)y);
    }
}
