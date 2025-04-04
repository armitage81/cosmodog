package antonafanasjew.cosmodog.model.portals.tiles;


import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.portals.Tile;
import antonafanasjew.cosmodog.model.portals.interfaces.Immaterial;

public class Glass extends Tile {

    public Glass(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean passable(Actor actor, DirectionType directionType) {
        return actor instanceof Immaterial;
    }

    @Override
    public boolean transparent() {
        return true;
    }

}
