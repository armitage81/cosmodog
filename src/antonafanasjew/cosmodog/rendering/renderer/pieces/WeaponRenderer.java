package antonafanasjew.cosmodog.rendering.renderer.pieces;

import org.newdawn.slick.Animation;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.upgrades.Weapon;
import antonafanasjew.cosmodog.util.Mappings;

public class WeaponRenderer extends AbstractPieceRenderer {

	@Override
	protected void render(ApplicationContext applicationContext, int tileWidth, int tileHeight, int tileNoX, int tileNoY, Piece piece) {
		CollectibleWeapon collectibleWeapon = (CollectibleWeapon)piece;
		Weapon weapon = collectibleWeapon.getWeapon();
		WeaponType weaponType = weapon.getWeaponType();
		
		String animationId = Mappings.WEAPON_TYPE_2_ANIMATION_ID.get(weaponType);
		Animation animation = applicationContext.getAnimations().get(animationId);
		
		int tileXCenter = (piece.getPositionX() - tileNoX) * tileWidth + (tileWidth / 2);
		int tileYCenter = (piece.getPositionY() - tileNoY) * tileHeight + (tileHeight / 2);
		
		float animationScaleFactor = 0.5f;
		
		float animationWidth = animation.getWidth() * animationScaleFactor;
		float animationHeight = animation.getHeight() * animationScaleFactor;
		
		animation.draw(tileXCenter - animationWidth / 2, tileYCenter - animationHeight /  2, animationWidth, animationHeight);		
	}

}
