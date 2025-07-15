package antonafanasjew.cosmodog.actions.camera;

import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.PositionUtils;

public class CamMovementUtils {

    public static final int SPEED_FAST = 480;
    public static final int SPEED_MEDIUM = 240;

    public static int movementDuration(int pixelPerSecond, Position startPixelPosition, Position targetPixelPosition) {
        float distance = PositionUtils.distance(startPixelPosition, targetPixelPosition);

        float pixelPerMillisecond = pixelPerSecond / 1000.0f;

        return (int)(distance / pixelPerMillisecond);
    }

}
