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
        ladderAction.onUpdate(0);
        assertEquals(0, ladderAction.getTransition().getValue(), 0.001);
    }

    @Test
    public void testStep5() {
        ladderAction.onUpdate(0.5f);
        assertEquals(0.5f, ladderAction.getTransition().getValue(), 0.001);
    }

    @Test
    public void testStep9() {
        ladderAction.onUpdate(0.99999f);
        assertEquals(0.9f, ladderAction.getTransition().getValue(), 0.001);
    }

    @Test
    public void testMaxCompletion() {
        ladderAction.onUpdate(1f);
        assertEquals(0.9f, ladderAction.getTransition().getValue(), 0.001);
    }

    @Test
    public void testManySteps() {
        ladderActionWithManySteps.onUpdate(0.5f);
        assertEquals(0.5f, ladderActionWithManySteps.getTransition().getValue(), 0.001);
    }
}
