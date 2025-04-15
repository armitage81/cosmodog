package antonafanasjew.cosmodog.model;

import java.util.*;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.ActivatableHolder;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.model.portals.interfaces.SwitchableHolder;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.CustomTiledMap;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.model.mapmodifications.MapModification;
import antonafanasjew.cosmodog.model.mapmodifications.MapModificationImpl;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.tiledmap.TiledMapLayer;
import antonafanasjew.cosmodog.tiledmap.TiledObjectGroup;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import org.newdawn.slick.AppletGameContainer;

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

	private MapType mapType;
	private Set<Enemy> enemies = Sets.newHashSet();
	private Map<Position, Piece> mapPieces = Maps.newHashMap();
	private Set<Piece> effectPieces = Sets.newHashSet();
	private Set<Piece> markedTilePieces = Sets.newHashSet();
	private Multimap<Class<?>, DynamicPiece> dynamicPieces = ArrayListMultimap.create();
	
	private List<MoveableGroup> moveableGroups = Lists.newArrayList();
	
	private MapModification mapModification = new MapModificationImpl();
	
	private Platform cachedPlatform = null;
	private boolean platformCacheInitialized = false;

	public CosmodogMap(CustomTiledMap customTiledMap) {
		this.setCustomTiledMap(customTiledMap);
	}
	
	public Map<Position, Piece> getMapPieces() {
		return mapPieces;
	}
	
	public Set<Piece> getEffectPieces() {
		return effectPieces;
	}
	
	public Set<Piece> getMarkedTilePieces() {
		return markedTilePieces;
	}
	
	public Map<Position, Piece> getInfobits() {
		Map<Position, Piece> infobits = Maps.filterValues(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobit);
			}
		});
		return infobits;
	}
	
	public Map<Position, Piece> getInfobytes() {
		Map<Position, Piece> infobytes = Maps.filterValues(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobyte);
			}
		});
		return infobytes;
	}
	
	public Map<Position, Piece> getInfobanks() {
		Map<Position, Piece> infobanks = Maps.filterValues(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobank);
			}
		});
		return infobanks;
	}
	
	public Map<Position, Piece> getSupplies() {
		Map<Position, Piece> supplies = Maps.filterValues(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.supplies);
			}
		});
		return supplies;
	}
	
	public Map<Position, Piece> getMedkits() {
		Map<Position, Piece> supplies = Maps.filterValues(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.medipack);
			}
		});
		return supplies;
	}
	
	public Map<Position, Piece> getInsights() {
		Map<Position, Piece> supplies = Maps.filterValues(mapPieces, new Predicate<Piece>() {
			@Override
			public boolean apply(Piece piece) {
				return piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.insight);
			}
		});
		return supplies;
	}
	
	public Map<Position, Piece> visibleMapPieces(Position ref, int width, int height, int grace) {
		
		Map<Position, Piece> retVal = Maps.newHashMap();
		
		for (Position position : mapPieces.keySet()) {
			if (position.getX() >= ref.getX() - grace && position.getX() < ref.getX() + width + grace) {
				if (position.getY() >= ref.getY() - grace && position.getY() < ref.getY() + height + grace) {
					retVal.put(position, mapPieces.get(position));
				}
			}
		}
		
		return retVal;
	}
	
	public Piece pieceAtTile(Position position) {
		return mapPieces.get(position);
	}
	
	public Enemy enemyAtTile(Position ref) {
		Enemy retVal = null;
		for (Enemy enemy : enemies) {
			if (enemy.getPosition().equals(ref)) {
				retVal = enemy;
			}
		}
		return retVal;
	}

	
	public Set<Piece> visibleEffectPieces(Position ref, int width, int height, int grace) {
		Set<Piece> retVal = Sets.newHashSet();
		for (Piece piece : effectPieces) {
			if (piece.getPosition().getX() >= ref.getX() - grace && piece.getPosition().getX() < ref.getX() + width + grace) {
				if (piece.getPosition().getY() >= ref.getY() - grace && piece.getPosition().getY() < ref.getY() + height + grace) {
					retVal.add(piece);
				}
			}
		}
		return retVal;
	}

	public Set<DynamicPiece> dynamicPiecesAtPosition(Position position) {
		Set<DynamicPiece> retVal = new HashSet<>();
		CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(position.getMapType());
		Set<Class<?>> dynamicPieceTypes = map.getDynamicPieces().keySet();
		for (Class<?> clazz : dynamicPieceTypes) {
			Collection<DynamicPiece> l = map.getDynamicPieces().get(clazz);
			l.stream().filter(e -> e.getPosition().equals(position)).forEach(retVal::add);
		}
		return retVal;
	}

	public Multimap<Class<?>, DynamicPiece> visibleDynamicPieces(Position position, int width, int height, int grace) {
		return PlayerMovementCache.getInstance().getVisibleDynamicPieces();
	}

	public MapType getMapType() {
		return mapType;
	}

	public void setMapType(MapType mapType) {
		this.mapType = mapType;
	}

	public Set<Enemy> getEnemies() {
		return enemies;
	}

	public Set<Enemy> getEnemiesInRange() {
		return PlayerMovementCache.getInstance().getEnemiesInRange();
	}
	
	public Set<Enemy> nearbyEnemies(Position ref, int maxDistance) {
		Set<Enemy> retVal = Sets.newHashSet();
		for (Enemy enemy : enemies) {
			float enemyDistance = CosmodogMapUtils.distanceBetweenPositions(ref, enemy.getPosition());
			if (enemyDistance <= maxDistance) {
				retVal.add(enemy);
			}
		}
		return retVal;		
	}
	
	public Set<Enemy> visibleEnemies(Position ref, int width, int height, int grace) {
		Set<Enemy> retVal = Sets.newHashSet();
		for (Enemy enemy : enemies) {
			if (enemy.getPosition().getX() >= ref.getX() - grace && enemy.getPosition().getX() < ref.getX() + width + grace) {
				if (enemy.getPosition().getY() >= ref.getY() - grace && enemy.getPosition().getY() < ref.getY() + height + grace) {
					retVal.add(enemy);
				}
			}
		}
		return retVal;
	}

	public Platform getCachedPlatform(CosmodogGame cosmodogGame) {
		
		if (!platformCacheInitialized) {
			Collection<Piece> pieces = getMapPieces().values();
			
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

	public int getTileId(Position position, int layerIndex) {
		return this.mapModification.getTileId(this.getCustomTiledMap(), position, layerIndex);
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

	public Multimap<Class<?>, DynamicPiece> getDynamicPieces() {
		return dynamicPieces;
	}

	public Optional<DynamicPiece> dynamicPieceAtPosition(Class<? extends DynamicPiece> clazz, Position position) {
		return dynamicPieces.get(clazz).stream().filter(e -> e.getPosition().equals(position)).findFirst();
	}

	public Optional<SwitchableHolder> switchableHolderAtPosition(Position position) {

		for (Class<?> c : dynamicPieces.keySet()) {
			Collection<DynamicPiece> l = dynamicPieces.get(c);
			for (DynamicPiece dp : l) {
				if (dp instanceof SwitchableHolder) {
					if (dp.getPosition().equals(position)) {
						return Optional.of((SwitchableHolder) dp);
					}
				}
			}
		}

		return Optional.empty();
	}

	public Optional<Switchable> switchableAtPosition(Position position) {

		for (Class<?> c : dynamicPieces.keySet()) {
			Collection<DynamicPiece> l = dynamicPieces.get(c);
			for (DynamicPiece dp : l) {
				if (dp instanceof Switchable) {
					if (dp.getPosition().equals(position)) {
						return Optional.of((Switchable) dp);
					}
				}
			}
		}

		return Optional.empty();
	}



	public Optional<ActivatableHolder> activatableHolderAtPosition(Position position) {

		for (Class<?> c : dynamicPieces.keySet()) {
			Collection<DynamicPiece> l = dynamicPieces.get(c);
			for (DynamicPiece dp : l) {
				if (dp instanceof ActivatableHolder) {
					if (dp.getPosition().equals(position)) {
						return Optional.of((ActivatableHolder) dp);
					}
				}
			}
		}

		return Optional.empty();
	}

	public Optional<Activatable> activatableAtPosition(Position position) {

		for (Class<?> c : dynamicPieces.keySet()) {
			Collection<DynamicPiece> l = dynamicPieces.get(c);
			for (DynamicPiece dp : l) {
				if (dp instanceof Activatable) {
					if (dp.getPosition().equals(position)) {
						return Optional.of((Activatable) dp);
					}
				}
			}
		}

		return Optional.empty();
	}





	public List<MoveableGroup> getMoveableGroups() {
		return moveableGroups;
	}
}
