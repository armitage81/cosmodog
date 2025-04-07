package antonafanasjew.cosmodog.geometry;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OscillationsTest {

    @Test
    public void testOscillationValue() {

        float retVal;

        //Beginning of the period in range 0:10 without offset should return middle of the length diff = 5.
        retVal = Oscillations.oscillation(0, 0, 10, 100_000_000, 0);
        assertEquals(5, retVal, 0.0001f);

        //Half the way between beginning and the middle of the period without offset should return length diff = 10;
        retVal = Oscillations.oscillation(24_999_999, 0, 10, 100_000_000, 0);
        assertEquals(10, retVal, 0.0001f);

        //Middle of the period in range 0:10 without offset should return middle of the length diff = 5.
        retVal = Oscillations.oscillation(49_999_999, 0, 10, 100_000_000, 0);
        assertEquals(5, retVal, 0.0001f);

        //Half the way between middle and the end of the period without offset should return 0;
        retVal = Oscillations.oscillation(74_999_999, 0, 10, 100_000_000, 0);
        assertEquals(0, retVal, 0.0001f);

        //End of the period in range 0:10 without offset should return middle of the length diff = 5.
        retVal = Oscillations.oscillation(99_999_999, 0, 10, 100_000_000, 0);
        assertEquals(5, retVal, 0.0001f);

        //Beginning of the period in range 0:10 with quarter period offset should return length diff = 10.
        retVal = Oscillations.oscillation(0, 0, 10, 100_000_000, 24_999_999);
        assertEquals(10, retVal, 0.0001f);

    }


}
