package antonafanasjew.cosmodog.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import antonafanasjew.cosmodog.caching.PieceCache;
import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.topology.Position;

public class PiecesUtils {

	public static float distanceBetweenPieces(Piece piece1, Piece piece2) {
		Position p1 = piece1.getPosition();
		Position p2 = piece2.getPosition();
		return CosmodogMapUtils.distanceBetweenPositions(p1, p2);
	}
	
	public static String pieceType(Piece piece) {
		String pieceType;
		if (piece instanceof Collectible collectible) {

            Collectible.CollectibleType collectibleType = collectible.getCollectibleType();

			if (collectibleType == Collectible.CollectibleType.COMPOSED) {
				pieceType = CollectibleComposed.class.getSimpleName();
			} else  if (collectibleType == Collectible.CollectibleType.TOOL) {
				pieceType = ((CollectibleTool)collectible).getToolType().name();
			} else if (collectibleType == Collectible.CollectibleType.WEAPON) {
				CollectibleWeapon collWeapon = (CollectibleWeapon)piece;
				pieceType = Mappings.WEAPON_TYPE_TO_PIECE_TYPE.get(collWeapon.getWeapon().getWeaponType());
			} else if (collectibleType == Collectible.CollectibleType.AMMO) {
				CollectibleAmmo collAmmo = (CollectibleAmmo)piece;
				pieceType = Mappings.WEAPON_TYPE_TO_AMMO_PIECE_TYPE.get(collAmmo.getWeaponType());
			} else if (collectibleType == Collectible.CollectibleType.KEY) {
				pieceType = CollectibleKey.class.getSimpleName() + "_" + ((CollectibleKey)piece).getKey().getDoorType().name();
			} else if (collectibleType == Collectible.CollectibleType.LOG) {
				pieceType = CollectibleLog.class.getSimpleName();
			}

			else {

				CollectibleGoodie goodie = (CollectibleGoodie)collectible;

				pieceType = goodie.getGoodieType().name();
			}

		} else if (piece instanceof Vehicle) {
			pieceType = Vehicle.class.getSimpleName();
		} else if (piece instanceof Platform) {
			pieceType = Platform.class.getSimpleName();
		} else {
			pieceType = null;
		}
		return pieceType;	
	}
}
