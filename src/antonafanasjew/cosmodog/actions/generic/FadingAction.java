package antonafanasjew.cosmodog.actions.generic;

public class FadingAction extends CompletionRateBasedAction {

    public FadingAction(int duration, String name) {
        super(duration, name);
    }

    @Override
    public void onUpdate(float completionRate) {

        double e = Math.E;
        double minusTenX = -10 * completionRate;
        double ePowMinusTenX = Math.pow(e, minusTenX);
        double onePlusRes = 1 - ePowMinusTenX;
        double oneDivRes = 1 / onePlusRes;

        getTransition().setValue((float)oneDivRes);
    }
}
