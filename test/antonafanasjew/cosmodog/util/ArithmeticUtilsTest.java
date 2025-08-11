package antonafanasjew.cosmodog.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ArithmeticUtilsTest {

    @Test
    public void testRemainingPhaseDuration() {

        int[] retVal;

        List<Integer> rhythm = new ArrayList<>();
        rhythm.add(1);
        rhythm.add(1);

        retVal = ArithmeticUtils.remainingPhaseDuration(rhythm, 4);
        assertEquals(0, retVal[0]);
        assertEquals(1, retVal[1]);


        rhythm.clear();
        rhythm.add(10);
        rhythm.add(5);

        retVal = ArithmeticUtils.remainingPhaseDuration(rhythm, 0);
        assertEquals(0, retVal[0]);
        assertEquals(10, retVal[1]);

        retVal = ArithmeticUtils.remainingPhaseDuration(rhythm, 10);
        assertEquals(1, retVal[0]);
        assertEquals(5, retVal[1]);

        retVal = ArithmeticUtils.remainingPhaseDuration(rhythm, 12);
        assertEquals(1, retVal[0]);
        assertEquals(3, retVal[1]);

        retVal = ArithmeticUtils.remainingPhaseDuration(rhythm, 35);
        assertEquals(0, retVal[0]);
        assertEquals(5, retVal[1]);

    }

}
