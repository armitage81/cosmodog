package antonafanasjew.cosmodog.rendering.renderer.pieces;

import java.util.Map;

import antonafanasjew.cosmodog.rendering.renderer.OccupiedPlatformRenderer;
import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;

import com.google.common.collect.Maps;

/**
 * This renderer is used only to render the platform as a piece on the map.
 * Do not use it to render the inventory item. Use {@link OccupiedPlatformRenderer} instead
 */
public class PlatformRenderer extends AbstractPieceRenderer {

	private final Map<DirectionType, String> platformDirection2animationKey = Maps.newHashMap();
	
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
		platformAnimation.draw((platform.getPosition().getX() - tileNoX - 3) * tileWidth, (platform.getPosition().getY() - tileNoY - 3) * tileHeight);	}

}
