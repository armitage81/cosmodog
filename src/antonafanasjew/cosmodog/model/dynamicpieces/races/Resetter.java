package antonafanasjew.cosmodog.model.dynamicpieces.races;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.SoundResources;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class Resetter extends DynamicPiece {

    private Race race;

    public static Resetter create(Position position) {
        Resetter resetter = new Resetter();
        resetter.setPosition(position);
        return resetter;
    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return false;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "resetter";
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    @Override
    public void interactWhenSteppingOn() {
        if (race.isStarted() && !race.isSolved()) {
            ApplicationContext.instance().getSoundResources().get(SoundResources.SOUND_TRAFFICBARRIERRESET).play();
            Player player = ApplicationContextUtils.getPlayer();
            int turn = player.getGameProgress().getTurn();
            race.getTrafficBarriers().forEach(t -> t.setReferenceTurn(turn));
        }
    }
}
