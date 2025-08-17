package antonafanasjew.cosmodog.model.dynamicpieces.races;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.race.CancelRaceAction;
import antonafanasjew.cosmodog.actions.race.WinRaceAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

public class RaceExit extends DynamicPiece {

    private Race race;

    public static RaceExit create(Position position) {
        RaceExit finishLine = new RaceExit();
        finishLine.setPosition(position);
        return finishLine;
    }

    private RaceExit() {

    }

    @Override
    public boolean wrapsCollectible() {
        return false;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return true;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        return "";
    }

    @Override
    public void interactWhenSteppingOn() {
        if (race.isStarted() && !race.isSolved()) {
            ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.CUTSCENE, new CancelRaceAction(race));
        }
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Race getRace() {
        return race;
    }
}
