package antonafanasjew.cosmodog.model.portals;


import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;

import java.io.Serializable;

public class Portal implements Serializable {

    public Position position;
    public DirectionType directionType;

    public Portal(Position position, DirectionType directionType) {
        this.position = position;
        this.directionType = directionType;
    }

}
