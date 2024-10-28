package antonafanasjew.cosmodog.actions.generic;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class LadderAction extends FixedLengthAsyncAction {

    private final int steps;

    public LadderAction(int duration, int steps) {
        super(duration);
        this.steps = steps;
        this.getProperties().put("value", 0f);
    }

    public int getSteps() {
        return steps;
    }

    @Override
    public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {

        float stepSize = 1f / steps;
        float step = 0;

        float completionRate = getCompletionRate();

        while (completionRate > stepSize) {
            completionRate -= stepSize;
            step++;
        }

        float value = (float)step * stepSize;

        this.getProperties().put("value", (float)value);
    }
}
