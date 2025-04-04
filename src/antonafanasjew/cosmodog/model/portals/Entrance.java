package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;

public class Entrance {

    private Position position;
    private DirectionType entranceDirection;

    public static Entrance instance(Position position, DirectionType entranceDirection) {
        Entrance entrance = new Entrance();
        entrance.position = position;
        entrance.entranceDirection = entranceDirection;
        return entrance;
    }

    public DirectionType getEntranceDirection() {
        return entranceDirection;
    }

    public Position getPosition() {
        return position;
    }
}
