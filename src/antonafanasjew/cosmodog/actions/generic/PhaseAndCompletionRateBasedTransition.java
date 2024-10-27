package antonafanasjew.cosmodog.actions.generic;


import java.util.HashMap;
import java.util.Map;

public class PhaseAndCompletionRateBasedTransition extends CompletionRateBasedTransition{

    private int phase = 0;

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void jumpToNextPhase() {
        setPhase(getPhase() + 1);
        setValue(0.0f);
    }

    public static PhaseAndCompletionRateBasedTransition instance() {
        return new PhaseAndCompletionRateBasedTransition();
    }
}
