package antonafanasjew.cosmodog.actions.camera;

import antonafanasjew.cosmodog.actions.fight.PhaseBasedAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.PositionUtils;

/**
 * There is a class {@link CamMovementAction} that moves the camera from one place to another.
 * This action has a fixed duration, though. That means that the speed of the camera
 * depends on the distance between the start and the end point.
 *
 * This action here has a variable length depending on the distance between the start and the end point,
 * resulting in the constant camera movement speed.
 *
 * This action delegates the actual camera movement to the {@link CamMovementAction} class and calculates the duration
 * of this movement based on the distance between the start and end points.
 */
public class CamMovementActionWithConstantSpeed extends PhaseBasedAction {

    public static final int SPEED_FAST = 160;

    private final int pixelPerSecond;
    private final Position targetCamPosition;
    private final CosmodogGame cosmodogGame;

    public CamMovementActionWithConstantSpeed(int pixelPerSecond, Position targetCamPosition, CosmodogGame cosmodogGame) {
        this.pixelPerSecond = pixelPerSecond;
        this.targetCamPosition = targetCamPosition;
        this.cosmodogGame = cosmodogGame;
    }

    @Override
    protected void onTriggerInternal() {
        Cam cam = cosmodogGame.getCam();

        Position startPixelPosition = Position.fromCoordinates(
                cam.viewCopy().centerX() / cam.getZoomFactor(),
                cam.viewCopy().centerY() / cam.getZoomFactor(),
                cam.viewCopy().getMapType()
        );

        Position targetPixelPosition = Position.fromCoordinates(targetCamPosition.getX(), targetCamPosition.getY(), targetCamPosition.getMapType());

        float distance = PositionUtils.distance(startPixelPosition, targetPixelPosition);

        float pixelPerMillisecond = pixelPerSecond / 1000.0f;
        int durationInMilliseconds = (int)(distance / pixelPerMillisecond);

        CamMovementAction delegate = new CamMovementAction(durationInMilliseconds, targetCamPosition, cosmodogGame);
        getPhaseRegistry().registerPhase("delegate", delegate);
    }
}
