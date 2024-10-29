package antonafanasjew.cosmodog.actions.functional;

import antonafanasjew.cosmodog.actions.camera.FadingAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FadingActionTest {

    public FadingAction action;

    @BeforeEach
    public void setUp() {
        action = new FadingAction(1000, true);
        action.onTrigger();
    }

    @Test
    public void testStart() {
        action.onUpdate(0, 0, null, null);
        assertEquals(0, (float)action.getProperty("value"), 0.01f);
    }

    @Test
    public void testWithinFirstHalf() {
        action.onUpdate(0, 100, null, null);
        assertEquals(0.02, (float)action.getProperty("value"), 0.01f);
        action.onUpdate(0, 200, null, null);
        assertEquals(0.05, (float)action.getProperty("value"), 0.01f);
        action.onUpdate(0, 300, null, null);
        assertEquals(0.12, (float)action.getProperty("value"), 0.01f);
        action.onUpdate(0, 400, null, null);
        assertEquals(0.26, (float)action.getProperty("value"), 0.01f);
    }

    @Test
    public void testHalf() {
        action.onUpdate(0, 500, null, null);
        assertEquals(0.5, (float)action.getProperty("value"), 0.01f);
    }

    @Test
    public void testWithinSecondHalf() {
        action.onUpdate(0, 600, null, null);
        assertEquals(0.73, (float)action.getProperty("value"), 0.01f);
        action.onUpdate(0, 700, null, null);
        assertEquals(0.88, (float)action.getProperty("value"), 0.01f);
        action.onUpdate(0, 800, null, null);
        assertEquals(0.95, (float)action.getProperty("value"), 0.01f);
        action.onUpdate(0, 900, null, null);
        assertEquals(0.98,(float)action.getProperty("value"), 0.01f);

    }

    @Test
    public void testEnd() {
        action.onUpdate(0, 1000, null, null);
        assertEquals(1, action.getProperty("value"), 0.01f);
    }
}