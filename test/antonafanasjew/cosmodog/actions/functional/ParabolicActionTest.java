package antonafanasjew.cosmodog.actions.functional;

import antonafanasjew.cosmodog.actions.ParabolicAction;
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
        parabolicAction.onUpdate(0, 0, null, null);
        assertEquals(0, (float)parabolicAction.getProperty("value"));
    }

    @Test
    public void testShortlyAfterBeginning() {
        parabolicAction.onUpdate(0, 100, null, null);
        float value = parabolicAction.getProperty("value");
        assertTrue(value < 0.4f && value > 0.3f);
    }

    @Test
    public void testShortlyBeforePeak() {
        parabolicAction.onUpdate(0, 400, null, null);
        float value = parabolicAction.getProperty("value");
        assertTrue(value < 1.0f && value > 0.95f);
    }

    @Test
    public void testPeak() {
        parabolicAction.onUpdate(0, 500, null, null);
        assertEquals(1, (float)parabolicAction.getProperty("value"));
    }

    @Test
    public void testEnd() {
        parabolicAction.onUpdate(0, 1000, null, null);
        assertEquals(0, (float)parabolicAction.getProperty("value"));
    }
}