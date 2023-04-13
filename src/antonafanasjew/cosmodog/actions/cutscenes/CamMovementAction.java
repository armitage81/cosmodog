package antonafanasjew.cosmodog.actions.cutscenes;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;

public class CamMovementAction extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -2679131506727529121L;

	private float initialCamX;
	private float initialCamY;
	private float targetCamX;
	private float targetCamY;
	private CosmodogGame cosmodogGame;
	
	public CamMovementAction(int duration, float targetCamX, float targetCamY, CosmodogGame cosmodogGame) {
		super(duration);
		this.targetCamX = targetCamX;
		this.targetCamY = targetCamY;
		this.cosmodogGame = cosmodogGame;
	}

	@Override
	public void onTrigger() {
		Cam cam = cosmodogGame.getCam();
		initialCamX = cam.viewCopy().centerX() / cam.getZoomFactor();
		initialCamY = cam.viewCopy().centerY() / cam.getZoomFactor();
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		Cam cam = cosmodogGame.getCam();
		
		//How far has the camera moved already considering configured duration and time passed?
		//At the beginning of the action, it will be 0.0f.
		//At the end of the action, it will be 1.0f.
		float completion = (float)after / (float)getDuration();
		if (completion > 1) {
			completion = 1;
		}
		
		//How many pixels are start and target points apart?
		Point startPoint = new Point(initialCamX, initialCamY);
		Point endPoint = new Point(targetCamX, targetCamY);
		float horizontalDiff = endPoint.x - startPoint.x;
		float verticalDiff = endPoint.y - startPoint.y;

		//Considering completion and distance between start and target points: Where must be our camera now?
		//For instance, if x1 = 100, x2 = 350, y1 = 200, y2 = 600 and completion = 0.5, the camera position must be at 225/400.
		float currentX = initialCamX + horizontalDiff * completion;
		float currentY = initialCamY + verticalDiff * completion;
		
		//Till now, we have not considered zooming. Initial and target points where given considering zoom factor 1.0
		//If passed distance was 200 pixels and we zoom out by factor 0.5, the map becomes half as big, and all distances with it. 
		//The resulting passed distance will be 100 pixels then.
		//If we had zoomed in by factor 2.0, the passed distance would be 400 pixels.
		currentX *= cam.getZoomFactor();
		currentY *= cam.getZoomFactor();
		
		//Till now, we calculated the left top corner of the cam view.
		//But we want to center on the given point.
		//For instance, if the camera's view size is 1920*1080 and the initial point is at 5960/4540 and the completion is 0.0,
		//the cam has to move to the point 5000/4000.
		currentX = currentX - cam.viewCopy().width() / 2;
		currentY = currentY - cam.viewCopy().height() / 2;

		try {
			cam.move(currentX, currentY);
		} catch (CamPositioningException e) {
			Log.error("Cam out of bounds: " + currentX + "/" + currentY);
		}
	}
	
	@Override
	public void onEnd() {

	}
	
}

