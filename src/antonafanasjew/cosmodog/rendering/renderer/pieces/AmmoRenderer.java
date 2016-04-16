package antonafanasjew.cosmodog.rendering.renderer.pieces;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.util.Mappings;

public class AmmoRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		CollectibleAmmo collectibleAmmo = (CollectibleAmmo)piece;
		WeaponType weaponType = collectibleAmmo.getWeaponType();
		
		String animationId = Mappings.WEAPON_TYPE_2_AMMO_ANIMATION_ID.get(weaponType);
		
		Animation animation = applicationContext.getAnimations().get(animationId);
		
		animation.draw((piece.getPositionX() - tileNoX) * tileWidth, (piece.getPositionY() - tileNoY) * tileHeight);
		
	}

}
