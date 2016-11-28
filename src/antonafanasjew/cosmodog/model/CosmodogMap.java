package antonafanasjew.cosmodog.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.mapmodifications.MapModification;
import antonafanasjew.cosmodog.model.mapmodifications.MapModificationImpl;
import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

/**
 * Represents the game map as model. Does not contains information about the
 * tile ids etc, but game relevant model informations.
 * 
 * Note that all modification on the map (collected infobits, busted walls, opened doors)
 * will be saved in objects of this class and not on the TiledMap.
 * 
 *  The TiledMap object will be used only for drawing the original map. It will be never modified.
 *  (The reason is: It is not serializable and cannot be saved as part of users game progress)
 *
 */
public class CosmodogMap extends CosmodogModel {

	private static final long serialVersionUID = -7464038323408300703L;

	private transient CustomTiledMap customTiledMap;
	
	private Set<Enemy> enemies = Sets.newHashSet();
	private Set<Piece> mapPieces = Sets.newHashSet();
	private Set<Piece> effectPieces = Sets.newHashSet();
	private Set<Piece> markedTilePieces = Sets.newHashSet();
	private MapModification mapModification = new MapModificationImpl();
	
	private Platform cachedPlatform = null;
	private boolean platformCacheInitialized = false;

	public CosmodogMap(CustomTiledMap customTiledMap) {
		this.setCustomTiledMap(customTiledMap);
	}
	
	public Set<Piece> getMapPieces() {
		return mapPieces;
	}
	
	public Set<Piece> getEffectPieces() {
		return effectPieces;
	}
	
	public Set<Piece> getMarkedTilePieces() {
		return markedTilePieces;
	}
	
	public Set<Piece> getInfobits() {
		Set<Piece> infobits = Sets.filter(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobit);
			}
		});
		return infobits;
	}
	
	public Set<Piece> getSupplies() {
		Set<Piece> supplies = Sets.filter(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.supplies);
			}
		});
		return supplies;
	}
	
	public Set<Piece> visibleMapPieces(int x, int y, int width, int height, int grace) {
		Set<Piece> retVal = Sets.newHashSet();
		for (Piece piece : mapPieces) {
			if (piece.getPositionX() >= x - grace && piece.getPositionX() < x + width + grace) {
				if (piece.getPositionY() >= y - grace && piece.getPositionY() < y + height + grace) {
					retVal.add(piece);
				}
			}
		}
		return retVal;
	}
	
	public Piece pieceAtTile(int x, int y) {
		Piece retVal = null;
		for (Piece piece : mapPieces) {
			if (piece.getPositionX() == x && piece.getPositionY() == y) {
				retVal = piece;
			}
		}
		return retVal;
	}

	
	public Set<Piece> visibleEffectPieces(int x, int y, int width, int height, int grace) {
		Set<Piece> retVal = Sets.newHashSet();
		for (Piece piece : effectPieces) {
			if (piece.getPositionX() >= x - grace && piece.getPositionX() < x + width + grace) {
				if (piece.getPositionY() >= y - grace && piece.getPositionY() < y + height + grace) {
					retVal.add(piece);
				}
			}
		}
		return retVal;
	}
	
	public Set<Piece> visibleMarkedTilePieces(int x, int y, int width, int height, int grace) {
		Set<Piece> retVal = Sets.newHashSet();
		for (Piece piece : markedTilePieces) {
			if (piece.getPositionX() >= x - grace && piece.getPositionX() < x + width + grace) {
				if (piece.getPositionY() >= y - grace && piece.getPositionY() < y + height + grace) {
					retVal.add(piece);
				}
			}
		}
		return retVal;
	}

	public Set<Enemy> getEnemies() {
		return enemies;
	}

	public Set<Enemy> visibleEnemies(int x, int y, int width, int height, int grace) {
		Set<Enemy> retVal = Sets.newHashSet();
		for (Enemy enemy : enemies) {
			if (enemy.getPositionX() >= x - grace && enemy.getPositionX() < x + width + grace) {
				if (enemy.getPositionY() >= y - grace && enemy.getPositionY() < y + height + grace) {
					retVal.add(enemy);
				}
			}
		}
		return retVal;
	}

	public Platform getCachedPlatform(CosmodogGame cosmodogGame) {
		
		if (!platformCacheInitialized) {
			Set<Piece> pieces = getMapPieces();
			
			for (Piece piece : pieces) {
				if (piece instanceof Platform) {
					cachedPlatform = (Platform)piece;
					break;
				}
			}
			
			platformCacheInitialized = true;
			
		}

		return cachedPlatform;
		
	}
	
	public void clearPlatformCache() {
		this.platformCacheInitialized = false;
		this.cachedPlatform = null;
	}

	public int getWidth() {
		return this.getCustomTiledMap().getWidth();
	}
	
	public int getHeight() {
		return this.getCustomTiledMap().getHeight();
	}
	
	public int getTileWidth() {
		return this.getCustomTiledMap().getTileWidth();
	}
	
	public int getTileHeight() {
		return this.getCustomTiledMap().getTileHeight();
	}

	public int getTileId(int x, int y, int layerIndex) {
		return this.mapModification.getTileId(this.getCustomTiledMap(), x, y, layerIndex);
	}
	
	public Map<String, TiledObjectGroup> getObjectGroups() {
		return this.getCustomTiledMap().getObjectGroups();
	}
	
	public List<TiledMapLayer> getMapLayers() {
		return this.getCustomTiledMap().getMapLayers();
	}

	public MapModification getMapModification() {
		return mapModification;
	}

	public CustomTiledMap getCustomTiledMap() {
		return customTiledMap;
	}

	public void setCustomTiledMap(CustomTiledMap customTiledMap) {
		this.customTiledMap = customTiledMap;
	}

}
