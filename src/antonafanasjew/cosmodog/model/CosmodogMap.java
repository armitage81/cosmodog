package antonafanasjew.cosmodog.model;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.caching.PieceCache;
import antonafanasjew.cosmodog.caching.PiecePredicates;
import antonafanasjew.cosmodog.model.portals.interfaces.Activatable;
import antonafanasjew.cosmodog.model.portals.interfaces.ActivatableHolder;
import antonafanasjew.cosmodog.model.portals.interfaces.Switchable;
import antonafanasjew.cosmodog.model.portals.interfaces.SwitchableHolder;
import antonafanasjew.cosmodog.structures.PortalPuzzle;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.structures.SafeSpace;
import com.google.common.collect.Lists;
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

	private MapDescriptor mapDescriptor;
	private PieceCache mapPieces = new PieceCache(mapDescriptor, 20, 20);

	private List<MoveableGroup> moveableGroups = Lists.newArrayList();
	private List<PortalPuzzle> portalPuzzles = Lists.newArrayList();
	private List<SafeSpace> safeSpaces = Lists.newArrayList();
	private List<Race> races = Lists.newArrayList();

	private MapModification mapModification = new MapModificationImpl();
	
	public CosmodogMap(CustomTiledMap customTiledMap) {
		this.setCustomTiledMap(customTiledMap);
	}
	
	public PieceCache getMapPieces() {
		return mapPieces;
	}

	private List<Piece> getSpecificPieces(Predicate<Piece> predicate) {

        return getMapPieces().piecesInArea(
                predicate,
                0,
                0,
                400,
                400
        );

	}

	private Map<Position, List<Piece>> getSpecificPiecesByPosition(Predicate<Piece> predicate) {
		List<Piece> pieces = getMapPieces().piecesInArea(
				predicate,
				0,
				0,
				400,
				400
		);

		Map<Position, List<Piece>> retVal = new HashMap<>();

		for (Piece piece : pieces) {
			retVal.computeIfAbsent(piece.getPosition(), (position) -> new ArrayList<>());
			retVal.get(piece.getPosition()).add(piece);
		}

		return retVal;

	}

	public List<Piece> getInfobits() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobit);
		return getSpecificPieces(predicate);
	}

	public Map<Position, List<Piece>> getInfobitsByPosition() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobit);
		return getSpecificPiecesByPosition(predicate);
	}

	public List<Piece> getInfobytes() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobyte);
		return getSpecificPieces(predicate);
	}

	public Map<Position, List<Piece>> getInfobytesByPosition() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobyte);
		return getSpecificPiecesByPosition(predicate);
	}

	public List<Piece> getInfobanks() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobank);
		return getSpecificPieces(predicate);
	}

	public Map<Position, List<Piece>> getInfobanksByPosition() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.infobank);
		return getSpecificPiecesByPosition(predicate);
	}

	public List<Piece> getSupplies() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.supplies);
		return getSpecificPieces(predicate);
	}

	public Map<Position, List<Piece>> getSuppliesByPosition() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.supplies);
		return getSpecificPiecesByPosition(predicate);
	}

	public List<Piece> getMedkits() {
		Predicate<Piece> predicate = piece -> {

			boolean retVal = false;

			if (piece instanceof CollectibleGoodie goodie) {
				boolean goodieIsFirstAidKit = goodie.getGoodieType() == CollectibleGoodie.GoodieType.firstaidkit;
				boolean goodieIsMedipack = goodie.getGoodieType() == CollectibleGoodie.GoodieType.medipack;
				if (goodieIsFirstAidKit || goodieIsMedipack) {
					retVal = true;
				}
			}
			return retVal;
		};

		return getSpecificPieces(predicate);
	}

	public Map<Position, List<Piece>> getMedkitsByPosition() {
		Predicate<Piece> predicate = piece -> {

			boolean retVal = false;

			if (piece instanceof CollectibleGoodie goodie) {
                boolean goodieIsFirstAidKit = goodie.getGoodieType() == CollectibleGoodie.GoodieType.firstaidkit;
				boolean goodieIsMedipack = goodie.getGoodieType() == CollectibleGoodie.GoodieType.medipack;
				if (goodieIsFirstAidKit || goodieIsMedipack) {
					retVal = true;
				}
			}
			return retVal;
		};

		return getSpecificPiecesByPosition(predicate);
	}

	public List<Piece> getInsights() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.insight);
		return getSpecificPieces(predicate);
	}

	public Map<Position, List<Piece>> getInsightsByPosition() {
		Predicate<Piece> predicate = piece -> piece instanceof CollectibleGoodie && ((CollectibleGoodie)piece).getGoodieType().equals(CollectibleGoodie.GoodieType.insight);
		return getSpecificPiecesByPosition(predicate);
	}

	public List<Piece> visibleMapPieces(Position ref, int width, int height, int grace) {
		Map<Position, List<Piece>> map = visibleMapPiecesByPosition(ref, width, height, grace);
		return map.values().stream().flatMap(Collection::stream).toList();
	}

	public Map<Position, List<Piece>> visibleMapPiecesByPosition(Position ref, int width, int height, int grace) {

		List<Piece> pieces = getMapPieces()
				.piecesInArea(piece -> true, ref.getX() - grace, ref.getY() - grace, width + 2 * grace, height + 2 * grace)
				.stream()
				.toList();

		Map<Position, List<Piece>> retVal = new HashMap<>();

		for (Piece piece : pieces) {
			retVal.computeIfAbsent(piece.getPosition(), (position) -> new ArrayList<>());
			retVal.get(piece.getPosition()).add(piece);
		}

		return retVal;

	}
	
	public Piece pieceAtTile(Position position) {
		List<Piece> pieces = mapPieces.piecesInArea(e -> true, position.getX(), position.getY(), 1, 1);
		return pieces.isEmpty() ? null : pieces.getFirst();
	}
	
	public Enemy enemyAtTile(Position position) {
		List<Piece> pieces = mapPieces.piecesInArea(e -> e instanceof Enemy, position.getX(), position.getY(), 1, 1);
		return pieces.isEmpty() ? null : (Enemy)pieces.getFirst();
	}

	
	public Set<Piece> visibleEffectPieces(Position position, int width, int height, int grace) {
		return new HashSet<>(getMapPieces().piecesInArea(piece -> piece instanceof Effect, position.getX(), position.getY(), width + grace, height + grace));
	}

	public Set<DynamicPiece> dynamicPiecesAtPosition(Position position) {
		return getMapPieces().piecesInArea(piece -> piece instanceof DynamicPiece, position.getX(), position.getY(), 1, 1).stream().map(e -> (DynamicPiece)e).collect(Collectors.toSet());
	}

	public MapDescriptor getMapDescriptor() {
		return mapDescriptor;
	}

	public void setMapDescriptor(MapDescriptor mapDescriptor) {
		this.mapDescriptor = mapDescriptor;
	}

	public Set<Enemy> getEnemiesInRange() {
		return PlayerMovementCache.getInstance().getEnemiesInRange();
	}

	public Set<Enemy> allEnemies() {
		return mapPieces
				.piecesOverall(PiecePredicates.ENEMY)
				.stream()
				.map(e -> (Enemy)e)
				.collect(Collectors.toSet());
	}

	public Set<Enemy> nearbyEnemies(Position ref, int maxDistance) {

		return mapPieces
				.piecesInArea(piece -> piece instanceof Enemy, ref.getX() - maxDistance, ref.getY() - maxDistance, maxDistance * 2, maxDistance * 2)
				.stream()
				.map(e -> (Enemy)e)
				.collect(Collectors.toSet());

	}
	
	public Set<Enemy> visibleEnemies(Position ref, int width, int height, int grace) {

		return mapPieces
				.piecesInArea(piece -> piece instanceof Enemy, ref.getX() - grace, ref.getY() - grace, width + 2 * grace, height + 2 * grace)
				.stream()
				.map(e -> (Enemy)e)
				.collect(Collectors.toSet());

	}

	public Set<Platform> getPlatforms() {
		//Platform is null when player is sitting in it, because then it counts as an inventory item, not a piece.
		return getMapPieces()
				.piecesOverall(PiecePredicates.PLATFORM)
				.stream()
				.map(e -> (Platform)e)
				.collect(Collectors.toSet());
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

	public Optional<DynamicPiece> dynamicPieceAtPosition(Class<? extends DynamicPiece> clazz, Position position) {
		return mapPieces.piecesAtPosition(e -> clazz
				.isAssignableFrom(e.getClass()), position.getX(), position.getY())
				.stream()
				.map(e -> (DynamicPiece)e)
				.findFirst();
	}


	public Optional<SwitchableHolder> switchableHolderAtPosition(Position position) {

		return mapPieces
				.piecesAtPosition(e -> SwitchableHolder.class.isAssignableFrom(e.getClass()), position.getX(), position.getY())
				.stream()
				.map(e -> (SwitchableHolder)e)
				.findFirst();
	}

	public Optional<Switchable> switchableAtPosition(Position position) {

		return mapPieces
				.piecesAtPosition(e -> Switchable.class.isAssignableFrom(e.getClass()), position.getX(), position.getY())
				.stream()
				.map(e -> (Switchable)e)
				.findFirst();
	}

	public Optional<MoveableDynamicPiece> moveableAtPosition(Position position) {
		return mapPieces
				.piecesAtPosition(e -> MoveableDynamicPiece.class.isAssignableFrom(e.getClass()), position.getX(), position.getY())
				.stream()
				.map(e -> (MoveableDynamicPiece)e)
				.findFirst();
	}

	public Optional<ActivatableHolder> activatableHolderAtPosition(Position position) {

		return mapPieces
				.piecesAtPosition(e -> ActivatableHolder.class.isAssignableFrom(e.getClass()), position.getX(), position.getY())
				.stream()
				.map(e -> (ActivatableHolder)e)
				.findFirst();
	}

	public Optional<Activatable> activatableAtPosition(Position position) {

		return mapPieces
				.piecesAtPosition(e -> Activatable.class.isAssignableFrom(e.getClass()), position.getX(), position.getY())
				.stream()
				.map(e -> (Activatable)e)
				.findFirst();
	}





	public List<MoveableGroup> getMoveableGroups() {
		return moveableGroups;
	}

	public List<PortalPuzzle> getPortalPuzzles() {
		return portalPuzzles;
	}

	public List<SafeSpace> getSafeSpaces() {
		return safeSpaces;
	}

	public List<Race> getRaces() {
		return races;
	}
}
