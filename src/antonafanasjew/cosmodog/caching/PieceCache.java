package antonafanasjew.cosmodog.caching;


import antonafanasjew.cosmodog.model.MapDescriptor;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Platform;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.CosmodogMapUtils;
import profiling.ProfilerUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PieceCache implements Serializable {

    private MapDescriptor mapDescriptor;
    private final int sectorWidth;
    private final int sectorHeight;

    public PieceCache(MapDescriptor mapDescriptor, int sectorWidth, int sectorHeight) {
        this.mapDescriptor = mapDescriptor;
        this.sectorWidth = sectorWidth;
        this.sectorHeight = sectorHeight;
    }

    private final Map<Position, List<Piece>> pieces = new HashMap<>();

    private final Map<Integer, List<Piece>> piecesByPredicates = new HashMap<>();

    private final Map<Platform, Set<Position>> positionsCoveredByPlatforms = new HashMap<>();

    public int getSectorWidth() {
        return sectorWidth;
    }

    public int getSectorHeight() {
        return sectorHeight;
    }

    private List<Position> sectorPositionsCrossingArea(float x, float y, float width, float height) {
        List<Position> relatedSectorPositions = new ArrayList<>();
        for (Position sectorPosition : pieces.keySet()) {
            if (sectorPosition.getX() * sectorWidth + sectorWidth < x) { //we check the right side of the sector.
                continue;
            }
            if (sectorPosition.getY() * sectorHeight + sectorHeight < y) { //we check the bottom side of the sector.
                continue;
            }
            if (sectorPosition.getX() * sectorWidth >= x + width) {
                continue;
            }
            if (sectorPosition.getY() * sectorHeight >= y + height) {
                continue;
            }
            relatedSectorPositions.add(sectorPosition);
        }
        return relatedSectorPositions;
    }

    public List<Piece> allPieces() {
        List<Piece> retVal = new ArrayList<>();
        Set<Position> sectorPositions = pieces.keySet();
        for (Position sectorPosition : sectorPositions) {
            retVal.addAll(pieces.get(sectorPosition).stream().toList());
        }
        return retVal;
    }

    public List<Piece> piecesOverall(Predicate<Piece> predicate) {
        List<Piece> retVal = new ArrayList<>();

        ProfilerUtils.runWithProfiling("piecesOverall", () -> {

            List<Piece> piecesForPredicate = piecesByPredicates.get(predicate.hashCode());

            if (piecesForPredicate == null) {
                piecesForPredicate = new ArrayList<>();
                Set<Position> sectorPositions = pieces.keySet();
                for (Position sectorPosition : sectorPositions) {
                    piecesForPredicate.addAll(pieces.get(sectorPosition).stream().filter(predicate).toList());
                }
                piecesByPredicates.put(predicate.hashCode(), piecesForPredicate);
            }

            retVal.addAll(piecesForPredicate);

        });

        return retVal;
    }

    public List<Piece> piecesAtPosition(Predicate<Piece> predicate, Position position) {
        return piecesAtPosition(predicate, position.getX(), position.getY());
    }

    public List<Piece> piecesAtPosition(Predicate<Piece> predicate, float x, float y) {
        return piecesInArea(predicate, x, y, 1, 1);
    }

    public List<Piece> piecesInArea(Predicate<Piece> predicate, float x, float y, float width, float height, float grace) {
        return piecesInArea(predicate, x - grace, y - grace, width + 2 * grace, height + 2 * grace);
    }

    public List<Piece> piecesInArea(Predicate<Piece> predicate, float x, float y, float width, float height) {

        Predicate<Piece> withinBoundsPredicate = new Predicate<Piece>() {
            @Override
            public boolean test(Piece piece) {
                return piece.getPosition().getX() >= x && piece.getPosition().getX() < x + width && piece.getPosition().getY() >= y && piece.getPosition().getY() < y + height;
            }
        };

        List<Position> sectorPositions = sectorPositionsCrossingArea(x, y, width, height);
        List<Piece> retVal = new ArrayList<>();
        for (Position sectorPosition : sectorPositions) {
            retVal.addAll(pieces.get(sectorPosition).stream().filter(withinBoundsPredicate).filter(predicate).toList());
        }
        return retVal;
    }

    public void addPiece(Piece piece) {
        Position piecePosition = piece.getPosition();
        int sectorColumn = (int)piecePosition.getX() / sectorWidth;
        int sectorRow = (int)piecePosition.getY() / sectorHeight;
        Position sectorPosition = Position.fromCoordinates(sectorColumn, sectorRow, null);
        List<Piece> piecesInSector = pieces.computeIfAbsent(sectorPosition, k -> new ArrayList<>());
        piecesInSector.add(piece);
        if (piece instanceof Platform platform) {
            Set<Position> positionsCoveredByPlatform = CosmodogMapUtils.positionsCoveredByPlatform(platform);
            positionsCoveredByPlatforms.put(platform, positionsCoveredByPlatform);
        }

        piecesByPredicates.clear();

    }

    public void removePiece(Piece piece) {
        //Positions are segment positions here.
        for (Position position : pieces.keySet()) {
            List<Piece> piecesAtPosition = pieces.get(position);
            if (piecesAtPosition != null) {
                piecesAtPosition.remove(piece);
            }
            if (piece instanceof Platform platform) {
                positionsCoveredByPlatforms.remove(platform);
            }
        }

        piecesByPredicates.clear();
    }

    public void clear() {
        this.pieces.clear();
        this.piecesByPredicates.clear();
    }

    public Set<Position> allPositionsCoveredByPlatforms() {
        return positionsCoveredByPlatforms.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
