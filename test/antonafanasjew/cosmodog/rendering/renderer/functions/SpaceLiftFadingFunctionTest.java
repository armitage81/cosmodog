package antonafanasjew.cosmodog.rendering.renderer.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceLiftFadingFunctionTest {

    @Test
    public void testFunction() {

        SpaceLiftFadingFunction f = new SpaceLiftFadingFunction(0.1f, 0.1f, 0.1f, 0.1f);

        assertEquals(0f, f.apply(0f), 0.001f); //Initial darkness at the beginning of the process
        assertEquals(0f, f.apply(0.05f), 0.001f); //Initial darkness at half the process
        assertEquals(0f, f.apply(0.9999f), 0.001f); //Initial darkness at the end of the process
        assertEquals(0.5f, f.apply(0.15f), 0.001f); //Linear fading in at half the process
        assertEquals(1.0f, f.apply(0.19999f), 0.001f); //Linear fading in at the end of the process
        assertEquals(1.0f, f.apply(0.5f), 0.001f); //Full opacity at half the process
        assertEquals(1.0f, f.apply(0.7999f), 0.001f); //Full opacity at the end of the process
        assertEquals(0.5f, f.apply(0.85f), 0.001f); //Fading out at half the process
        assertEquals(0.0f, f.apply(0.8999f), 0.001f); //Fading out at end of the process
        assertEquals(0.0f, f.apply(0.95f), 0.001f); //Final darkness at half the process
        assertEquals(0.0f, f.apply(1f), 0.001f); //Final darkness at the end of the process

    }

}
