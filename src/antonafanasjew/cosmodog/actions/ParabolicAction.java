package antonafanasjew.cosmodog.actions;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class ParabolicAction extends FixedLengthAsyncAction {

    public ParabolicAction(int duration) {
        super(duration);
        this.getProperties().put("value", 0f);
    }

    @Override
    public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
        float y = 1 - (float) (Math.pow((float) getCompletionRate() * 2 - 1, 2));
        this.getProperties().put("value", (float)y);
    }
}
