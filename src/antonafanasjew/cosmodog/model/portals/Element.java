package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.topology.Position;

public abstract class Element {

    public int positionX;
    public int positionY;

    public Element(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    @Override
    public String toString() {
        return positionX + "/" + positionY;
    }

    public abstract boolean passable(Actor actor, DirectionType directionType);

    public abstract boolean transparent();

    public Position getPosition() {
        return Position.fromCoordinatesOnPlayerLocationMap(positionX, positionY);
    }
}
