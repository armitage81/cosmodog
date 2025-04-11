package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;

public class Entrance {

    private Position position;
    private DirectionType entranceDirection;
    private boolean usedPortal;
    private boolean waited;

    private Entrance() {

    }

    public static Entrance instance(Position position, DirectionType entranceDirection) {
        return instance(position, entranceDirection, false, false);
    }

    public static Entrance instance(Position position, DirectionType entranceDirection, boolean usedPortal, boolean waited) {
        Entrance entrance = new Entrance();
        entrance.position = position;
        entrance.entranceDirection = entranceDirection;
        entrance.usedPortal = usedPortal;
        entrance.waited = waited;
        return entrance;
    }

    public DirectionType getEntranceDirection() {
        return entranceDirection;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isWaited() {
        return waited;
    }

    public boolean isUsedPortal() {
        return usedPortal;
    }
}
