package antonafanasjew.cosmodog.actions.teleportation;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class PoisonDeactivationEndActionPhase extends FixedLengthAsyncAction {

	private static final long serialVersionUID = -5683528278656049586L;

	private TiledPolylineObject switchConnection;
	private float initialCamX;
	private float initialCamY;
	
	public PoisonDeactivationEndActionPhase(int duration, TiledPolylineObject switchConnection) {
		super(duration);
		this.switchConnection = switchConnection;
	}
	
	@Override
	public void onTrigger() {
		
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		initialCamX = cam.viewCopy().minX();
		initialCamY = cam.viewCopy().minY();
		
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		
		float completion = (float)after / (float)getDuration();
		
		if (completion > 1) {
			completion = 1;
		}
		
		//Take care. The start point here is the end point of the connection and vice versa.
		
		Point startPoint = switchConnection.getPoints().get(1);
		Point endPoint = switchConnection.getPoints().get(0);
		
		float horizontalDiff = endPoint.x - startPoint.x;
		float verticalDiff = endPoint.y - startPoint.y;

		
		float currentX = initialCamX + horizontalDiff * completion * cam.getZoomFactor();
		float currentY = initialCamY + verticalDiff * completion * cam.getZoomFactor();
		
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
