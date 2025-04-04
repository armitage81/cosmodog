package antonafanasjew.cosmodog.model.portals;


import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;

public class Portal {

    public Position position;
    public DirectionType directionType;

    public Portal(Position position, DirectionType directionType) {
        this.position = position;
        this.directionType = directionType;
    }

}
