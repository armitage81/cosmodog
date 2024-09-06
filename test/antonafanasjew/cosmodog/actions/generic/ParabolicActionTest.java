package antonafanasjew.cosmodog.actions.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParabolicActionTest {

    ParabolicAction parabolicAction;

    @BeforeEach
    public void setUp() {
        parabolicAction = new ParabolicAction(1000);
        parabolicAction.onTrigger();
    }

    @Test
    public void testBeginning() {
        parabolicAction.onUpdate(0);
        assertEquals(0, parabolicAction.getTransition().getValue());
    }

    @Test
    public void testShortlyAfterBeginning() {
        parabolicAction.onUpdate(0.1f);
        float value = parabolicAction.getTransition().getValue();
        assertTrue(value < 0.4f && value > 0.3f);
    }

    @Test
    public void testShortlyBeforePeak() {
        parabolicAction.onUpdate(0.4f);
        float value = parabolicAction.getTransition().getValue();
        assertTrue(value < 1.0f && value > 0.95f);
    }

    @Test
    public void testPeak() {
        parabolicAction.onUpdate(0.5f);
        assertEquals(1, parabolicAction.getTransition().getValue());
    }

    @Test
    public void testEnd() {
        parabolicAction.onUpdate(1.0f);
        assertEquals(0, parabolicAction.getTransition().getValue());
    }
}