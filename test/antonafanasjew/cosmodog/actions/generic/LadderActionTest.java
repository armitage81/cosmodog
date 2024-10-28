package antonafanasjew.cosmodog.actions.generic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LadderActionTest {

    LadderAction ladderAction;
    LadderAction ladderActionWithManySteps;

    @BeforeEach
    public void setUp() {
        ladderAction = new LadderAction(1000, 10);
        ladderAction.onTrigger();
        ladderActionWithManySteps = new LadderAction(1000, 10000);
        ladderActionWithManySteps.onTrigger();
    }

    @Test
    public void testStep0() {
        ladderAction.onUpdate(0, 0, null, null);
        assertEquals(0, (float)ladderAction.getProperty("value"), 0.001);
    }

    @Test
    public void testStep5() {
        ladderAction.onUpdate(0, 500, null, null);
        assertEquals(0.5f, (float)ladderAction.getProperty("value"), 0.001);
    }

    @Test
    public void testStep9() {
        ladderAction.onUpdate(0, 999, null, null);;
        assertEquals(0.9f, (float)ladderAction.getProperty("value"), 0.001);
    }

    @Test
    public void testMaxCompletion() {
        ladderAction.onUpdate(0, 1000, null, null);
        assertEquals(0.9f, (float)ladderAction.getProperty("value"), 0.001);
    }

    @Test
    public void testManySteps() {
        ladderActionWithManySteps.onUpdate(0, 500, null, null);
        assertEquals(0.5f, (float)ladderActionWithManySteps.getProperty("value"), 0.001);
    }
}
