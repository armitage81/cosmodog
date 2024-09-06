package antonafanasjew.cosmodog.actions.generic;

public class LadderAction extends CompletionRateBasedAction {

    private final int steps;

    public LadderAction(int duration, int steps) {
        super(duration);
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }

    @Override
    protected void onUpdate(float completionRate) {

        float stepSize = 1f / steps;
        float step = 0;

        while (completionRate > stepSize) {
            completionRate -= stepSize;
            step++;
        }

        float value = (float)step * stepSize;

        getTransition().setValue(value);
    }
}
