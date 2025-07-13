package antonafanasjew.cosmodog.structures;

import antonafanasjew.cosmodog.tiledmap.TiledObject;

import java.io.Serializable;

public class SafeSpace implements Serializable {

    private TiledObject region;

    public TiledObject getRegion() {
        return region;
    }

    public void setRegion(TiledObject region) {
        this.region = region;
    }
}
