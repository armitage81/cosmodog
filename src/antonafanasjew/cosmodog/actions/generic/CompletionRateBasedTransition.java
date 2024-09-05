package antonafanasjew.cosmodog.actions.generic;

public class CompletionRateBasedTransition {

    private float value;

    public CompletionRateBasedTransition() {
        this.value = 0;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public static CompletionRateBasedTransition instance() {
        return new CompletionRateBasedTransition();
    }
}
