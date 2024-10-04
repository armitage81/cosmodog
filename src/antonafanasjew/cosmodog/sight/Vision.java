package antonafanasjew.cosmodog.sight;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

import java.util.HashSet;
import java.util.Set;

public class Vision {

    private Set<VisionElement> elements = new HashSet<VisionElement>();

    public Set<VisionElement> getElements() {
        return elements;
    }

    public Set<Position> visiblePositions(Actor observer, int mapWidth, int mapHeight) {
        DirectionType directionType = observer.getDirection();

        Set<Position> retVal = new HashSet<Position>();

        for (VisionElement visionElement : elements) {
            int visionElementX = visionElement.getX();
            int visionElementY = visionElement.getY();

            if (directionType == DirectionType.DOWN) {
                visionElementY = -visionElementY;
            } else if (directionType == DirectionType.LEFT) {
                int temp = visionElementX;
                visionElementX = -visionElementY;
                visionElementY = temp;
            } else if (directionType == DirectionType.RIGHT) {
                int temp = visionElementX;
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

    public boolean visible(Actor observer, Position observable) {
        return visiblePositions(observer).contains(observable);
    }
}
