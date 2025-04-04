package antonafanasjew.cosmodog.model.portals.interfaces;

public interface Switchable {

    int numberOfStates();
    int currentState();
    void switchToNextState();

}
