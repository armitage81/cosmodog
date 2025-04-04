package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import jdk.jshell.spi.ExecutionControl;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Ray {

    private List<Position> rayPositions = new ArrayList<>();
    private Optional<Position> targetPosition = Optional.empty();
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

    public Optional<Position> getTargetPosition() {
        return targetPosition;
    }

    private static boolean reflectorPresent(Position position) {
        throw new RuntimeException("Not yet implemented.");
    }

    private static boolean positionPenetrable(Position position) {
        throw new RuntimeException("Not yet implemented.");
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
                            ray.targetPosition = Optional.of(lookAheadPosition);
                            finished = true;
                        }
                    } else {
                        ray.targetPosition = Optional.of(lookAheadPosition);
                        finished = true;
                    }
                }
            }


        } while (!finished);

        return ray;
    }

    public void render(GameContainer gc, Graphics g) {
        long timestamp = System.currentTimeMillis() / 250;
        Color color = timestamp % 2 == 0 ? Color.red : Color.blue;
        g.setColor(color);

        /*
        When drawing the ray, following must be considered.
        - If the player stands on a field with an EMP piece, there is no ray.
        - The ray always starts at the position adjacent to the player in the facing direction, with one exception:
        - The exception being when the player is facing an adjacent non-transparent tile, in which case the ray covers no positions.
        - In the simplest case, the ray is a straight line covering one or multiple positions which ends at an non-transparent tile.
        - The ray can be bent though. When the ray hits a reflector, the ray is bent in the direction of the reflector.
        - A bent ray can hit itself (like in the Snake game), in which case the ray ends.
        - A bent ray can also hit the protagonist, in which case the ray also ends.
        - A ray that hits either itself or protagonist does not have a target position.
        - A ray that ends before a non-transparent tile has a target position, which is this tile's position.
        - When drawing a ray, we look at all it's positions and draw the ray's segment for each of those positions.
        - Each position refers to the previous position and to the next position.
        - For the very first position, the previous position is the protagonist's position.
        - For the very last position, the next position is the target position (if it exists)
        - or the position adjacent to the last position in the direction of the last direction.
        - Each segment of the ray is drawn in two steps.
        - The first half is drawn depending on the direction of the previous position.
        - The second half is drawn depending on the direction of the next position.
        - Each half of the segment can be a straight line or an L-shape.
        - If the target position does exist, it is drawn at the end.

         */

        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();
        int tileLength = TileUtils.tileLengthSupplier.get();

        Player protagonist = game.getPlayer();

        if (empAtPlayersPosition()) {
            return;
        }

        List<Position> positions = getRayPositions();
        for (int i = 0; i < positions.size(); i++) {

            Position lookBehindPosition;
            if (i == 0) {
                lookBehindPosition = Position.fromCoordinatesOnPlayerLocationMap((int)protagonist.getPosition().getX(), (int)protagonist.getPosition().getY());
            } else {
                lookBehindPosition = positions.get(i - 1);
            }

            Position position = positions.get(i);
            DirectionType startDirection = DirectionType.direction(lookBehindPosition, position);

            Optional<Position> lookAheadPosition;

            if (i < positions.size() - 1) {
                lookAheadPosition = Optional.of(positions.get(i + 1));
            } else {
                if (getTargetPosition().isPresent()) {
                    lookAheadPosition = getTargetPosition();
                } else {
                    lookAheadPosition = Optional.of(DirectionType.facedAdjacentPosition(position, startDirection));
                }
            }

            DirectionType endDirection = lookAheadPosition.isPresent() ? DirectionType.direction(position, lookAheadPosition.get()) : startDirection;

            g.setLineWidth(3);

            float x1;
            float y1;
            float x2;
            float y2;

            if (startDirection == DirectionType.UP || endDirection == DirectionType.DOWN) {
                x1 = position.getX() * tileLength + tileLength / 2f;
                x2 = x1;
                y1 = position.getY() * tileLength + tileLength;
                y2 = position.getY() * tileLength + tileLength / 2f;
                g.drawLine(x1, y1, x2, y2);
            }

            if (startDirection == DirectionType.DOWN || endDirection == DirectionType.UP) {
                x1 = position.getX() * tileLength + tileLength / 2f;
                x2 = x1;
                y1 = position.getY() * tileLength;
                y2 = position.getY() * tileLength + tileLength / 2f;
                g.drawLine(x1, y1, x2, y2);
            }

            if (startDirection == DirectionType.RIGHT || endDirection == DirectionType.LEFT) {
                x1 = position.getX() * tileLength;
                x2 = position.getX() * tileLength + tileLength / 2f;
                y1 = position.getY() * tileLength + tileLength / 2f;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
            }

            if (startDirection == DirectionType.LEFT || endDirection == DirectionType.RIGHT) {
                x1 = position.getX() * tileLength + tileLength / 2f;
                x2 = position.getX() * tileLength + tileLength;
                y1 = position.getY() * tileLength + tileLength / 2f;
                y2 = y1;
                g.drawLine(x1, y1, x2, y2);
            }

            g.setLineWidth(1);

        }

        Optional<Position> targetPosition = getTargetPosition();
        if (targetPosition.isPresent()) {
            g.setColor(new Color(1, 0, 0, 0.33f));
            g.fillRect(targetPosition.get().getX() * tileLength, targetPosition.get().getY() * tileLength, tileLength, tileLength);
        }
    }

}
