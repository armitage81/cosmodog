package antonafanasjew.cosmodog.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.caching.PiecePredicates;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.structures.PortalPuzzle;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.structures.SafeSpace;
import antonafanasjew.cosmodog.util.*;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.domains.UnitType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.structures.MoveableGroup;
import antonafanasjew.cosmodog.tiledmap.TiledObject;
import antonafanasjew.cosmodog.topology.Position;

/**
 * This class holds values that need to be calculated only once per player movement.
 * It implements the movement listener to be updated when the player has moved.
 * Its values can be retrieved many times per turn instead of being calculated again and again.
 * Singleton. 
 */
public class PlayerMovementCache implements Serializable {

	public static final int CACHE_RANGE = 50;
	
	@Serial
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

	private Set<Enemy> enemiesInRange = Sets.newHashSet();
	private Map<Enemy, Set<TiledObject>> enemiesInRangeWithRoofsOverThem = Maps.newHashMap();
	
	/*
	 * When the player is in the moveable group region (Sokoban riddle), a hint will be shown how to reset the riddle.
	 * Instead of checking every time if the player is in such a region, we will use the value from the cache,
	 * which will be updated once per turn.
	 */
	private MoveableGroup activeMoveableGroup;

	private PortalPuzzle activePortalPuzzle;

	private SafeSpace activeSafeSpace;

	private Race activeRace;

	private Set<Position> positionsCoveredByPlatforms;

	public Piece getClosestSupply() {
		return closestSupply;
	}
	
	public Piece getClosestMedkit() {
		return closestMedkit;
	}
	
	public Piece getClosestPieceInterestingForDebugging() {
		return closestPieceInterestingForDebugging;
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

	public PortalPuzzle getActivePortalPuzzle() {
		return activePortalPuzzle;
	}

	public SafeSpace getActiveSafeSpace() {
		return activeSafeSpace;
	}

	public Race getActiveRace() {
		return activeRace;
	}

	public void setActivePortalPuzzle(PortalPuzzle activePortalPuzzle) {
		this.activePortalPuzzle = activePortalPuzzle;
	}

	public void setActiveSafeSpace(SafeSpace activeSafeSpace) {
		this.activeSafeSpace = activeSafeSpace;
	}

	public void setActiveRace(Race activeRace) {
		this.activeRace = activeRace;
	}

	public Set<Position> getPositionsCoveredByPlatforms() {
		return positionsCoveredByPlatforms;
	}

	public void update(Actor actor, Position position1, Position position2) {
		ApplicationContext applicationContext = ApplicationContext.instance();
		recalculateClosestSupplyAndMedkitPosition(actor, position1, position2, applicationContext);
		recalculateclosestPieceInterestingForDebugging(actor, position1, position2, applicationContext);
		recalculateWhetherPlayerIsOnPlatform(actor);
		recalculateRoofsOverPlayer();
		recalculateRoofRemovalBlockerRegions(actor);
		recalculateInfobitsInGame();
		recalculateEnemiesInRange();
		recalculateRoofsOverEnemiesInRange();
		recalculateActiveMoveableGroup();
		recalculateActivePortalPuzzle();
		recalculateActiveSafeSpace();
		recalculateActiveRace();
		recalculatePositionsCoveredByPlatforms();
	}


	private void recalculateEnemiesInRange() {
		Player player = ApplicationContextUtils.getPlayer();
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();

		enemiesInRange.clear();

		Set<Enemy> enemies = map.allEnemies();
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

	private void recalculateRoofRemovalBlockerRegions(Actor actor) {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		roofRemovalBlockerRegionsOverPlayer.clear();
		roofRemovalBlockerRegionsOverPlayer.addAll(RegionUtils.roofRemovalBlockersOverPiece(actor, map));

	}

	private void recalculateClosestSupplyAndMedkitPosition(Actor actor, Position position1, Position position2, ApplicationContext applicationContext) {
		CosmodogMap map = applicationContext.getCosmodog().getCosmodogGame().mapOfPlayerLocation();
		Collection<Piece> supplies = map.getSupplies();
		Collection<Piece> medkits = map.getMedkits();


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
		Collection<Piece> pieces = map.getMapPieces().piecesOverall(PiecePredicates.GOODIE);

		Collection<Enemy> enemies = map.allEnemies().stream().filter(enemy -> enemy.getUnitType() != UnitType.ARTILLERY).collect(Collectors.toSet());

		List<Piece> piecesSortedByProximity = Lists.newArrayList(pieces);
		piecesSortedByProximity.addAll(enemies);
		piecesSortedByProximity.sort((o1, o2) -> {
            Position o1Pos = o1.getPosition();
            Position o2Pos = o2.getPosition();
            Position playerPos = actor.getPosition();

            float distance1 = CosmodogMapUtils.distanceBetweenPositions(o1Pos, playerPos);
            float distance2 = CosmodogMapUtils.distanceBetweenPositions(o2Pos, playerPos);

            return Float.compare(distance1, distance2);
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

			Set<Enemy> enemies = map.allEnemies();
			for (Enemy enemy : enemies) {
				InventoryItem item = enemy.getInventoryItem();
				if (item instanceof GoodieInventoryItem) {
					GoodieInventoryItem goodie = (GoodieInventoryItem) item;
					CollectibleGoodie.GoodieType goodieType = goodie.getGoodieType();
					if (goodieType == CollectibleGoodie.GoodieType.infobit) {
						inInventories += 1;
					}
					if (goodieType == CollectibleGoodie.GoodieType.infobyte) {
						inInventories += 5;
					}
					if (goodieType == CollectibleGoodie.GoodieType.infobank) {
						inInventories += 25;
					}
				}
			}

			numberInfobitsInGame += (inInventories + noInfobits + (5 * noInfoBytes) + (25 * noInfobanks));

		}
	}

	private void recalculateActiveMoveableGroup() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		MoveableGroup moveableGroupAroundPlayer = null;
		List<MoveableGroup> moveableGroups = map.getMoveableGroups();
		for (MoveableGroup moveableGroup : moveableGroups) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), moveableGroup.getRegion())) {
				moveableGroupAroundPlayer = moveableGroup;
				break;
			}
		}
		setActiveMoveableGroup(moveableGroupAroundPlayer);
	}

	private void recalculateActivePortalPuzzle() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		PortalPuzzle portalPuzzleAroundPlayer = null;
		List<PortalPuzzle> portalPuzzles = map.getPortalPuzzles();
		for (PortalPuzzle portalPuzzle : portalPuzzles) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), portalPuzzle.getRegion())) {
				portalPuzzleAroundPlayer = portalPuzzle;
				break;
			}
		}
		setActivePortalPuzzle(portalPuzzleAroundPlayer);
	}

	private void recalculateActiveSafeSpace() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		SafeSpace safeSpaceAroundPlayer = null;
		List<SafeSpace> safeSpaces = map.getSafeSpaces();
		for (SafeSpace safeSpace : safeSpaces) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), safeSpace.getRegion())) {
				safeSpaceAroundPlayer = safeSpace;
				break;
			}
		}
		setActiveSafeSpace(safeSpaceAroundPlayer);
	}

	private void recalculateActiveRace() {
		CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
		Player player = ApplicationContextUtils.getPlayer();
		Race raceAroundPlayer = null;
		List<Race> races = map.getRaces();
		for (Race race : races) {
			if (RegionUtils.pieceInRegion(player, map.getMapType(), race.getRegion())) {
				raceAroundPlayer = race;
				break;
			}
		}
		setActiveRace(raceAroundPlayer);
	}

	private void recalculatePositionsCoveredByPlatforms() {
		positionsCoveredByPlatforms = CosmodogMapUtils.positionsCoveredByPlatforms();
	}



}

