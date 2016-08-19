package antonafanasjew.cosmodog.rendering.renderer.pieces;

import java.util.Map;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;

import com.google.common.collect.Maps;

public class PlatformRenderer extends AbstractPieceRenderer {

	private Map<DirectionType, String> platformDirection2animationKey = Maps.newHashMap();
	
	public PlatformRenderer() {
		platformDirection2animationKey.put(DirectionType.RIGHT, "platformRight");
		platformDirection2animationKey.put(DirectionType.DOWN, "platformDown");
		platformDirection2animationKey.put(DirectionType.LEFT, "platformLeft");
		platformDirection2animationKey.put(DirectionType.UP, "platformUp");
	}
	
	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		Platform platform = (Platform)piece;
		DirectionType direction = platform.getDirection();
		String animationKey = platformDirection2animationKey.get(direction);
		Animation platformAnimation = applicationContext.getAnimations().get(animationKey);
		platformAnimation.draw((platform.getPositionX() - tileNoX - 3) * tileWidth, (platform.getPositionY() - tileNoY - 3) * tileHeight);	}

}
