package antonafanasjew.cosmodog;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import antonafanasjew.cosmodog.caching.PieceCache;
import antonafanasjew.cosmodog.model.actors.NpcActor;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import antonafanasjew.cosmodog.model.Collectible;
import antonafanasjew.cosmodog.model.Collectible.CollectibleType;
import antonafanasjew.cosmodog.model.CollectibleComposed;
import antonafanasjew.cosmodog.model.CollectibleGoodie;
import antonafanasjew.cosmodog.model.CollectibleGoodie.GoodieType;
import antonafanasjew.cosmodog.model.CollectibleTool;
import antonafanasjew.cosmodog.model.CollectibleWeapon;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.inventory.GoodieInventoryItem;
import antonafanasjew.cosmodog.model.inventory.InventoryItem;
import antonafanasjew.cosmodog.topology.Position;

public class CosmodogMapStatisticsProvider {

	private static CosmodogMapStatisticsProvider instance = new CosmodogMapStatisticsProvider();

	public static CosmodogMapStatisticsProvider getInstance() {
		return instance;
	}
	
	public Function<Piece, Integer> pieceValueInInfobitsFunction() {
		return e -> {

			List<GoodieType> goodieTypes = Lists.newArrayList();

			if (e instanceof Collectible collectible) {
                if (collectible.getCollectibleType() == CollectibleType.GOODIE) {
					CollectibleGoodie goodie = (CollectibleGoodie) collectible;
					if (goodie.getGoodieType() == GoodieType.infobit || goodie.getGoodieType() == GoodieType.infobyte || goodie.getGoodieType() == GoodieType.infobank) {
						goodieTypes.add(goodie.getGoodieType());
					}
				} else if (collectible.getCollectibleType() == CollectibleType.COMPOSED) {
					CollectibleComposed composed = (CollectibleComposed) collectible;
					List<Collectible> elements = composed.getElements();
					for (Collectible element : elements) {
						CollectibleGoodie goodie = (CollectibleGoodie) element;
						if (goodie.getGoodieType() == GoodieType.infobit || goodie.getGoodieType() == GoodieType.infobyte || goodie.getGoodieType() == GoodieType.infobank) {
							goodieTypes.add(goodie.getGoodieType());
						}
					}
				}
			} else if (e instanceof Enemy enemy) {
                InventoryItem inventoryItem = enemy.getInventoryItem();
				if (inventoryItem instanceof GoodieInventoryItem goodieInventoryItem) {
                    if (goodieInventoryItem.getGoodieType() == GoodieType.infobit || goodieInventoryItem.getGoodieType() == GoodieType.infobyte || goodieInventoryItem.getGoodieType() == GoodieType.infobank) {
						goodieTypes.add(goodieInventoryItem.getGoodieType());
					}
				}
			}

            return goodieTypes.stream().mapToInt(el -> {
				if (el == GoodieType.infobit) {
					return 1;
				} else if (el == GoodieType.infobyte) {
					return 5;
				} else if (el == GoodieType.infobank) {
					return 25;
				} else {
					return 0;
				}
			}).sum();

		};
	}

	/**
	 * Returns the number of pieces per map piece. The predicate defines which
	 * pieces to count. Positions are representing chart pieces in the map (not
	 * tiles!).
	 */
	public Map<Position, Integer> infobitValuePerChartPiece(PieceCache mapPieces, Set<Enemy> enemies, int chartPieceWidthInTiles, int chartPieceHeightInTiles) {

		Function<Piece, Integer> valueFunction = pieceValueInInfobitsFunction();

		Map<Position, Integer> pieceValues = mapPieces.piecesOverall(e -> true).stream().filter(e -> e instanceof Collectible || e instanceof NpcActor).collect(Collectors.toMap(Piece::getPosition, valueFunction));

		Map<Position, Integer> mapPieceValuePerChartPiece = Maps.newHashMap();
		for (Position position : pieceValues.keySet()) {
			Integer pieceValue = pieceValues.get(position);
			int chartPiecePositionX = (int) position.getX() / chartPieceWidthInTiles;
			int chartPiecePositionY = (int) position.getY() / chartPieceHeightInTiles;
			Position chartPiecePosition = Position.fromCoordinates(chartPiecePositionX, chartPiecePositionY, position.getMapType());
			if (mapPieceValuePerChartPiece.get(chartPiecePosition) == null) {
				mapPieceValuePerChartPiece.put(chartPiecePosition, 0);
			}
			int currentCount = mapPieceValuePerChartPiece.get(chartPiecePosition);
			mapPieceValuePerChartPiece.put(chartPiecePosition, currentCount + pieceValue);
		}

		return mapPieceValuePerChartPiece;

	}

	public Map<Position, Collectible> charts(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.chart;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> soulEssences(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.soulessence;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> supplies(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.supplies;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> armors(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.armor;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> medkits(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.medipack;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> insights(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.insight;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> bottles(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.bottle;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> foodCompartments(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.foodcompartment;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}

	public Map<Position, Collectible> fuelTanks(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.fueltank;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> softwares(Map<Position, Piece> pieces) {
		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.software;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> weapons(Map<Position, Piece> pieces) {

		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean weapon = e instanceof CollectibleWeapon;
			if (weapon) {
				return true;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> tools(Map<Position, Piece> pieces) {

		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean tool = e instanceof CollectibleTool;
			if (tool) {
				return true;
			}
			return false;
		};
		return specificCollectibles(pieces, importantCollectiblePredicate);
	}
	
	public Map<Position, Collectible> importantGoodiesAndTools(Map<Position, Piece> pieces) {

		Predicate<Collectible> importantCollectiblePredicate = e -> {
			boolean goodie = e instanceof CollectibleGoodie;
			if (goodie) {
				GoodieType goodieType = ((CollectibleGoodie) e).getGoodieType();
				return goodieType == GoodieType.armor || goodieType == GoodieType.bottle || goodieType == GoodieType.chart || goodieType == GoodieType.cognition || goodieType == GoodieType.foodcompartment || goodieType == GoodieType.insight || goodieType == GoodieType.software || goodieType == GoodieType.soulessence;
			}
			boolean tool = e instanceof CollectibleTool;
			if (tool) {
				return true;
			}
			boolean weapon = e instanceof CollectibleWeapon;
			if (weapon) {
				return true;
			}
			return false;
		};
		
		return specificCollectibles(pieces, importantCollectiblePredicate);

	}

	private Map<Position, Collectible> specificCollectibles(Map<Position, Piece> pieces, Predicate<Collectible> predicate) {
		return pieces
				.entrySet()
				.stream()
				.filter(e -> e.getValue() instanceof Collectible)
				.filter(e -> predicate.test((Collectible) e.getValue()))
				.collect(Collectors.toMap(e -> e.getKey(), e -> (Collectible) e.getValue())
		);
	}

}
