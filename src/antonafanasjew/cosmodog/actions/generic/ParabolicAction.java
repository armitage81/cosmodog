package antonafanasjew.cosmodog.actions.generic;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class ParabolicAction extends FixedLengthAsyncAction {

    public ParabolicAction(int duration) {
        super(duration);
        this.getProperties().put("value", 0f);
    }

    /**
     * Updates the transition based on the completion rate in a parabolic manner.
     * <p>
     * That means that the value of the transition raises from 0 to 1 in the first half of the
     * duration while its incline sinks and then falls back to 0 in the second half with raising incline.
     * <p>
     * A typical example for this kind of transition is the movement of an object thrown in the air vertically.
     * <p>
     * Take note: The range of possible values for the transition is 0 to 1.
     * The actual distances must be calculated in the renderer.
     * <p>
     * For instance: If a worm lurches from the snow and must be rendered at a height of 32 pixels,
     * the renderer must calculate the actual height based on the value of the transition and this height.
     * height = 32 * transition.getValue();
     * Same is valid for inversion, stretching etc.
     *
     */
    @Override
    public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
        float y = 1 - (float) (Math.pow((float) getCompletionRate() * 2 - 1, 2));
        this.getProperties().put("value", (float)y);
    }
}
