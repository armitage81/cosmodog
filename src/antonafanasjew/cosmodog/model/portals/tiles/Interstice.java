package antonafanasjew.cosmodog.model.portals.tiles;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Tile;
import antonafanasjew.cosmodog.model.portals.interfaces.Small;

public class Interstice extends Tile {

    public Interstice(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return actor instanceof Small;
    }

    @Override
    public boolean transparent() {
        return true;
    }

}
