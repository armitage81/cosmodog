package antonafanasjew.cosmodog.model.portals.interfaces;


import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;

public interface PresenceDetector {

    void presenceDetected(CosmodogGame game, Actor presence);
    void presenceLost(CosmodogGame game);

}
