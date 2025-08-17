package antonafanasjew.cosmodog.model.dynamicpieces.races;

import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.race.InitiateRaceAction;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.DynamicPiece;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.structures.Race;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;

import java.util.Arrays;

public class PolePosition extends DynamicPiece {

    private boolean horizontalNotVertical;
    private Race race;

    public static PolePosition create(Position position, boolean horizontalNotVertical) {
        PolePosition polePosition = new PolePosition();
        polePosition.setPosition(position);
        polePosition.horizontalNotVertical = horizontalNotVertical;
        return polePosition;
    }

    private PolePosition() {

    }

    public boolean isHorizontalNotVertical() {
        return horizontalNotVertical;
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
        return horizontalNotVertical ? "polePositionHorizontal" : "polePositionVertical";
    }

    @Override
    public void interactWhenSteppingOn() {
        if (!race.isStarted() && !race.isSolved()) {
            ApplicationContextUtils.getCosmodogGame().getActionRegistry().registerAction(AsyncActionType.CUTSCENE, new InitiateRaceAction(race));
        }
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Race getRace() {
        return race;
    }
}
