package antonafanasjew.cosmodog.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BrokenFunctionTest {

    @Test
    public void testBrokenFunction() {

        BrokenFunction<Float> bf = new BrokenFunction<>();

        bf.registerElement(BrokenFunctionElement.instance(0.25f, x -> 10 * x));
        bf.registerElement(BrokenFunctionElement.instance(0.5f, x -> 10f));
        bf.registerElement(BrokenFunctionElement.instance(0.25f, x -> -10f * x + 10));
        bf.registerValueForFullCompletion(0.0f);

        assertEquals(0, bf.apply(0.0f));
        assertEquals(10f, bf.apply(0.25f));
        assertEquals(10f, bf.apply(0.75f));
        assertEquals(0f, bf.apply(1.0f));



    }

}
