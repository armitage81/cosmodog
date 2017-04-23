package antonafanasjew.cosmodog.util;

import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CollectibleAmmo;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleKey;
import antonafanasjew.cosmodog.model.CollectibleLog;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Mark;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.CollectibleTool.ToolType;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.actors.Vehicle;
import antonafanasjew.cosmodog.model.dynamicpieces.Door.DoorType;
import antonafanasjew.cosmodog.model.gamelog.GameLogs;
import antonafanasjew.cosmodog.model.upgrades.Key;
import antonafanasjew.cosmodog.model.upgrades.Weapon;

public class PieceFactory {

	public static Piece createPieceFromTileType(TileType tileType) {
		
		Piece piece = null;
		
		GoodieType goodieTypeRepresentedByTile = Mappings.MAP_TILE_TO_GOODIE_TYPE.get(tileType);
		if (goodieTypeRepresentedByTile != null) {
			CollectibleGoodie goodie = new CollectibleGoodie(goodieTypeRepresentedByTile);
			piece = goodie;
		}
						
		ToolType toolTypeRepresentedByTile = Mappings.MAP_TILE_TO_TOOL_TYPE.get(tileType);
		if (toolTypeRepresentedByTile != null) {
			CollectibleTool tool = new CollectibleTool(toolTypeRepresentedByTile);
			piece = tool;
		}

		if (TileType.VEHICLE.getTileId() == tileType.getTileId()) {
			Vehicle vehicle = new Vehicle();
			piece = vehicle;
		}

		if (TileType.PLATFORM.getTileId() == tileType.getTileId()) {
			Platform platform = new Platform();
			piece = platform;
		}

		if (TileType.WEAPONS_TILES.contains(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileType.getTileId()))) {

			WeaponType weaponType = Mappings.COLLECTIBLE_WEAPON_TILE_TYPE_2_WEAPON_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileType.getTileId()));
			Weapon weapon = new Weapon(weaponType);
			CollectibleWeapon collWeapon = new CollectibleWeapon(weapon);
			piece = collWeapon;

		}

		if (TileType.AMMO_TILES.contains(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileType.getTileId()))) {
			WeaponType weaponType = Mappings.COLLECTIBLE_AMMO_TILE_TYPE_2_WEAPON_TYPE.get(TileType.getByLayerAndTileId(Layers.LAYER_META_COLLECTIBLES, tileType.getTileId()));
			CollectibleAmmo collAmmo = new CollectibleAmmo(weaponType);
			piece = collAmmo;
		}

		if (TileType.RED_ALIEN_KEYCARD.getTileId() <= tileType.getTileId() && tileType.getTileId() <= TileType.WHITE_ALIEN_KEYCARD.getTileId()) {
			DoorType doorType = Mappings.TILE_ID_TO_KEY_TYPE.get(tileType.getTileId());
			Key key = new Key();
			key.setDoorType(doorType);
			CollectibleKey collectibleKey = new CollectibleKey(key);
			piece = collectibleKey;
		}
		
		
		if (TileType.LOG_CARD_0.getTileId() <= tileType.getTileId() && tileType.getTileId() <= TileType.LOG_CARD_35.getTileId()) {
			CollectibleLog collectibleLog = new CollectibleLog(GameLogs.SPECIFIC_LOGS_SERIES, Mappings.MAP_TILE_TO_UNSORTED_LOG_ID.get(tileType));
			piece = collectibleLog;
		}
		
		if (TileType.LOG_CARD_SERIES_0.getTileId() <= tileType.getTileId() && tileType.getTileId() <= TileType.LOG_CARD_SERIES_17.getTileId()) {
			CollectibleLog collectibleLog = new CollectibleLog(Mappings.MAP_TILE_TO_LOG_SERIES.get(tileType), "<irrelevant>");
			piece = collectibleLog;
		}
		
		
		
		if (TileType.FUEL.getTileId() == tileType.getTileId()) {
			Mark m = new Mark(Mark.FUEL_MARK_TYPE);
			piece = m;
		}
		
		return piece;
	}
	
}
