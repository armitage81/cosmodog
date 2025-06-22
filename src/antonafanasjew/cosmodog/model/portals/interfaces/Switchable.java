package antonafanasjew.cosmodog.model.portals.interfaces;

import antonafanasjew.cosmodog.topology.Position;

public interface Switchable {

    int numberOfStates();
    int currentState();
    void switchToNextState();
    Position getPosition();
    void switchToInitialState();

}
