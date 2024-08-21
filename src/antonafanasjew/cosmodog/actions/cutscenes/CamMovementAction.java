package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;

import java.io.Serial;

/**
 * Represents a camera movement action.
 * <p>
 * During this action, the camera moves linearly from the current point of the world to the target point of the world.
 * <p>
 * Points of the world are given in world coordinates without considering zooming.
 * Zooming in Cosmodog is done by scaling the scene. However, in this action, the points are provided as if not zoomed.
 * <p>
 * This action is a fixed length action. It will take a fixed amount of time to finish regardless of the distance
 * between the start and target points. The camera will move faster the further the points are apart.
 */
public class CamMovementAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -2679131506727529121L;

	/*
	 * Horizontal component of the point in the world at which the center of the camera is located before the movement.
	 * It is given in unscaled world coordinates.
	 */
	private float initialCamX;

	/*
	 * Vertical component of the point in the world at which the center of the camera is located before the movement.
	 * It is given in unscaled world coordinates.
	 */
	private float initialCamY;

	/*
	 * Horizontal component of the point in the world to which the center of the camera will be moved.
	 * It is given in unscaled world coordinates.
	 */
	private final float targetCamX;

	/*
	 * Vertical component of the point in the world to which the center of the camera will be moved.
	 * It is given in unscaled world coordinates.
	 */
	private final float targetCamY;

	/*
	 * The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	private final CosmodogGame cosmodogGame;

	/**
	 * Creates the action instance by defining where the camera should be pointed on the map,
	 * and how long the camera movement should take,
	 * <p>
	 * The camera target is defined as a point in the world.
	 * <p>
	 * Take note: Zooming is irrelevant for the target point in the world.
	 * In Cosmodog, zooming means that the scene is scaled while the view's size stays the same.
	 * But here, targetCamX and targetCamY are given in the unscaled world's coordinate system.
	 * <p>
	 * Example: Scene is 40x40 pixels big and the view is 10x10 pixels big. The camera must be moved to the middle of the scene.
	 * Without zoom, the target point is 20/20. With zoom factor 2.0, the target point would be 40/40.
	 * But since zooming is not considered, the target point is still 20/20.
	 *
	 * @param duration The duration of the camera movement to the target in milliseconds.
	 * @param targetCamX The horizontal component of the point in the world to which the center of the camera will be moved.
	 * @param targetCamY The vertical component of the point in the world to which the center of the camera will be moved.
	 * @param cosmodogGame The game instance. Used to retrieve global game objects, like the camera and the map.
	 */
	public CamMovementAction(int duration, float targetCamX, float targetCamY, CosmodogGame cosmodogGame) {
		super(duration);
		this.targetCamX = targetCamX;
		this.targetCamY = targetCamY;
		this.cosmodogGame = cosmodogGame;
	}

	/**
	 * Initializes the action by calculating the initial point of the camera in the world.
	 * <p>
	 * Take note: No zooming is considered when talking about the initial point.
	 * However, the positioning methods of the {@link Cam} class consider the zoom factor.
	 * That means that the initial point includes the zoom factor. That's why it must be divided by the latter.
	 */
	@Override
	public void onTrigger() {
		Cam cam = cosmodogGame.getCam();
		initialCamX = cam.viewCopy().centerX() / cam.getZoomFactor();
		initialCamY = cam.viewCopy().centerY() / cam.getZoomFactor();
	}

	/**
	 * Updates the action based on the passed time.
	 * <p>
	 * First, the completion rate is calculated by dividing the passed time of the action by the intended duration of the action.
	 * <p>
	 * Then, the horizontal and vertical distances between the initial and target points are calculated.
	 * <p>
	 * Afterward, the camera is moved to the point that is the completion rate (0..1) times the distance between the initial and target points.
	 * <p>
	 * Since no zooming is considered for the initial and target points, the real camera's position is adjusted by the zoom factor.
	 * <p>
	 * Finally, we have to subtract half of the view's width and height to center the camera on the point
	 * because the move method of the camera is meant for the top left anchor of the view.
	 *
	 * @param before Time offset of the last update as compared to the start of the action.
	 * @param after Time offset of the current update. after - before = time passed since the last update.
	 * @param gc GameContainer instance forwarded by the game state's update method.
	 * @param sbg StateBasedGame instance forwarded by the game state's update method.
	 */
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		Cam cam = cosmodogGame.getCam();

		float completion = (float)after / (float)getDuration();
		if (completion > 1) {
			completion = 1;
		}

		Point startPoint = new Point(initialCamX, initialCamY);
		Point endPoint = new Point(targetCamX, targetCamY);
		float horizontalDiff = endPoint.x - startPoint.x;
		float verticalDiff = endPoint.y - startPoint.y;

		float currentX = initialCamX + horizontalDiff * completion;
		float currentY = initialCamY + verticalDiff * completion;

		currentX *= cam.getZoomFactor();
		currentY *= cam.getZoomFactor();

		currentX = currentX - cam.viewCopy().width() / 2;
		currentY = currentY - cam.viewCopy().height() / 2;

		try {
			cam.move(currentX, currentY);
		} catch (CamPositioningException e) {
			Log.error("Cam out of bounds: " + currentX + "/" + currentY);
		}
	}

	/**
	 * This method does not do anything. This could lead to slightly wrong camera positioning in the end.
	 * However, this action is used only as a phase in the CamCenterAction, which will correct the camera's position
	 * at the very end.
	 */
	@Override
	public void onEnd() {

	}
}

