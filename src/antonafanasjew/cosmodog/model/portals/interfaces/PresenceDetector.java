package antonafanasjew.cosmodog.model.portals.interfaces;


import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public interface PresenceDetector {

    void presenceDetected(CosmodogMap map, Actor presence);
    void presenceLost(CosmodogMap map);

}
