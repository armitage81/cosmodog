package antonafanasjew.cosmodog.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.domains.MapType;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.globals.ObjectGroups;
import antonafanasjew.cosmodog.listener.movement.MovementListenerAdapter;
import antonafanasjew.cosmodog.model.Collectible.CollectibleType;
import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.ChartInventoryItem;
import antonafanasjew.cosmodog.model.inventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import antonafanasjew.cosmodog.util.PiecesUtils;
import antonafanasjew.cosmodog.util.RegionUtils;

/**
 * This class holds values that need to be calculated only once per player movement.
 * It implements the movement listener to be updated when the player has moved.
 * Its values can be retrieved many times per turn instead of being calculated again and again.
 * Singleton. 
 */
public class PlayerMovementCache extends MovementListenerAdapter {

	public static final int CACHE_RANGE = 50;
	
	private static final long serialVersionUID = 3422584256506346853L;
	
	private static PlayerMovementCache instance = null;
	
	public static PlayerMovementCache getInstance() {
		if (instance == null) {
			instance = new PlayerMovementCache();
		}
		return instance;
	}
	
	private PlayerMovementCache() {

	}
	
	private Piece closestPieceInterestingForDebugging;
	
	private Piece closestSupply;
	private Piece closestMedkit;
	
	private boolean playerOnPlatform;
	
	private Set<TiledObject> roofRegionsOverPlayer = Sets.newHashSet();
	private Set<TiledObject> roofRemovalBlockerRegionsOverPlayer = Sets.newHashSet();
	
	private int numberInfobitsInGame;
	

	private Map<Position, DynamicPiece> dynamicPieces = Maps.newHashMap();
	private Multimap<Class<?>, DynamicPiece> visibleDynamicPieces = ArrayListMultimap.create();
	
	private Set<Enemy> enemiesInRange = Sets.newHashSet();
	private Map<Enemy, Set<TiledObject>> enemiesInRangeWithRoofsOverThem = Maps.newHashMap();
	
	/*
	 * When the player is in the moveable group region (Sokoban riddle), a hint will be shown how to reset the riddle.
	 * Instead of checking every time if the player is in such a region, we will use the value from the cache,
	 * which will be updated once per turn.
	 */
	private MoveableGroup activeMoveableGroup;
	
	@Override
	public void afterMovement(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		recalculateClosestSupplyAndMedkitPosition(actor, position1, position2, applicationContext);
		recalculateclosestPieceInterestingForDebugging(actor, position1, position2, applicationContext);
		recalculateWhetherPlayerIsOnPlatform(actor);
		recalculateRoofsOverPlayer();
		recalculateRoofRemovalBlockerRegions(actor);
		recalculateDynamicPieces();
		recalculateVisibleDynamicPieces();
		recalculateInfobitsInGame();
		recalculateEnemiesInRange();
		recalculateRoofsOverEnemiesInRange();
		recalculateActiveMoveableGroup();
	}
	
	@Override
	public void afterFight(Actor actor, ApplicationContext applicationContext) {
		afterMovement(actor, actor.getPosition(), actor.getPosition(), applicationContext);
	}

	@Override
	public void afterTeleportation(Actor actor, ApplicationContext applicationContext) {
		afterMovement(actor, actor.getPosition(), actor.getPosition(), applicationContext);
	}

	@Override
	public void afterRespawn(Actor actor, ApplicationContext applicationContext) {
		afterMovement(actor, actor.getPosition(), actor.getPosition(), applicationContext);
	}

	@Override
	public void afterWaiting(Actor actor, ApplicationContext applicationContext) {
		afterMovement(actor, actor.getPosition(), actor.getPosition(), applicationContext);
	}

	private void recalculateEnemiesInRange() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		enemiesInRange.clear();
		
		Set<Enemy> enemies = map.getEnemies();
		for (Enemy enemy : enemies) {
			float distance = PiecesUtils.distanceBetweenPieces(player, enemy);
			if (distance < CACHE_RANGE) {
				enemiesInRange.add(enemy);
			}
		}
	}

	private void recalculateRoofsOverPlayer() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		roofRegionsOverPlayer.clear();
		roofRegionsOverPlayer.addAll(RegionUtils.roofsOverPiece(player, map));
	}

	private void recalculateRoofsOverEnemiesInRange() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		for (Enemy enemy : enemiesInRange) {
			Set<TiledObject> roofs = Sets.newHashSet();
			roofs = RegionUtils.roofsOverPiece(enemy, map);
			Set<TiledObject> oldRoofs = enemiesInRangeWithRoofsOverThem.get(enemy);
			if (oldRoofs == null) {
				if (roofs.isEmpty() == false) {
					enemiesInRangeWithRoofsOverThem.put(enemy, roofs);
				}
			} else {
				if (roofs.isEmpty() == false) {
					enemiesInRangeWithRoofsOverThem.get(enemy).clear();
					enemiesInRangeWithRoofsOverThem.get(enemy).addAll(roofs);
				} else {
					enemiesInRangeWithRoofsOverThem.remove(enemy);
				}
			}
		}
	}
	
	private void recalculateDynamicPieces() {
	
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		
		dynamicPieces.clear();
		for (Class<?> key : map.getDynamicPieces().keySet()) {
			Collection<DynamicPiece> piecesForKey = map.getDynamicPieces().get(key);
			if (piecesForKey != null) {
                for (DynamicPiece piece : piecesForKey) {
                    Position position = piece.getPosition();
                    dynamicPieces.put(position, piece);
                }
			}
		}
	}

	private void recalculateVisibleDynamicPieces() {
		
		//Unfortunately, this cache does not make sense, as it focuses at the pieces next to the player,
		//but in case of a camera flight over the field, every piece should be visible.
		//Commented the logic and showing all pieces instead to avoid the problem.
		//This has a bad impact on performance and should be tackled.
		
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		visibleDynamicPieces = map.getDynamicPieces();

//		CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
//		Cam cam = game.getCam();
//		int tileWidth = map.getTileWidth();
//		int tileHeight = map.getTileHeight();
//		
//		int scaledTileWidth = (int) (tileWidth * cam.getZoomFactor());
//		int scaledTileHeight = (int) (tileHeight * cam.getZoomFactor());
//
//		int camX = (int) cam.viewCopy().x();
//		int camY = (int) cam.viewCopy().y();
//		
//		
//		int tileNoX = camX / scaledTileWidth;
//		int tileNoY = camY / scaledTileHeight;
//		
//		int tilesW = (int) (cam.viewCopy().width()) / scaledTileWidth + 2;
//		int tilesH = (int) (cam.viewCopy().height()) / scaledTileHeight + 2;
//		
//		
//		visibleDynamicPieces.clear();
//		for (Class<?> key : map.getDynamicPieces().keySet()) {
//			Collection<DynamicPiece> piecesForKey = map.getDynamicPieces().get(key);
//			if (piecesForKey != null) {
//				Iterator<DynamicPiece> it = piecesForKey.iterator();
//				while (it.hasNext()) {
//					DynamicPiece piece = it.next();
//					if (piece.getPositionX() >= (tileNoX - 2) && piece.getPositionX() < (tileNoX + tilesW + 2)) {
//						if (piece.getPositionY() >= (tileNoY - 2) && piece.getPositionY() < (tileNoY + tilesH + 2)) {
//							visibleDynamicPieces.put(key, piece);
//						}
//					}
//				}
//			}
//		}
	}
	
	private void recalculateRoofRemovalBlockerRegions(Actor actor) {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		roofRemovalBlockerRegionsOverPlayer.clear();
		roofRemovalBlockerRegionsOverPlayer.addAll(RegionUtils.roofRemovalBlockersOverPiece(actor, map));
		
	}

	public Piece getClosestSupply() {
		return closestSupply;
	}
	
	public Piece getClosestMedkit() {
		return closestMedkit;
	}
	
	public Piece getClosestPieceInterestingForDebugging() {
		return closestPieceInterestingForDebugging;
	}

	private void recalculateClosestSupplyAndMedkitPosition(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		CosmodogMap map = applicationContext.getCosmodog().getCosmodogGame().mapOfPlayerLocation();
		Collection<Piece> supplies = map.getSupplies().values();
		Collection<Piece> medkits = map.getMedkits().values();
		
		
		Comparator<Piece> proximityComparator = new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				Position o1Pos = o1.getPosition();
				Position o2Pos = o2.getPosition();
				Position playerPos = actor.getPosition();
				
				float distance1 = CosmodogMapUtils.distanceBetweenPositions(o1Pos, playerPos);
				float distance2 = CosmodogMapUtils.distanceBetweenPositions(o2Pos, playerPos);
				
				return Float.compare(distance1, distance2);
			}

		};
		
		List<Piece> suppliesSortedByProximity = Lists.newArrayList(supplies);
		Collections.sort(suppliesSortedByProximity, proximityComparator);
		
		List<Piece> medkitsSortedByProximity = Lists.newArrayList(medkits);
		Collections.sort(medkitsSortedByProximity, proximityComparator);
		
		closestSupply = suppliesSortedByProximity.isEmpty() ? null : suppliesSortedByProximity.get(0);
		closestMedkit = medkitsSortedByProximity.isEmpty() ? null : medkitsSortedByProximity.get(0);
		
	}

	
	private void recalculateclosestPieceInterestingForDebugging(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		CosmodogMap map = applicationContext.getCosmodog().getCosmodogGame().mapOfPlayerLocation();
		Collection<Piece> pieces = map.getMapPieces().values();
		
		pieces = Lists.newArrayList(Iterables.filter(pieces, new Predicate<Piece>() {

			@Override
			public boolean apply(Piece piece) {
								
				if (!(piece instanceof Collectible)) {
					return false;
				}
				
				
				Collectible coll = (Collectible)piece;
				if (coll.getCollectibleType() == CollectibleType.GOODIE) {
					CollectibleGoodie goodie = (CollectibleGoodie)coll;
					if (goodie.getGoodieType() == GoodieType.cognition) {
						return false;
					}
				}
				
				return true;
				
			}
			
			
		}));
		
		Collection<Enemy> enemies = map.getEnemies();
		
		enemies = Lists.newArrayList(Iterables.filter(enemies, new Predicate<Enemy>() {

			@Override
			public boolean apply(Enemy enemy) {
								
				if (enemy.getUnitType() == UnitType.ARTILLERY) {
					return false;
				}
				
				return true;
				
			}
			
			
		}));
		
		
		List<Piece> piecesSortedByProximity = Lists.newArrayList(pieces);
		piecesSortedByProximity.addAll(enemies);
		Collections.sort(piecesSortedByProximity, new Comparator<Piece>() {

			@Override
			public int compare(Piece o1, Piece o2) {
				Position o1Pos = o1.getPosition();
				Position o2Pos = o2.getPosition();
				Position playerPos = actor.getPosition();
				
				float distance1 = CosmodogMapUtils.distanceBetweenPositions(o1Pos, playerPos);
				float distance2 = CosmodogMapUtils.distanceBetweenPositions(o2Pos, playerPos);
				
				return Float.compare(distance1, distance2);
			}

		});
		
		closestPieceInterestingForDebugging = piecesSortedByProximity.isEmpty() ? null : piecesSortedByProximity.get(0);
		
	}
	
	
	
	private void recalculateWhetherPlayerIsOnPlatform(Actor actor) {
		setPlayerOnPlatform(CosmodogMapUtils.isTileOnPlatform(actor.getPosition()));
	}

	private void recalculateInfobitsInGame() {

		numberInfobitsInGame = 0;

		for (MapType mapType : MapType.values()) {

			CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(mapType);

			int noInfobits = map.getInfobits().size();
			int noInfoBytes = map.getInfobytes().size();
			int noInfobanks = map.getInfobanks().size();

			int inInventories = 0;

			Set<Enemy> enemies = map.getEnemies();
			for (Enemy enemy : enemies) {
				InventoryItem item = enemy.getInventoryItem();
				if (item instanceof GoodieInventoryItem) {
					GoodieInventoryItem goodie = (GoodieInventoryItem) item;
					GoodieType goodieType = goodie.getGoodieType();
					if (goodieType == GoodieType.infobit) {
						inInventories += 1;
					}
					if (goodieType == GoodieType.infobyte) {
						inInventories += 5;
					}
					if (goodieType == GoodieType.infobank) {
						inInventories += 25;
					}
				}
			}

			numberInfobitsInGame += (inInventories + noInfobits + (5 * noInfoBytes) + (25 * noInfobanks));

		}
	}
	
	public boolean isPlayerOnPlatform() {
		return playerOnPlatform;
	}

	public void setPlayerOnPlatform(boolean playerOnPlatform) {
		this.playerOnPlatform = playerOnPlatform;
	}

	public Set<TiledObject> getRoofRegionsOverPlayer() {
		return roofRegionsOverPlayer;
	}
	
	public Map<Enemy, Set<TiledObject>> getEnemiesInRangeWithRoofsOverThem() {
		return enemiesInRangeWithRoofsOverThem;
	}
	
	public Set<TiledObject> getRoofRemovalBlockerRegionsOverPlayer() {
		return roofRemovalBlockerRegionsOverPlayer;
	}

	public Map<Position, DynamicPiece> getDynamicPieces() {
		return dynamicPieces;
	}
	
	public Multimap<Class<?>, DynamicPiece> getVisibleDynamicPieces() {
		return visibleDynamicPieces;
	}
	
	public int getNumberInfobitsInGame() {
		return numberInfobitsInGame;
	}
	
	public Set<Enemy> getEnemiesInRange() {
		return enemiesInRange;
	}
	
	public MoveableGroup getActiveMoveableGroup() {
		return activeMoveableGroup;
	}
	
	public void setActiveMoveableGroup(MoveableGroup activeMoveableGroup) {
		this.activeMoveableGroup = activeMoveableGroup;
	}
	
	private void recalculateActiveMoveableGroup() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		MoveableGroup moveableGroupAroundPlayer = null;
		List<MoveableGroup> moveableGroups = map.getMoveableGroups();
		for (MoveableGroup moveableGroup : moveableGroups) {
			if (RegionUtils.pieceInRegion(player, moveableGroup.getRegion(), map.getTileWidth(), map.getTileHeight())) {
				moveableGroupAroundPlayer = moveableGroup;
				break;
			}
		}
		setActiveMoveableGroup(moveableGroupAroundPlayer);
	}
}

