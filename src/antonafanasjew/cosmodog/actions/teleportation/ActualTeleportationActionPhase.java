package antonafanasjew.cosmodog.actions.teleportation;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.actions.FixedLengthAsyncAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.tiledmap.TiledLineObject.Point;
import antonafanasjew.cosmodog.tiledmap.TiledPolylineObject;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.io.Serial;

public class ActualTeleportationActionPhase extends FixedLengthAsyncAction {

	@Serial
	private static final long serialVersionUID = -7801472349956706302L;

	private final TiledPolylineObject teleportConnection;
	private float initialCamX;
	private float initialCamY;
	
	public ActualTeleportationActionPhase(int duration, TiledPolylineObject teleportConnection, TeleportationAction.TeleportationState state) {
		super(duration);
		this.teleportConnection = teleportConnection;
		this.getProperties().put("state", state);
	}

	@Override
	public void onTrigger() {
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		initialCamX = cam.viewCopy().minX();
		initialCamY = cam.viewCopy().minY();
		ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TELEPORT_TRANSFER).play();
	}
	
	@Override
	public void onUpdate(int before, int after, GameContainer gc, StateBasedGame sbg) {
		
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		
		float completion = (float)after / (float)getDuration();
		
		if (completion > 1) {
			completion = 1;
		}
		
		Point startPoint = teleportConnection.getPoints().get(0);
		Point endPoint = teleportConnection.getPoints().get(1);
		
		float horizontalDiff = endPoint.x - startPoint.x;
		float verticalDiff = endPoint.y - startPoint.y;

		
		float currentX = initialCamX + horizontalDiff * completion * cam.getZoomFactor();
		float currentY = initialCamY + verticalDiff * completion * cam.getZoomFactor();
		
		try {
			cam.move(Position.fromCoordinates(currentX, currentY, cam.viewCopy().getMapType()));
		} catch (CamPositioningException e) {
			Log.error("Cam out of bounds: " + currentX + "/" + currentY);
		}
		
	}
	
	@Override
	public void onEnd() {

		int tileLength = TileUtils.tileLengthSupplier.get();

		CosmodogGame cosmodogGame = ApplicationContextUtils.getCosmodogGame();
		CosmodogMap map = cosmodogGame.mapOfPlayerLocation();
		
		Point endPoint = teleportConnection.getPoints().get(1);
		
		Player player = ApplicationContextUtils.getPlayer();
		Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();
		
		int targetPosX = (int)endPoint.x / tileLength;
		int targetPosY = (int)endPoint.y / tileLength;
		
		player.setPosition(Position.fromCoordinates(targetPosX, targetPosY, map.getMapType()));

		cam.focusOnPiece(cosmodogGame, 0, 0, player);
	}

}
