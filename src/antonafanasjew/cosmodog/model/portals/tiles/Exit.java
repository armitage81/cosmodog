package antonafanasjew.cosmodog.model.portals.tiles;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Tile;

public class Exit extends Tile {

    public Exit(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return true;
    }

    @Override
    public boolean transparent() {
        return true;
    }

}
