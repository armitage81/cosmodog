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
	
	@Override
	public void onEnd() {

	}
	
}

