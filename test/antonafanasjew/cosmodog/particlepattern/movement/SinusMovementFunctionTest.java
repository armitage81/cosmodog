package antonafanasjew.cosmodog.particlepattern.movement;

import antonafanasjew.cosmodog.topology.Vector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SinusMovementFunctionTest {


    /**
     * There is no scale. The sine function goes between -1 and 1 with the period of 2 * PI
     */
    @Test
    void testDefaultCase() {

        SinusMovementFunction f = new SinusMovementFunction(1f, 1f, 0f, 1f);
        Vector res;
        res = f.applyInternal(0f);
        assertEquals(0f, res.getY(), 0.0001);
        res = f.applyInternal((float)(Math.PI / 2));
        assertEquals(1f, res.getY(), 0.0001);
        res = f.applyInternal((float)(Math.PI));
        assertEquals(0f, res.getY(), 0.0001);
        res = f.applyInternal((float)(Math.PI * 3/2));
        assertEquals(-1f, res.getY(), 0.0001);
        res = f.applyInternal((float)(Math.PI * 2));
        assertEquals(0f, res.getY(), 0.0001);
    }

}