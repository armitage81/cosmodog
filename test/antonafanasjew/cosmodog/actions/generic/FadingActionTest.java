package antonafanasjew.cosmodog.actions.generic;

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
        action.onUpdate(0);
        assertEquals(0, action.getTransition().getValue(), 0.01f);
    }

    @Test
    public void testWithinFirstHalf() {
        action.onUpdate(0.1f);
        assertEquals(0.02, action.getTransition().getValue(), 0.01f);
        action.onUpdate(0.2f);
        assertEquals(0.05, action.getTransition().getValue(), 0.01f);
        action.onUpdate(0.3f);
        assertEquals(0.12, action.getTransition().getValue(), 0.01f);
        action.onUpdate(0.4f);
        assertEquals(0.26, action.getTransition().getValue(), 0.01f);
    }

    @Test
    public void testHalf() {
        action.onUpdate(0.5f);
        assertEquals(0.5, action.getTransition().getValue(), 0.01f);
    }

    @Test
    public void testWithinSecondHalf() {
        action.onUpdate(0.6f);
        assertEquals(0.73, action.getTransition().getValue(), 0.01f);
        action.onUpdate(0.7f);
        assertEquals(0.88, action.getTransition().getValue(), 0.01f);
        action.onUpdate(0.8f);
        assertEquals(0.95, action.getTransition().getValue(), 0.01f);
        action.onUpdate(0.9f);
        assertEquals(0.98, action.getTransition().getValue(), 0.01f);

    }

    @Test
    public void testEnd() {
        action.onUpdate(1f);
        assertEquals(1, action.getTransition().getValue(), 0.01f);
    }
}