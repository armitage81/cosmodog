package antonafanasjew.cosmodog.actions.generic;

public class FadingAction extends CompletionRateBasedAction {

    private boolean fadingInNotFadingOut;

    public FadingAction(int duration, boolean fadingInNotFadingOut) {
        super(duration);
        this.fadingInNotFadingOut = fadingInNotFadingOut;
    }

    public boolean isFadingInNotFadingOut() {
        return fadingInNotFadingOut;
    }

    @Override
    public void onUpdate(float completionRate) {

        double value = completionRate;
        value -= 0.5;
        value *= -10;
        value = Math.pow(Math.E, value);
        value += 1;
        value = 1 / value;

        getTransition().setValue((float)value);
    }
}
