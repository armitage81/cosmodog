package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;

public class Emitter extends DynamicPiece {
    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "";
    }
}
