package antonafanasjew.cosmodog.model.portals.interfaces;


import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public interface PresenceDetector {

    void presenceDetected(CosmodogGame game, Actor presence);
    void presenceLost(CosmodogGame game);
    //Resets the detector by setting the presence to false.
    void reset();

}
