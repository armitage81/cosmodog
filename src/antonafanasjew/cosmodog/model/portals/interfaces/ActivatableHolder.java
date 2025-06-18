package antonafanasjew.cosmodog.model.portals.interfaces;

import java.util.List;

public interface ActivatableHolder {

    void addActivatable(int priority, Activatable activatable);

    List<Activatable> getActivatables();

}
