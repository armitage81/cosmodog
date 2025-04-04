package antonafanasjew.cosmodog.model.portals.interfaces;

import antonafanasjew.cosmodog.model.CosmodogMap;

public interface Activatable {

        void activate();

        void deactivate();

        boolean isActive();

        boolean canActivate(CosmodogMap map);

        boolean canDeactivate(CosmodogMap map);
}
