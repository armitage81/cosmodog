package antonafanasjew.cosmodog.actions.functional;

import antonafanasjew.cosmodog.actions.ExponentialAction;
import antonafanasjew.cosmodog.actions.ParabolicAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExponentialActionTest {

    ExponentialAction exponentialAction;

    @BeforeEach
    public void setUp() {
        exponentialAction = new ExponentialAction(1000);
        exponentialAction.onTrigger();
    }

    @Test
    public void testBeginning() {
        exponentialAction.onUpdate(0, 0, null, null);
        assertEquals(0, (float)exponentialAction.getProperty("value"));
    }

    @Test
    public void testEnd() {
        exponentialAction.onUpdate(0, 1000, null, null);
        assertEquals(1, (float)exponentialAction.getProperty("value"));
    }
}