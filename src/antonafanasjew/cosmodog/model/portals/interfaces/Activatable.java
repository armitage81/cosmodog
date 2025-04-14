package antonafanasjew.cosmodog.model.portals.interfaces;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;

public interface Activatable {

        void activate();

        void deactivate();

        boolean isActive();

        boolean canActivate(CosmodogGame game);

        boolean canDeactivate(CosmodogGame game);
}
