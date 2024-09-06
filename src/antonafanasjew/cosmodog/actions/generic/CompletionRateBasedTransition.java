package antonafanasjew.cosmodog.actions.generic;


import java.util.HashMap;
import java.util.Map;

public class CompletionRateBasedTransition {

    private float value;
    private final Map<String, Object> properties = new HashMap<>();

    public CompletionRateBasedTransition() {
        this.value = 0;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public static CompletionRateBasedTransition instance() {
        return new CompletionRateBasedTransition();
    }
}
