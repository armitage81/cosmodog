package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.model.DynamicPiece;

public class Pillar extends DynamicPiece {



    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "";
    }
}
