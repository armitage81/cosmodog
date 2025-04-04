package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private static boolean reflectorPresent(Position position) {
        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(position.getMapType());
        return map.dynamicPieceAtPosition(Reflector.class, position).isPresent();
    }

    private static boolean positionPenetrable(Position position) {
        CosmodogMap map = ApplicationContextUtils.getCosmodogGame().getMaps().get(position.getMapType());
        int tileId = map.getTileId(position, Layers.LAYER_META_PORTALS);
        TileType tileType = TileType.getByLayerAndTileId(Layers.LAYER_META_PORTALS, tileId);
        boolean blocking = tileType.equals(TileType.PORTAL_RAY_BLOCKING);
        boolean attachable = tileType.equals(TileType.PORTAL_RAY_ATTACHABLE);
        return !blocking && !attachable;
    }

    private static Optional<DirectionType> reflectionDirection(Position reflectorPosition, DirectionType directionType) {
        throw new RuntimeException("Not yet implemented.");
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
            if (ray.containsPosition(lookAheadPosition)) {
                finished = true;
            } else if (lookAheadPosition.equals(protagonistsPosition)) {
                finished = true;
            } else {
                if (positionPenetrable(lookAheadPosition)) {
                    ray.addPosition(lookAheadPosition);
                    lookAheadPosition = DirectionType.facedAdjacentPosition(lookAheadPosition, directionType);
                } else {
                    if (reflectorPresent(lookAheadPosition)) {
                        Optional<DirectionType> reflectionDirection = reflectionDirection(lookAheadPosition, directionType);
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
