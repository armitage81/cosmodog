package antonafanasjew.cosmodog.model.portals.interfaces;

import java.util.List;

public interface SwitchableHolder {

    public void addSwitchable(int priority, Switchable switchable);

    public List<Switchable> getSwitchables();

}
