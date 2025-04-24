package antonafanasjew.cosmodog.model.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;

public class Entrance {

    private Position position;
    private DirectionType entranceDirection;
    private boolean usedPortal;

    //This properties are only defined if usedPortal == true.
    private Portal entrancePortal = null;
    private Portal exitPortal = null;

    private boolean waited;

    private Entrance() {

    }

    public static Entrance instance(Position position, DirectionType entranceDirection) {
        return instance(position, entranceDirection, false, null, null, false);
    }

    public static Entrance instanceForPortals(Position position, DirectionType entranceDirection, Portal entrancePortal, Portal exitPortal) {
        return instance(position, entranceDirection, true, entrancePortal, exitPortal, false);
    }

    public static Entrance instanceForWaiting(Position position, DirectionType entranceDirection) {
        return instance(position, entranceDirection, false, null, null, true);
    }

    private static Entrance instance(Position position, DirectionType entranceDirection, boolean usedPortal,Portal entrancePortal, Portal exitPortal, boolean waited) {
        Entrance entrance = new Entrance();
        entrance.position = position;
        entrance.entranceDirection = entranceDirection;
        entrance.usedPortal = usedPortal;
        entrance.entrancePortal = entrancePortal;
        entrance.exitPortal = exitPortal;
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

    public Portal getEntrancePortal() {
        return entrancePortal;
    }

    public Portal getExitPortal() {
        return exitPortal;
    }
}
