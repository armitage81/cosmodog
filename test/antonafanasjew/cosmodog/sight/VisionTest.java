package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VisionTest {

    private Vision vision;

    @BeforeEach
    public void setUp() {
        vision = new Vision();
    }

    @Test
    public void testGetVisibleRectangle() throws CamPositioningException {
        Actor actor = new Actor() {
            @Override
            public int getPositionX() {
                return 2;
            }

            @Override
            public int getPositionY() {
                return 7;
            }

            @Override
            public DirectionType getDirection() {
                return DirectionType.UP;
            }
        };

        VisionElement element1 = new VisionElement(0, 1);
        VisionElement element2 = new VisionElement(0, 2);
        VisionElement element3 = new VisionElement(0, 3);

        //The observer is on pos 2/7 and looks up.
        //Visible positions are 2/6, 2/5, 2/4.


    }

}
