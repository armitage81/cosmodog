package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Describes the vision of an enemy, that is all tiles that are visible to that enemy.
 */
public class Vision {

    private Set<Position> elements = new HashSet<>();

    public Set<Position> getElements() {
        return elements;
    }

    public Set<Position> visiblePositions(Actor observer, int mapWidth, int mapHeight) {
        DirectionType directionType = observer.getDirection();

        Set<Position> retVal = new HashSet<Position>();

        for (Position visionElement : elements) {
            float visionElementX = visionElement.getX();
            float visionElementY = visionElement.getY();

            if (directionType == DirectionType.DOWN) {
                visionElementY = -visionElementY;
            } else if (directionType == DirectionType.LEFT) {
                float temp = visionElementX;
                visionElementX = -visionElementY;
                visionElementY = temp;
            } else if (directionType == DirectionType.RIGHT) {
                float temp = visionElementX;
                visionElementX = visionElementY;
                visionElementY = temp;
            }
            visionElementX = observer.getPositionX() + visionElementX;
            visionElementY = observer.getPositionY() + visionElementY;

            if (visionElementX < 0 || visionElementX >= mapWidth) {
                continue;
            }

            if (visionElementY < 0 || visionElementY >= mapHeight) {
                continue;
            }

            retVal.add(Position.fromCoordinates(visionElementX, visionElementY));
        }

        return retVal;
    }

    public boolean visible(Actor observer, Position observable, int mapWidth, int mapHeight) {
        return visiblePositions(observer, mapWidth, mapHeight).contains(observable);
    }
}
