package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Reflector;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import jdk.jshell.spi.ExecutionControl;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.io.Serializable;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Ray implements Serializable {

    private List<Position> rayPositions = new ArrayList<>();
    private Position targetPosition = null;
    private DirectionType lastDirection;

    public Ray(DirectionType initialDirection) {
        this.lastDirection = initialDirection;
    }

    public void addPosition(Position position) {
        rayPositions.add(position);
    }

    public boolean containsPosition(Position position) {
        return rayPositions.contains(position);
    }

    public List<Position> getRayPositions() {
        return rayPositions;
    }

    public DirectionType getLastDirection() {
        return lastDirection;
    }

    public Position getTargetPosition() {
        return targetPosition;
    }

    private static Optional<Reflector> reflector(Position position) {
        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(position.getMapType());
        return map.dynamicPieceAtPosition(Reflector.class, position).map(dynamicPiece -> (Reflector) dynamicPiece);
    }

    private static boolean positionPenetrable(Position position) {
        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(position.getMapType());
        int tileId = map.getTileId(position, Layers.LAYER_META_PORTALS);
        TileType tileType = TileType.getByLayerAndTileId(Layers.LAYER_META_PORTALS, tileId);
        boolean blocking = tileType.equals(TileType.PORTAL_RAY_BLOCKING);
        boolean attachable = tileType.equals(TileType.PORTAL_RAY_ATTACHABLE);
        Set<DynamicPiece> dynamicPieces = map.dynamicPiecesAtPosition(position);
        boolean dynamicPiecePermeable = true;
        for (DynamicPiece dynamicPiece : dynamicPieces) {
            if (!dynamicPiece.permeableForPortalRay(null)) {
                dynamicPiecePermeable = false;
                break;
            }
        }
        return !blocking && !attachable && dynamicPiecePermeable;
    }

    private static Optional<DirectionType> reflectionDirection(Reflector reflector, DirectionType directionType) {

        Optional<DirectionType> retVal = Optional.empty();
        ReflectionType reflectionType = reflector.getReflectionType();

        if (directionType == DirectionType.RIGHT) {
            if (reflectionType == ReflectionType.NORTH_WEST) {
                retVal = Optional.of(DirectionType.UP);
            }
            if (reflectionType == ReflectionType.SOUTH_WEST) {
                retVal = Optional.of(DirectionType.DOWN);
            }
        }

        if (directionType == DirectionType.LEFT) {
            if (reflectionType == ReflectionType.NORTH_EAST) {
                retVal = Optional.of(DirectionType.UP);
            }
            if (reflectionType == ReflectionType.SOUTH_EAST) {
                retVal = Optional.of(DirectionType.DOWN);
            }
        }

        if (directionType == DirectionType.UP) {
            if (reflectionType == ReflectionType.SOUTH_EAST) {
                retVal = Optional.of(DirectionType.RIGHT);
            }
            if (reflectionType == ReflectionType.SOUTH_WEST) {
                retVal = Optional.of(DirectionType.LEFT);
            }
        }

        if (directionType == DirectionType.DOWN) {
            if (reflectionType == ReflectionType.NORTH_EAST) {
                retVal = Optional.of(DirectionType.RIGHT);
            }
            if (reflectionType == ReflectionType.NORTH_WEST) {
                retVal = Optional.of(DirectionType.LEFT);
            }
        }

        return retVal;

    }

    private boolean empAtPlayersPosition() {
        throw new RuntimeException("Not yet implemented.");
    }

    public static Ray create(CosmodogMap map, Player player) {


        Position protagonistsPosition = Position.fromCoordinatesOnPlayerLocationMap(player.getPosition().getX(), player.getPosition().getY());
        DirectionType directionType = player.getDirection();

        Ray ray = new Ray(player.getDirection());

        boolean finished = false;
        Position lookAheadPosition = DirectionType.facedAdjacentPosition(protagonistsPosition, directionType);
        do {
            if (lookAheadPosition.equals(protagonistsPosition)) {
                finished = true;
            } else {
                if (positionPenetrable(lookAheadPosition)) {
                    ray.addPosition(lookAheadPosition);
                    lookAheadPosition = DirectionType.facedAdjacentPosition(lookAheadPosition, directionType);
                } else {
                    Optional<Reflector> optReflector = reflector(lookAheadPosition);
                    if (optReflector.isPresent()) {
                        Optional<DirectionType> reflectionDirection = reflectionDirection(optReflector.get(), directionType);
                        if (reflectionDirection.isPresent()) {
                            ray.addPosition(lookAheadPosition);
                            directionType = reflectionDirection.get();
                            ray.lastDirection = directionType;
                            lookAheadPosition = DirectionType.facedAdjacentPosition(lookAheadPosition, directionType);
                        } else {
                            ray.targetPosition = lookAheadPosition;
                            finished = true;
                        }
                    } else {
                        ray.targetPosition = lookAheadPosition;
                        finished = true;
                    }
                }
            }


        } while (!finished);

        return ray;
    }

    public void render(GameContainer gc, Graphics g) {

    }

}
