package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;
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
            public Position getPosition() {
                return Position.fromCoordinates(2, 7, ApplicationContextUtils.mapDescriptorMain());
            }

            @Override
            public DirectionType getDirection() {
                return DirectionType.UP;
            }
        };

        Position element1 = Position.fromCoordinates(0f, 1f, ApplicationContextUtils.mapDescriptorMain());
        Position element2 = Position.fromCoordinates(0f, 2f, ApplicationContextUtils.mapDescriptorMain());
        Position element3 = Position.fromCoordinates(0f, 3f, ApplicationContextUtils.mapDescriptorMain());

        //The observer is on pos 2/7 and looks up.
        //Visible positions are 2/6, 2/5, 2/4.


    }

}
