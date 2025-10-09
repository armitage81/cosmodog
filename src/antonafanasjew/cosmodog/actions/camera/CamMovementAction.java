package antonafanasjew.cosmodog.actions.camera;

import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;

import java.io.Serial;

public class CamMovementAction extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -2679131506727529121L;

	private Position startPositionInPixels;

	private final Position targetPositionInPixels;

	public CamMovementAction(int duration, Position targetPositionInPixels) {
		super(duration);
		this.targetPositionInPixels = targetPositionInPixels;
	}

	@Override
	public void onTrigger() {
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		startPositionInPixels = Position.fromCoordinates(
				cam.viewCopy().centerX() / cam.getZoomFactor(),
				cam.viewCopy().centerY() / cam.getZoomFactor(),
				cam.viewCopy().getMapType()
		);
	}

	@Override
	public void onUpdateInternal(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();

		Point startPoint = new Point(startPositionInPixels.getX(), startPositionInPixels.getY());
		Point endPoint = new Point(targetPositionInPixels.getX(), targetPositionInPixels.getY());
		float horizontalDiff = endPoint.x - startPoint.x;
		float verticalDiff = endPoint.y - startPoint.y;

		float currentX = startPositionInPixels.getX() + horizontalDiff * getCompletionRate();
		float currentY = startPositionInPixels.getY() + verticalDiff * getCompletionRate();

		currentX *= cam.getZoomFactor();
		currentY *= cam.getZoomFactor();

		currentX = currentX - cam.viewCopy().width() / 2;
		currentY = currentY - cam.viewCopy().height() / 2;

		try {
			cam.move(Position.fromCoordinates(currentX, currentY, cam.viewCopy().getMapType()));
		} catch (CamPositioningException e) {
			Log.error("Cam out of bounds: " + currentX + "/" + currentY);
		}
	}

}

