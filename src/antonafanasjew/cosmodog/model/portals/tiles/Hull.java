package antonafanasjew.cosmodog.model.portals.tiles;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Tile;

public class Hull extends Tile {

    public Hull(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return false;
    }

    @Override
    public boolean transparent() {
        return false;
    }
}
