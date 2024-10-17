package antonafanasjew.cosmodog.rendering.renderer.pieces;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.util.Mappings;

public class KeyRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		CollectibleKey collectibleKey = (CollectibleKey)piece;
		DoorType keyType = collectibleKey.getKey().getDoorType();
		
		String animationId = Mappings.KEY_TYPE_2_ANIMATION_ID.get(keyType);
		
		Animation animation = applicationContext.getAnimations().get(animationId);
		
		animation.draw((piece.getPosition().getX() - tileNoX) * tileWidth, (piece.getPosition().getY() - tileNoY) * tileHeight);
		
	}

}
