package antonafanasjew.cosmodog.rendering.renderer.pieces;

import java.util.Map;

import org.newdawn.slick.Animation;

import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Vehicle;

public class VehicleRenderer extends AbstractPieceRenderer {

	private Map<DirectionType, String> vehicleDirection2animationKey = Maps.newHashMap();
	
	public VehicleRenderer() {
		vehicleDirection2animationKey.put(DirectionType.RIGHT, "vehicleRight");
		vehicleDirection2animationKey.put(DirectionType.DOWN, "vehicleDown");
		vehicleDirection2animationKey.put(DirectionType.LEFT, "vehicleLeft");
		vehicleDirection2animationKey.put(DirectionType.UP, "vehicleUp");
	}
	
	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		Vehicle vehicle = (Vehicle)piece;
		DirectionType direction = vehicle.getDirection();
		String animationKey = vehicleDirection2animationKey.get(direction);
		Animation vehicleAnimation = applicationContext.getAnimations().get(animationKey);
		vehicleAnimation.draw((vehicle.getPositionX() - tileNoX) * tileWidth, (vehicle.getPositionY() - tileNoY) * tileHeight);	}

}
